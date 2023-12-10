package puzzles.day03.two

import utils.ResourceUtils

data class ScratchCard(val line: String) {
    val cardId: Int

    private val winningNumbers: IntArray
    private val cardNumbers: IntArray

    init {
        val (cardIdStr, cardSetStr) = line.split(":")
        val (winningNumbersStr, cardNumbersStr) = cardSetStr.split("|")

        cardId = cardIdStr.replace(" ", "").substring("Card".length).toInt()
        winningNumbers = winningNumbersStr.trim().replace("  ", " ").split(" ").map { it.toInt() }.toIntArray()
        cardNumbers = cardNumbersStr.trim().replace("  "," ").split(" ").map { it.toInt() }.toIntArray()
    }

    fun getWinningNumbers(): Set<Int> {
        return cardNumbers.toSet().intersect(winningNumbers.toSet())
    }

    companion object {
        fun getPoints(nrOfWinningNumbers: Int): Int {
            return when {
                nrOfWinningNumbers <= 0 -> 0
                else -> (0 until nrOfWinningNumbers).fold(0) { accumulator, number ->
                    if (number == 0) accumulator + 1 else accumulator * 2
                }
            }
        }
    }
}



fun main(args: Array<String>) {

    fun part1() {
        println("PART 1 - SAMPLE")
        val sampleStringList: List<String> = ResourceUtils.getResourceAsText("/day04/sampleInput.txt").orEmpty().split("\n")
        val sampleScratchCards = sampleStringList.map(::ScratchCard)

        val samplePointsSum = sampleScratchCards.sumOf { scratchCard ->
            ScratchCard.getPoints(scratchCard.getWinningNumbers().count())
        }
        println("Part 1 - Sample sum: " + samplePointsSum)


        println("PART 1")
        val stringList: List<String> = ResourceUtils.getResourceAsText("/day04/input.txt").orEmpty().split("\n")
        val scratchCards = stringList.map(::ScratchCard)

        val pointsSum = scratchCards.sumOf { scratchCard ->
            ScratchCard.getPoints(scratchCard.getWinningNumbers().count())
        }
        println("Part 1 - Sample sum: " + pointsSum)
    }

    part1()
}