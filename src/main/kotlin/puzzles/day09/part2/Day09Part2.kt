package puzzles.day09.part2

import utils.ResourceUtils

tailrec fun diffs(intList: List<List<Int>>): List<List<Int>> {
    return if (!intList.last().any { it != 0 }) {
        intList
    } else {
        val diffedList: List<Int> = intList.last().zipWithNext { a, b ->
            b - a
        }
        diffs(intList + listOf(diffedList))
    }
}

fun calculateHistory(list: List<Int>): List<List<Int>> {
    val diffsReversed = diffs(listOf(list)).reversed()
    val placeHolders = mutableListOf(0)
    
    return diffsReversed.mapIndexed() { idx, element ->
        if (idx == 0) element + 0
        else {
            val placeHolderValue = element.first() - placeHolders.last()
            placeHolders.add(placeHolderValue)
            listOf(placeHolderValue) + element
        }
    }.reversed()
}



fun main(args: Array<String>) {
    // val input = ResourceUtils.getResourceAsText("/day09/sampleInput.txt").orEmpty()
    val input = ResourceUtils.getResourceAsText("/day09/input.txt").orEmpty()
    
    val lines = input.split("\n").map {
        it.split(" ").map { it.toInt() }
    }

    val historySum = lines.map {
        calculateHistory(it)
    }.map {
        it.first().first()
    }.sum()

    println("Sum of history: " + historySum)
}