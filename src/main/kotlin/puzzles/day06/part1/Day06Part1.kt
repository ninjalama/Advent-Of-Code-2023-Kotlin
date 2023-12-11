package puzzles.day06.part1

import utils.ResourceUtils

data class Race(val time: Int, val distance: Int)

fun distanceInMilimeters(speed: Int, duration: Int) = speed * duration
    
fun main(args: Array<String>) {
    fun calcPart1(input: String): Int {
        val lines = input.split("\n")
        val times = lines[0].replace(Regex("\\s+"), " ").removePrefix("Time: ").split(" ").map { it.toInt() }
        val distances = lines[1].replace(Regex("\\s+"), " ").removePrefix("Distance: ").split(" ").map { it.toInt() }

        val races: List<Race> = times.zip(distances).map {
            Race(it.first, it.second)
        }.onEach(::println)

        return races.map { race ->
            race to (1..race.time).filter { msButtonHeld ->
                distanceInMilimeters(msButtonHeld, race.time - msButtonHeld) > race.distance
            }
        }.map { p ->
            p.second.count()
        }.reduce { acc, next -> acc * next }
    }
    
    fun part1() {
        println("PART1 - Sample")
        val sampleInput = ResourceUtils.getResourceAsText("/day06/sampleInput.txt").orEmpty()
        val sampleCalced = calcPart1(sampleInput)
        println("Ways to win multiplied: " + sampleCalced)

        println("PART 1")
        val input = ResourceUtils.getResourceAsText("/day06/input.txt").orEmpty()
        val part1Calced = calcPart1(input)
        println("Ways to win multiplied: " + part1Calced)
    }

    part1()
}