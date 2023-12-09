package puzzles.day01

import utils.ResourceUtils

data class DigitString(val digitString: String, val value: Int, val position: Int? = null)

fun main(args: Array<String>) {

    val digitStrings: List<DigitString> = listOf<DigitString>(
        DigitString("one", 1),
        DigitString("two", 2),
        DigitString("three", 3),
        DigitString("four", 4),
        DigitString("five", 5),
        DigitString("six", 6),
        DigitString("seven", 7),
        DigitString("eight", 8),
        DigitString("nine", 9)
    ).plus((1..9).map {
        DigitString(it.toString(), it)
    })

    fun getCalibrationValue(input: String): Int {
        val firstDigitStringOccurence: DigitString = digitStrings.map {
            val pos = input.indexOf(it.digitString)
            DigitString(it.digitString, it.value, if (pos > -1) pos else null)
        }.minBy {
            it.position ?: Int.MAX_VALUE
        }

        val newInput = input.substring(firstDigitStringOccurence.position ?: 0)
        val lastDigitStringOccurrence: DigitString = digitStrings.map {
            val pos = newInput.lastIndexOf(it.digitString)
            DigitString(it.digitString, it.value, if (pos > -1) pos else null)
        }.maxBy {
            it.position ?: Int.MIN_VALUE
        }

        return (firstDigitStringOccurence.value.toString() + lastDigitStringOccurrence.value.toString()).toInt()
    }

    fun calculateCalibrationValue(calibrationDocument: CalibrationDocument): Int {
        return calibrationDocument.getCalibrationLines().map {
            getCalibrationValue(it)
        }.sum()
    }

    // Sample puzzle w/assertion that it works
    val sampleInput =
        CalibrationDocument(
            "two1nine\neightwothree\nabcone2threexyz\nxtwone3four\n4nineeightseven2\nzoneight234\n7pqrstsixteen"
        )

    val sampleExpectedSum = 281
    assert(calculateCalibrationValue(sampleInput) == sampleExpectedSum)

    // Actual puzzle
    val calibrationDocument =
        CalibrationDocument(ResourceUtils.getResourceAsText("/day01/calibrationDocument.txt").orEmpty())

    print("Calibration value: ${calculateCalibrationValue(calibrationDocument)}")
}