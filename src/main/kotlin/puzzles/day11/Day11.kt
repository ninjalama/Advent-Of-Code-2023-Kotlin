package puzzles.day11

import utils.ResourceUtils

fun List<ImageObject.Galaxy>.getShortestDistancesSum(): Long {
    return this.flatMapIndexed { idx, galaxyA ->
        this.takeLast(this.size - (idx+1)).map { galaxyB ->
            galaxyA to galaxyB
        }
    }.map {
        val xAxisDiff = kotlin.math.abs(it.first.position.xSum() - it.second.position.xSum())
        val yAxisDiff = kotlin.math.abs(it.first.position.ySum() - it.second.position.ySum())

        xAxisDiff + yAxisDiff
    }.sum()
}

data class Position(val originalX: Long, val originalY: Long, val xIncreasedBy: Long = 0, val yIncreasedBy: Long = 0) {
    fun xSum() = originalX + xIncreasedBy
    fun ySum() = originalY + yIncreasedBy
}

sealed class ImageObject {
    override abstract fun toString(): String

    data class Galaxy(val position: Position, val number: Int?) : ImageObject() {
        override fun toString(): String = number?.toString() ?: "#"
    } 
    object EmptySpace : ImageObject() {
        override fun toString(): String = "."
    }
}

class Image(val image: List<List<ImageObject>>) {
    companion object {
        fun fromInputString(inputString: String): Image {
            var galaxyCounter = 1
            val imageObjList = inputString.split("\n").mapIndexed { idxY, str ->
                str.mapIndexed { idxX, char ->
                    when (char) {
                        '#' -> {
                            ImageObject.Galaxy(Position(idxX.toLong(), idxY.toLong()), galaxyCounter++)   
                        }
                        else -> ImageObject.EmptySpace
                    }
                }
            }
            return Image(imageObjList)
        }
    }
    
    fun getRowIndexesWithNoGalaxy(): List<Int> {
       return image.mapIndexedNotNull { idx, innerList ->
            if (innerList.none { it is ImageObject.Galaxy }) idx else null
        }
    }
    
    fun getColumnIndexesWithNoGalaxy(): List<Int> {
        // Get columnIndexes without any columns with a Galaxy in it
        return image.indices.filter { columnIndex ->
            image.none { it[columnIndex] is ImageObject.Galaxy }
        }        
    }
    
    fun getGalaxiesWitEmptyRowsAndColsIncremented(increment: Int): List<ImageObject.Galaxy> {
        val rowIndexesWithNoGalaxy = getRowIndexesWithNoGalaxy()
        val columnIndexesWithNoGalaxy = getColumnIndexesWithNoGalaxy()
        
        val galaxies = image.flatMap { it.filterIsInstance<ImageObject.Galaxy>() }.toMutableList()
        
        rowIndexesWithNoGalaxy.forEachIndexed { iterationIdx, rowIndex ->
            galaxies.indices.forEach { idx ->
                if (galaxies[idx].position.originalY > rowIndex) {
                    val pos = galaxies[idx].position
                    galaxies[idx] = galaxies[idx].copy(position = pos.copy(yIncreasedBy = pos.yIncreasedBy + increment - 1))
                }
            }
        }
        
        columnIndexesWithNoGalaxy.forEachIndexed { iterationIdx, columnIndex ->
            galaxies.indices.forEach { idx ->
                if (galaxies[idx].position.originalX > columnIndex) {
                    val pos = galaxies[idx].position
                    galaxies[idx] = galaxies[idx].copy(position = pos.copy(xIncreasedBy = pos.xIncreasedBy + increment - 1))
                }
            }
        }

        return galaxies.toList()
    }
}

fun main(args: Array<String>) {

    fun part1() {
        val input = ResourceUtils.getResourceAsText("/day11/input.txt").orEmpty()
        val image = Image.fromInputString(input)
        
        val sum = image.getGalaxiesWitEmptyRowsAndColsIncremented(2).getShortestDistancesSum()
        println("Part 1")
        println("Shortest distances sum: " + sum)
    }
    
    fun part2() {
        val input = ResourceUtils.getResourceAsText("/day11/input.txt").orEmpty()
        val image = Image.fromInputString(input)

        val sum = image.getGalaxiesWitEmptyRowsAndColsIncremented(1000000).getShortestDistancesSum()
        println("Part 2")
        println("Shortest distances sum: " + sum)
    }
    
    part1()
    part2()
}