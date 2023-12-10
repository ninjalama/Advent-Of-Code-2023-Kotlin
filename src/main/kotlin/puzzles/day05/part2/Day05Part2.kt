package puzzles.day05.part2

import utils.ResourceUtils

typealias Seeds = List<Long>

typealias SeedPairs = List<Pair<Long, Long>>

data class Map(val mapRanges: List<MapRange>) {

    fun findDestinationMapRange(number: Long): Long {
        val destinationMapRange = mapRanges.find {
            number in (it.sourceRangeStart..it.sourceRangeStart + it.rangeLength - 1)
        }
        return when (destinationMapRange) {
            null -> number
            else -> destinationMapRange.destinationRangeStart + (number - destinationMapRange.sourceRangeStart)
        }
    }
}

data class MapRange(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)

data class Almanac(val input: String) {
    val seedPairs: SeedPairs
    val mapRanges: List<Map>

    init {
        val inputLines = input.split("\n\n").map { it.lines() }
        val seedLine = inputLines[0][0].split("seeds: ").last()
        seedPairs = seedLine.split(" ").map { it.toLong() }.chunked(2) { (first, second) -> first to second }

        mapRanges = inputLines.drop(1).map {
            val mapName = it.take(1).joinToString("")
            mapName to it.drop(1)
        }.let {
            it.map {
                val name = it.first
                val ranges = it.second.map {
                    val (dest, src, len) = it.split(" ").map { it.toLong() }
                    MapRange(dest, src, len)
                }
                Map(ranges)
            }
        }
    }
    fun findSmallestLocation(): Long {
        var smallestLocation: Long = Long.MAX_VALUE

        seedPairs.map { seedPair ->
            println("Checking seed pair: " + seedPair)
            for (seed in (seedPair.first..seedPair.first + seedPair.second-1)) {
                val location = mapRanges.fold(seed) { acc, map ->
                    map.findDestinationMapRange(acc)
                }
                smallestLocation = kotlin.math.min(smallestLocation, location)
            }
            println("Done checking seedPair: " + seedPair)
            smallestLocation
        }
        return smallestLocation
    }
}

fun main(args: Array<String>) {

    fun part2() {
        println("PART2 - SAMPLE")
        val sampleInput = ResourceUtils.getResourceAsText("/day05/sampleInput.txt").orEmpty()
        val sampleAlmanac = Almanac(sampleInput)
        println("Lowest location: " + sampleAlmanac.findSmallestLocation())


        val input = ResourceUtils.getResourceAsText("/day05/input.txt").orEmpty()
        val almanac = Almanac(input) 
        println("Lowest location: " + almanac.findSmallestLocation())
    }

    part2()
}