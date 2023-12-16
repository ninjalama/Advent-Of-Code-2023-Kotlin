package puzzles.day10

import utils.ResourceUtils
import java.awt.Polygon

fun String.isNumber(): Boolean {
    return this.toDoubleOrNull() != null
}

data class Position(val x: Int, val y: Int)

data class Sketch(val inputString: String) {
    val input: List<List<Char>>
    val startingPipe: Pipe
    val maxXpos: Int
    val maxYpos: Int

    init {
        this.input = this.inputString.split("\n").map { line ->
            line.toCharArray().toList()
        }
        maxXpos = this.input[0].size - 1
        maxYpos = this.input.size - 1

        val outerIndex = this.input.indexOfFirst { innerList -> innerList.contains('S') }
        val innerIndex = if (outerIndex != -1) {
            this.input[outerIndex].indexOf('S')
        } else 0

        val sPosY = outerIndex
        val sPosX = innerIndex

        startingPipe = Pipe(this.input[sPosY][sPosX], Position(sPosX, sPosY), 0)
    }
    
    fun calculateEnclosedTiles(): Int {
        val distances = calculatePipeDistances()
        val polygon = Polygon()
        
        val listOfPipes = MutableList(maxYpos+1) { MutableList<Pipe?>(maxXpos+1) { null } }
        
        (distances.first.map { pipe ->
            listOfPipes[pipe.pos.y][pipe.pos.x] = pipe
            Position(pipe.pos.x, pipe.pos.y)
        } + (distances.second?.reversed()?.map { pipe ->
            listOfPipes[pipe.pos.y][pipe.pos.x] = pipe
            Position(pipe.pos.x, pipe.pos.y)
        } ?: emptyList())).forEach {
            polygon.addPoint(it.x, it.y)
        }
        
        var enclosed = 0
        for(y in 0..maxYpos) {
            for (x in 0..maxXpos) {
                val isPipe = listOfPipes.getOrNull(y)?.getOrNull(x) != null
                if (!isPipe && polygon.contains(x, y)) {
                    enclosed += 1
                }
            }
        }
        
        return enclosed
    }
    
    fun calculatePipeDistances(): Pair<List<Pipe>, List<Pipe>?> {
        val startingPipeNeighbors = startingPipe.getNeighbours()
        val startingPipeConnectedNeighbors = startingPipeNeighbors.filter { neighborPosition ->
            val pipe = getPipeAt(neighborPosition)
            pipe?.getConnectedPositions()?.contains(startingPipe.pos) ?: false
        }

        val startingPipes = startingPipeNeighbors.filter { neighborPosition ->
            val pipe = getPipeAt(neighborPosition)
            pipe?.getConnectedPositions()?.contains(startingPipe.pos) ?: false
        }.map {
            getPipeAt(it)
        }.filterNotNull()
        
        val startingListA = listOf(startingPipe, startingPipes.first().copy(distance = 1))
        val startingListB = listOf(startingPipe, startingPipes.last().copy(distance = 1))
        
        return calculatePipeDistances(startingListA, startingListB, 1)
    }
    
    tailrec fun calculatePipeDistances(pipeListA: List<Pipe>, pipeListB: List<Pipe>?, count: Int): Pair<List<Pipe>, List<Pipe>?> {
        
        fun getNextPipe(pipeList: List<Pipe>): Pipe? {
            return pipeList.last().getConnectedPositions()
                .filterNot { pipeList.takeLast(2).map { it.pos }.contains(Position(it.x, it.y))}
                .map {
                    getPipeAt(it)
                }
                .firstOrNull()
        }
        
        return if (pipeListA.last() == pipeListB?.last()) pipeListA to pipeListB.orEmpty()
        else {
            // Get next pipe to look at for pipeListA
            val nextPipeA = getNextPipe(pipeListA)
            val nextPipeB = getNextPipe(pipeListB.orEmpty())
            
            if (nextPipeA == null && nextPipeB == null){
                pipeListA to pipeListB  
            } else {
                val newListA = nextPipeA?.let { pipeListA + it.copy(distance = count + 1) } ?: pipeListA
                val newListB = pipeListB?.let { if (nextPipeB != null) it + nextPipeB.copy(distance = count + 1) else it }
                calculatePipeDistances(newListA, newListB, count + 1)
            }
        }
    }

