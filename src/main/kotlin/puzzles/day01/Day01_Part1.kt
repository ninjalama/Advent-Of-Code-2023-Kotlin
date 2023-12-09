package puzzles.day01

import utils.ResourceUtils

fun main(args: Array<String>) {
    fun getCalibrationValue(s: String): Int {
        val digitValues = s.filter {
            it.isDigit()
        }
        return (digitValues.take(1) + digitValues.takeLast(1)).toInt()
    }

    fun calculateCalibrationValue(calibrationDocument: CalibrationDocument): Int {
        return calibrationDocument.getCalibrationLines().map {
            getCalibrationValue(it)
        }.sum()
    }

    // Sample puzzle w/assertion it works
    val sampleInput = CalibrationDocument("1abc2\npqr3stu8vwx\na1b2c3d4e5f\ntreb7uchet")
    val sampleExpectedSum = 142
    assert(calculateCalibrationValue(sampleInput) == sampleExpectedSum)

    // Actual puzzle
    val calibrationDocument =
        CalibrationDocument(ResourceUtils.getResourceAsText("/day01/calibrationDocument.txt").orEmpty())

    print("Calibration value: ${calculateCalibrationValue(calibrationDocument)}")
}