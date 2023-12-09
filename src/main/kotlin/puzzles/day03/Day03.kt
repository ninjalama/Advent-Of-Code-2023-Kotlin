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
}

fun main(args: Array<String>) {
    fun sample() {
        println("SAMPLE")
        val sampleSchematicStringList = ResourceUtils.getResourceAsText("/day03/sampleInput.txt").orEmpty().split("\n")
        val schematic = Schematic(sampleSchematicStringList)
        println("Sum: " + schematic.findParts().sumOf {
                it.number
            })
    }

    fun part1() {
        println("\nPART 1")
        val schematicStringList = ResourceUtils.getResourceAsText("/day03/input.txt").orEmpty().split("\n")
        val schematic = Schematic(schematicStringList)
        println("Sum: " + schematic.findParts().sumOf {
                it.number
            })
    }

    sample()
    part1()
}