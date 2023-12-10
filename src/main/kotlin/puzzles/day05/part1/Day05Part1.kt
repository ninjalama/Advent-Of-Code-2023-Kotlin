package puzzles.day05.part1

import utils.ResourceUtils


enum class MapType {
    SeedToSoil,
    SoilToFertilizer,
    FertilizerToWater,
    WaterToLight,
    LightToTemperature,
    TemperatureToHumidity,
    HumidityToLocations
}

typealias Seeds = List<Long>

data class Map(val mapType: MapType, val mapRanges: List<MapRange>) {
    
    fun findDestinationMapRange(number: Long): Long {
        val destinationMapRange = mapRanges.find {
            number in (it.sourceRangeStart..it.sourceRangeStart + it.rangeLength -1)
        }
        return when (destinationMapRange) {
            null -> number
            else -> destinationMapRange.destinationRangeStart + (number - destinationMapRange.sourceRangeStart)
        }
    }
}
data class MapRange(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)

data class Almanac(val input: String) {
    val seeds: Seeds
    val mapRanges: List<Map>
    
    init {
        val inputLines = input.split("\n\n").map { it.lines() }
        seeds = inputLines[0][0].split("seeds: ").last().split(" ").map { it.toLong() }
        
        mapRanges = inputLines.drop(1).map { 
            val mapName = it.take(1).joinToString("")
            mapName to it.drop(1)
        }.let {
            it.map {
                val name = it.first
                val ranges = it.second.map {
                    val (dest, src, len) = it.split(" " ).map { it.toLong() }
                    MapRange(dest, src, len)
                }
                Map(mapNameStrToMapNameEnum(it.first), ranges)
            }
        }
    }

    fun findLocations(): List<Long> {
        return seeds.map { seed ->
            mapRanges.fold(seed) { acc, map ->
                map.findDestinationMapRange(acc)
            }
        }
    }
    
    private fun mapNameStrToMapNameEnum(mapName: String): MapType {
        return when {
            mapName.startsWith("seed-to-soil") -> MapType.SeedToSoil
            mapName.startsWith("soil-to-fertilizer") -> MapType.SoilToFertilizer
            mapName.startsWith("fertilizer-to-water") -> MapType.FertilizerToWater
            mapName.startsWith("water-to-light") -> MapType.WaterToLight
            mapName.startsWith("light-to-temperature") -> MapType.LightToTemperature
            mapName.startsWith("temperature-to-humidity") -> MapType.TemperatureToHumidity
            mapName.startsWith("humidity-to-location") -> MapType.HumidityToLocations
            else -> throw Exception("Unknown mapname: " + mapName)
        }
    }
}

fun main(args: Array<String>) {
    
    fun part1() {
        println("PART1 - SAMPLE")
        val sampleInput = ResourceUtils.getResourceAsText("/day05/sampleInput.txt").orEmpty()

        val sampleAlmanac = Almanac(sampleInput)

        println("Lowest location: " + sampleAlmanac.findLocations().min())


        val input = ResourceUtils.getResourceAsText("/day05/input.txt").orEmpty()
        val almanac = Almanac(input)
        println("Lowest location: " + almanac.findLocations().min())
    }
    
    part1()
}