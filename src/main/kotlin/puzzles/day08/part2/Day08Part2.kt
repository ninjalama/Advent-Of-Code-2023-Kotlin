package puzzles.day08.part2

import utils.ResourceUtils


data class Node(val valueA: String, val valueB: String)

data class Network(val map: String) {
    val instructions: String
    val nodes: Map<String, Node>

    val firstNodeString: String

    init {
        // Bit hackish init... TODO: FIX
        val lines = map.split("\n")
        instructions = lines[0]
        firstNodeString = lines[2].split(" ")[0]
        nodes = lines.drop(2).map { networkLine ->
            val (nodeValue, nodePair) = networkLine.replace(" ", "").split("=")
            val (nodeA, nodeB) = nodePair.drop(1).dropLast(1).split(",")
            nodeValue to Node(nodeA, nodeB)
        }.associate { it.first to it.second }
    }

    tailrec fun findFirstEndingInZ(counter: Long = 0, nodeStr: String): Long {
        // if (counter % 10000000L == 0L) println("Counter: " + counter)
        val instruction = instructions[(counter % instructions.length).toInt()]

        val node = nodes.getOrDefault(nodeStr, Node("", ""))
        val nextNodeString = if (instruction == 'L') node.valueA else node.valueB

        return if (nodeStr.endsWith("Z")) {
            counter
        } else {
            findFirstEndingInZ(counter + 1, nextNodeString)
        }
    }
}

fun main(args: Array<String>) {

    /*
    So, it turns out the solution to part 2 is an LCM-solution...
    afer brute-forcing for 30 minutes, it turns out the number of
    executed cycles was only 0.09% of the required cycles. Fun.
     */
    fun calculateGCD(a: Long, b: Long): Long {
        return if (b == 0L) a else calculateGCD(b, a % b)
    }

    fun calculateLCM(a: Long, b: Long): Long {
        return (a * b) / calculateGCD(a, b)
    }

    fun calculateLCMOfList(numbers: List<Long>): Long {
        if (numbers.isEmpty()) return 0L

        var lcm = numbers[0]
        for (i in 1 until numbers.size) {
            lcm = calculateLCM(lcm, numbers[i])
        }
        return lcm
    }

    fun part2() {
        //val input = ResourceUtils.getResourceAsText("/day08/sampleInput1.txt").orEmpty()
        //val input = ResourceUtils.getResourceAsText("/day08/sampleInput2.txt").orEmpty()
        // val input = ResourceUtils.getResourceAsText("/day08/sampleInputPart2.txt").orEmpty()
        val input = ResourceUtils.getResourceAsText("/day08/input.txt").orEmpty()
        val network = Network(input)
        val nodeStringsEndingInA = network.nodes.filterKeys { it.endsWith("A") }.map { it.key }

        val numbers = nodeStringsEndingInA.map {
            network.findFirstEndingInZ(0, it)
        }

        val number = calculateLCMOfList(numbers)
        println("Number: " + number)
    }


    part2()
}