    fun getCharAt(position: Position): Char? {
        return if (position.y >= 0 && position.y <= maxYpos && position.x >= 0 && position.x <= maxXpos) {
            this.input[position.y][position.x]
        } else null
    }

    fun getPipeAt(position: Position): Pipe? {
        val char = getCharAt(position)
        return char?.let { Pipe(it, position, 0) }
    }
}

data class Pipe(val symbol: Char, val pos: Position, val distance: Int = 0) {
    fun getNeighbours(): List<Position> {
        val listOfNeighbors = mutableListOf<Position>()
        for (xPos in -1..1) {
            for (yPos in -1..1) {
                listOfNeighbors.add(Position(pos.x - xPos, pos.y - yPos))
            }
        }
        return listOfNeighbors.toList().filterNot { it.x == pos.x && it.y == pos.y }
    }

    fun getConnectedPositions(): List<Position> {
        val xPos = pos.x
        val yPos = pos.y
        return when (symbol) {
            '|' -> listOf(
                Position(xPos, yPos - 1),
                Position(xPos, yPos + 1)
            )

            '-' -> listOf(
                Position(xPos - 1, yPos),
                Position(xPos + 1, yPos)
            )

            'L' -> listOf(
                Position(xPos, yPos - 1),
                Position(xPos + 1, yPos)
            )

            'J' -> listOf(
                Position(xPos, yPos - 1),
                Position(xPos - 1, yPos)
            )

            '7' -> listOf(
                Position(xPos - 1, yPos),
                Position(xPos, yPos + 1)
            )

            'F' -> listOf(
                Position(xPos + 1, yPos),
                Position(xPos, yPos + 1)
            )

            else -> emptyList()
        }
    }
}

fun printPrettyTable(data: List<List<String>>) {
    val redColor = "\u001B[31m"
    val greenColor = "\u001B[32m"
    val whiteColor = "\u001B[37m"
    val resetColor = "\u001B[0m"
    
    // Find maximum lengths of strings in each column
    val columnWidths = MutableList(data[0].size) { 0 }
    for (row in data) {
        for ((index, value) in row.withIndex()) {
            columnWidths[index] = maxOf(columnWidths[index], value.length)
        }
    }

    // Print table
    for (row in data) {
        for ((index, value) in row.withIndex()) {
            val color = when(value) {
                "I" -> greenColor
                "O" -> redColor
                else -> whiteColor
            }
            print(color + value.padEnd(columnWidths[index] + 2) + resetColor) // Add padding for spacing
        }
        println()
    }
}

fun main(args: Array<String>) {
    fun part1() {
        // val input = ResourceUtils.getResourceAsText("/day10/sampleInput.txt").orEmpty()
        val input = ResourceUtils.getResourceAsText("/day10/input.txt").orEmpty()
        val sketch = Sketch(input)
        // printPrettyTable(sketch.input.map { it.map { it.toString() }})
        
        // calculatePipeDistances gives out a Pair of the lists of steps necessary
        // to get both ways from starting point 'S' to the end-point
        val distance = sketch.calculatePipeDistances().first.last().distance
        println("Distance: " + distance)
    }
    
    fun part2() {
        // val input = ResourceUtils.getResourceAsText("/day10/sampleInputPart2.txt").orEmpty()
        val input = ResourceUtils.getResourceAsText("/day10/input.txt").orEmpty()
        // printPrettyTable(sketch.input.map { it.map { it.toString()}})
        println("Enclosed tiles: " + Sketch(input).calculateEnclosedTiles())
    }
    
    part1()
    part2()
}