package puzzles.day08.part1

import utils.ResourceUtils


data class Node(val valueA: String, val valueB: String)

data class Network(val map: String) {
    val instructions: String
    val nodes: Map<String, Node>

    val firstNodeString: String
    
    init {
        val lines = map.split("\n")
        instructions = lines[0]
        firstNodeString = lines[2].split(" ")[0]
        nodes = lines.drop(2).map { networkLine ->
            val (nodeValue, nodePair) = networkLine.replace(" ", "").split("=")
            val (nodeA, nodeB) = nodePair.drop(1).dropLast(1).split(",")
            nodeValue to Node(nodeA, nodeB)
        }.associate { it.first to it.second }
    }
    
    tailrec fun findZZZ(counter: Long = 0, nodeStr: String?) {
        if (counter % 10000000L == 0L) println("Counter: " + counter)
        val instruction = instructions[(counter % instructions.length).toInt()]
        
        val node = nodes.get(nodeStr)
        val nextNodeString = if (instruction == 'L') node?.valueA else node?.valueB
        
        if (nodeStr == "ZZZ") {
            println("Found ZZZ in $counter steps")
        } else {
            findZZZ(counter+1, nextNodeString)
        }
    }
}

fun main(args: Array<String>) {

    fun part1() {
        //val input = ResourceUtils.getResourceAsText("/day08/sampleInput1.txt").orEmpty()
        //val input = ResourceUtils.getResourceAsText("/day08/sampleInput2.txt").orEmpty()
        val input = ResourceUtils.getResourceAsText("/day08/input.txt").orEmpty()
        
        val lines = input.split("\n")
        
        val network = Network(input)
        
        println("Finding node - using first node: " + network.firstNodeString)
        network.findZZZ(0, "AAA")
        println("First nodestring foun")
    }
    
    
    part1()
}