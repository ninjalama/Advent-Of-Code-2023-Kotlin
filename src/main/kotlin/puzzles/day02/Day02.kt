package puzzles.day02

import utils.ResourceUtils
import kotlin.text.Regex

data class Cube(val numberOfCubes: Int, val color: Color)

enum class Color(s: String) {
    RED("red"), BLUE("blue"), GREEN("green")
}


data class Game(val gameLine: String) {
    val gameSets: List<GameSet>
    val gameId: Int

    init {
        val (gameIdStr, gameSetsStr) = gameLine.split(":")
        gameId = gameIdStr.substring("Game ".length).toInt()
        gameSets = gameSetsStr.split(";").map(::GameSet)
    }
}

data class GameSet(val gameSet: String) {
    val cubes = gameSet.trim().split(",").map {
        val number = it.trim().takeWhile {
            it.isDigit()
        }.toInt()
        val color = it.trim().takeLastWhile { !it.isDigit() }
        Cube(number, enumValueOf<Color>(color.trim().uppercase()))
    }

    fun nrOfCubes(color: Color) = cubes.filter { it.color == color }.sumOf { it.numberOfCubes }
}


fun main(args: Array<String>) {

    val sampleGame: List<String> = listOf<String>(
        "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
        "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
        "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
        "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
        "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
    )
    val sampleGames = sampleGame.map(::Game)


    fun part1() {
        fun findPossibleGames(games: List<Game>, redCubes: Int, greenCubes: Int, blueCubes: Int): List<Game> {
            return games.filter {
                it.gameSets.filter {
                    it.nrOfCubes(Color.RED) <= redCubes && it.nrOfCubes(Color.BLUE) <= blueCubes && it.nrOfCubes(Color.GREEN) <= greenCubes
                }.size == it.gameSets.size
            }
        }

        // SAMPLE
        println("PART1 SAMPLE")
        val possibleSampleGames = findPossibleGames(sampleGames, 12, 13, 14)
        println("Possible games: " + possibleSampleGames.map { it.gameId }.sorted())
        println("Sum: " + possibleSampleGames.sumOf { it.gameId })

        println("\nPART 1")
        val games: List<Game> = ResourceUtils.getResourceAsText("/day02/games.txt").orEmpty().split("\n").map(::Game)
        val possibleGames = findPossibleGames(games, 12, 13, 14)
        println("Possible games: " + possibleGames.map { it.gameId }.sorted())
        println("Sum: " + possibleGames.sumOf { it.gameId })
    }

    fun part2() {
        fun getSumOfPower(games: List<Game>): Int {
            return games.map {
                // Find minimum number of blue cubes present
                it.gameSets.maxOf { it.nrOfCubes(Color.BLUE) } *
                        it.gameSets.maxOf { it.nrOfCubes(Color.RED) } *
                        it.gameSets.maxOf { it.nrOfCubes(Color.GREEN) }
            }.sum()
        }

        println("\nPART 2 - SAMPLE")
        println("Sum of power for sample games: " + getSumOfPower(sampleGames))
        println("\nPART 2")

        val games: List<Game> = ResourceUtils.getResourceAsText("/day02/games.txt").orEmpty().split("\n").map(::Game)
        println("Sum of power for part 2: " + getSumOfPower(games))
    }

    part1()
    part2()
}