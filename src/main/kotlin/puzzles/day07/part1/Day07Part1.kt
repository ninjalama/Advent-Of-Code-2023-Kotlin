package puzzles.day07

import utils.ResourceUtils

data class HandCard(val card: Char, val count: Int)

data class Card(val card: Char) {
    val rank: Int

    init {
        rank = when (card) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> {
                card.digitToInt()
            }
        }
    }
}

enum class HandType(val rank: Int) {
    FiveOfAKind(7),
    FourOfAKind(6),
    FullHouse(5),
    ThreeOfAKind(4),
    TwoPair(3),
    OnePair(2),
    HighCard(1)
}

data class Hand(val cards: List<Card>, val bid: Int) {
    val handType: HandType
    val handCards: List<HandCard>

    init {
        handCards = cards.groupingBy { it.card }.eachCount().map {
            HandCard(it.key, it.value)
        }

        handType = when {
            handCards.any { it.count == 5 } -> HandType.FiveOfAKind
            handCards.any { it.count == 4 } -> HandType.FourOfAKind
            handCards.any { it.count == 3 } && handCards.any { it.count == 2 } -> HandType.FullHouse
            handCards.any { it.count == 3 } && handCards.count { it.count == 1 } == 2 -> HandType.ThreeOfAKind
            handCards.count { it.count == 2 } == 2 -> HandType.TwoPair
            handCards.any { it.count == 2 } -> HandType.OnePair
            else -> HandType.HighCard
        }
    }

    fun compare(other: Hand): Int {
        return when {
            handType.rank < other.handType.rank -> -1
            handType.rank > other.handType.rank -> 1
            else -> {
                // Compare 1st card - if same compare 2nd card - if same compare 3rd card..
                val indexOfFirstNotEqualCard = cards.zip(other.cards).indexOfFirst { (card1, card2) ->
                    card1.rank != card2.rank
                }
                if (cards[indexOfFirstNotEqualCard].rank > other.cards[indexOfFirstNotEqualCard].rank) 1 else -1
            }
        }
    }

    companion object {
        fun compareHands(hand1: Hand, hand2: Hand): Int {
            return hand1.compare(hand2)
        }
    }
}

fun main(args: Array<String>) {

    fun part1() {
        // val input = ResourceUtils.getResourceAsText("/day07/sampleInput.txt").orEmpty()
        val input = ResourceUtils.getResourceAsText("/day07/input.txt").orEmpty()
        val sum = input.split("\n").map { handLine ->
            val (handStr, bid) = handLine.split(" ")
            Hand(
                handStr.map {
                    Card(it)
                }, bid.toInt()
            )
        }.sortedWith(Comparator { hand1, hand2 ->
            Hand.compareHands(hand1, hand2)
        }).withIndex().sumBy { (index, hand) ->
            (index + 1) * hand.bid
        }

        println("Sum: " + sum)
    }

    part1()
}