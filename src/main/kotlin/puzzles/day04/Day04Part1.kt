package puzzles.day04

import utils.ResourceUtils

object Day04Part1 {

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
    
    fun part1Calc(input: List<String>): Int {
        val scratchCards = input.map { it -> Day04Part1.ScratchCard(it) }
        return scratchCards.map { sCard ->
            val ourWinNumbersCount = sCard.winningNumbers.count { it in sCard.cardNumbers }
            (0 until ourWinNumbersCount).fold(0) { acc, number ->
                if (number == 0) 1 else acc * 2
            }
        }.onEach(::println)
            .sum()
            .also {
                println("Sum: " + it)
            }
    }
    
    fun part1() {
        println("PART 1 - SAMPLE")
        val sampleStringList: List<String> = ResourceUtils.getResourceAsText("/day04/sampleInput.txt").orEmpty().split("\n")
        part1Calc(sampleStringList)
        
        println("PART 1")
        val stringList: List<String> = ResourceUtils.getResourceAsText("/day04/input.txt").orEmpty().split("\n")
        part1Calc(stringList)
    }
    
    part1()
}