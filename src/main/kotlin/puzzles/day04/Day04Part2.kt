package puzzles.day04

import utils.ResourceUtils

object Day04Part2 {

    data class ScratchCard(val line: String) {
        val cardId: Int

        val winningNumbers: List<Int>
        val cardNumbers: List<Int>

        init {
            val (cardIdStr, cardSetStr) = line.split(":")
            val (winningNumbersStr, cardNumbersStr) = cardSetStr.split(" | ")

            cardId = cardIdStr.replace(" ", "").substring("Card".length).toInt()
            winningNumbers = winningNumbersStr.chunked(3).map { it.trim().toInt() }
            cardNumbers = cardNumbersStr.chunked(3).map { it.trim().toInt() }
        }
    }
}

fun main(args: Array<String>) {

    fun part2Calc(input: List<String>) {
        val scratchCards = input.map { it -> Day04Part1.ScratchCard(it) }
        scratchCards.map { sCard ->
            val ourWinNumbersCount = sCard.winningNumbers.count { it in sCard.cardNumbers }
            sCard to ourWinNumbersCount
        }
            .let { p ->
                val numberOfcardsList = MutableList(p.size) { 1 }
                //(1 to it.pai)
                p.mapIndexed { idx, (sCard, nrOfWonCards) ->
                    (1..nrOfWonCards).forEach {
                        numberOfcardsList[idx + it] += numberOfcardsList[idx]
                    }
                }
                numberOfcardsList
            }.sum()
            .also {
                println("Sum: " + it)
            }
    }

    fun part2() {
        println("PART 2 - SAMPLE")
        val sampleStringList: List<String> =
            ResourceUtils.getResourceAsText("/day04/sampleInput.txt").orEmpty().split("\n")
        part2Calc(sampleStringList)


        println("\nPART 2")
        val stringList: List<String> = ResourceUtils.getResourceAsText("/day04/input.txt").orEmpty().split("\n")
        part2Calc(stringList)
    }

    part2()
}