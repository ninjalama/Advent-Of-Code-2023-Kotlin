package puzzles.day05

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

typealias Seeds = List<Int>


data class Map(val mapType: MapType, val mapRanges: List<MapRange>) {
    
    fun findDestinationMapRange(number: Int): Int {
        val destinationMapRange = mapRanges.find {
            number in (it.sourceRangeStart..it.sourceRangeStart + it.rangeLength -1)
        }
        return when (destinationMapRange) {
            null -> number
            else -> destinationMapRange.destinationRangeStart + (number - destinationMapRange.sourceRangeStart)
        }
    }
    
}
data class MapRange(val destinationRangeStart: Int, val sourceRangeStart: Int, val rangeLength: Int)

data class Almanac(val input: String) {
    val seeds: Seeds
    val mapRanges: List<Map>
    
    init {
        val inputLines = input.split("\n\n").map { it.lines() }
        seeds = inputLines[0][0].split("seeds: ").last().split(" ").map { it.toInt() }
        
        mapRanges = inputLines.drop(1).map { 
            val mapName = it.take(1).joinToString("")
            mapName to it.drop(1)
        }.let {
            it.map {
                val name = it.first
                val ranges = it.second.map {
                    val (dest, src, len) = it.split(" " ).map { it.toInt() }
                    MapRange(dest, src, len)
                }
                Map(mapNameStrToMapNameEnum(it.first), ranges)
            }
        }
    }
    
    fun findLocations(): List<Int> {
        return seeds.map { seed ->
            // Bit lazy - just want to quickly see it works
            val soil = mapRanges[0].findDestinationMapRange(seed)
            val fertilizer = mapRanges[1].findDestinationMapRange(soil)
            val water = mapRanges[2].findDestinationMapRange(fertilizer)
            val light = mapRanges[3].findDestinationMapRange(water)
            val temperature = mapRanges[4].findDestinationMapRange(light)
            val humidity = mapRanges[5].findDestinationMapRange(temperature)
            val location = mapRanges[6].findDestinationMapRange(humidity)
            //print("Seed: " + seed + " Soil: " + soil + " Fertilizer: " + fertilizer + " Water: " + water + " Light: " + light + "Temperature: " + temperature)
            //print(" Humidity: " + humidity + " Location: " + location + "\n")
            location
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

    }
    
    part1()
}