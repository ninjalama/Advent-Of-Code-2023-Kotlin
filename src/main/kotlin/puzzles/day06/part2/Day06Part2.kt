package puzzles.day06.part2

import utils.ResourceUtils

fun main(args: Array<String>) {
    val timeStart = System.currentTimeMillis()
    
    val input = ResourceUtils.getResourceAsText("/day06/input.txt").orEmpty()
    val lines = input.split("\n")
    
    val time = lines[0].replace(" ", "").removePrefix("Time:").toLong()
    val distance = lines[1].replace(" ", "").removePrefix("Distance:").toLong()
    
    // TODO: Optimize..
    var numberOfWays: Long = 0
    (1..time).forEach { msButtonHeld ->
        if (msButtonHeld * (time - msButtonHeld) > distance) numberOfWays += 1
    }
    val executionTime = System.currentTimeMillis() - timeStart
    println("" + executionTime + "ms execution time")
    println("Number of ways to win: " + numberOfWays)
}