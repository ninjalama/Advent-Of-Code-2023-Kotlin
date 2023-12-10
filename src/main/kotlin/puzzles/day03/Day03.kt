package puzzles.day03.two

import utils.ResourceUtils

typealias SchematicRow = List<SchematicElement>

sealed class SchematicElement
data class SchematicNumber(val number: Int, val length: Int, val row: Int, val column: Int) : SchematicElement()
data class SchematicSymbol(val value: Char, val row: Int, val column: Int) : SchematicElement()

data class Schematic(val schematicStringList: List<String>) {
    val schematicRows: List<SchematicRow>

    init {
        schematicRows = schematicStringList.mapIndexed { idx, schematicString ->
            buildSchematicRows(schematicString, idx)
        }
    }

    private fun buildSchematicRows(input: String, row: Int): List<SchematicElement> {
        val numberOrSymbolRegex = Regex("[^\\w.]+|\\d+")

        return numberOrSymbolRegex.findAll(input).map { matchResult ->
            val value = matchResult.value
            val startPos = matchResult.range.first
            if (value[0].isDigit()) // TODO: Fix this, it's hackish
                SchematicNumber(value.toInt(), value.length, row, startPos)
            else SchematicSymbol(value[0], row, startPos)
        }.toList()
    }

    fun findParts(): List<SchematicNumber> {
        return schematicRows.windowed(2).map { window ->
            val numbers = window.flatten().filterIsInstance<SchematicNumber>()
            val symbols = window.flatten().filterIsInstance<SchematicSymbol>()
            numbers.filter { n ->
                symbols.any { s ->
                    val columnRange = IntRange(n.column - 1, n.column + n.length)
                    s.column in columnRange
                }
            }
        }.flatten().distinct()
    }

    fun findGears(): List<Pair<SchematicNumber, SchematicNumber>> {
        val numbers: List<SchematicNumber> = schematicRows.flatten().filterIsInstance<SchematicNumber>()
        val symbols: List<SchematicSymbol> =
            schematicRows.flatten().filterIsInstance<SchematicSymbol>().filter { it.value == '*' }

        return symbols.map { s ->
            numbers.filter { n ->
                (s.row in IntRange(n.row - 1, n.row + 1)) && (s.column in IntRange(n.column - 1, n.column + n.length))
            }
        }
            .filter { it.size == 2 }
            .map { schematicNumbers ->
                schematicNumbers[0] to schematicNumbers[1]
            }
    }

}

fun main(args: Array<String>) {

    fun part1() {
        println("PART 1 - SAMPLE")
        val sampleSchematicStringList = ResourceUtils.getResourceAsText("/day03/sampleInput.txt").orEmpty().split("\n")
        val sampleSchematic = Schematic(sampleSchematicStringList)
        println("Sum: " + sampleSchematic.findParts().sumOf {
            it.number
        })

        println("\nPART 1")
        val schematicStringList = ResourceUtils.getResourceAsText("/day03/input.txt").orEmpty().split("\n")
        val schematic = Schematic(schematicStringList)
        println("Sum: " + schematic.findParts().sumOf {
            it.number
        })
    }

    fun part2() {
        println("\nPART 2 - SAMPLE")
        val sampleSchematicStringList = ResourceUtils.getResourceAsText("/day03/sampleInput.txt").orEmpty().split("\n")
        val sampleSchematic = Schematic(sampleSchematicStringList)
        val sampleSum = sampleSchematic.findGears().sumOf { it.first.number * it.second.number }
        println("Sum: " + sampleSum)

        println("\nPART 2")
        val schematicStringList = ResourceUtils.getResourceAsText("/day03/input.txt").orEmpty().split("\n")
        val schematic = Schematic(schematicStringList)
        val sum = schematic.findGears().sumOf { it.first.number * it.second.number }
        println("Sum: " + sum)
    }

    part1()
    part2()
}