fun main() {
    data class Node(val key: String) {
        var link: MutableSet<Node> = mutableSetOf()
    }

    fun parse(input: List<String>): Node {
        val nodeMap = input.flatMap {
            it.split("-")
        }.distinct().associateWith {
            Node(it)
        }
        input.forEach {
            val (n1, n2) = it.split("-").let { (k1, k2) ->
                nodeMap.getValue(k1) to nodeMap.getValue(k2)
            }
            n1.link.add(n2)
            n2.link.add(n1)
        }
        return nodeMap.getValue("start")
    }

    fun dfs(node: Node, visited: Set<String>, routes: Set<String>, strict: Boolean = true): Set<String> {
        if (node.key == "end") return routes
        if (node.key in visited && strict) return emptySet()
        val s = strict || node.key in visited
        val v = if (node.key == node.key.lowercase()) {
            visited + node.key
        } else {
            visited
        }
        val neighbors = node.link.filter { it.key != "start" }
        return neighbors.flatMap { neighbor ->
            dfs(neighbor, v, routes.map { "$it,${neighbor.key}" }.toSet(), s)
        }.toSet()
    }

    fun part1(input: List<String>): Int {
        return dfs(parse(input), emptySet(), setOf("start")).size
    }

    fun part2(input: List<String>): Int {
        return dfs(parse(input), emptySet(), setOf("start"), strict = false).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    val testInput2 = readInput("Day12_test2")
    check(part1(testInput2) == 19)
    val testInput3 = readInput("Day12_test3")
    check(part1(testInput3) == 226)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
