package puzzles.day01

import utils.ResourceUtils

data class DigitWord(val stringValue: String, val intValue: Int) {
    companion object {
        fun getDigitWordList() =
            listOf(
                "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
            ).mapIndexed { idx, numberStr -> DigitWord(numberStr, idx + 1) }
                .plus((1..9).map {
                    DigitWord(it.toString(), it)
                })
    }
}


fun calculateCalibrationValuePart1(document: String): Int {
    return document.split("\n").map { line ->
        // Could just filter by it.isDigit and  take first and last, and build a string out of them but...
        line.find { it.isDigit() }.toString() + line.findLast { it.isDigit() }.toString()
    }.map { it.toInt() }
        .sum()
}

fun calculateCalibrationValuePart2(document: String): Int {
    val digitWordList = DigitWord.getDigitWordList()
    return document.split("\n").map { line ->
        val firstDigitStringOccurence = digitWordList.map { digitWord ->
            val pos = line.indexOf(digitWord.stringValue)
            digitWord to if (pos != -1) pos else Int.MAX_VALUE
        }.minBy { it.second }

        val lastDigitOccurence = digitWordList.map { digitWord ->
            val pos = line.lastIndexOf(digitWord.stringValue)
            digitWord to pos
        }.maxBy { it.second }

        (firstDigitStringOccurence.first.intValue.toString() + lastDigitOccurence.first.intValue.toString()).toInt()
    }.sum()
}


fun main(args: Array<String>) {


    fun part1() {
        // Sample puzzle (Part 1) w/assertion it works
        val sampleInput = ResourceUtils.getResourceAsText("/day01/samplePart1Input.txt").orEmpty()
        val sampleExpectedSum = 142
        assert(calculateCalibrationValuePart1(sampleInput) == sampleExpectedSum)

        // Actual Part 1 puzzle
        val input = ResourceUtils.getResourceAsText("/day01/input.txt").orEmpty()
        println("Calibration value for Part 1: ${calculateCalibrationValuePart1(input)}")
    }

    fun part2() {
        // Sample puzzle (Part 2) w/assertion it works
        val sampleInput = ResourceUtils.getResourceAsText("/day01/samplePart2Input.txt").orEmpty()
        val sampleExpectedSum = 142
        assert(calculateCalibrationValuePart2(sampleInput) == sampleExpectedSum)

        val input = ResourceUtils.getResourceAsText("/day01/input.txt").orEmpty()
        println("Calibration value for Part 2: ${calculateCalibrationValuePart2(input)}")
    }

    part1()
    part2()
}