fun main() {

    fun part1(input: List<String>): Int {
        val commandSumMap = input.map {
            it.split(" ").let { (c, v) ->
                c to v.toInt()
            }
        }.groupingBy {
            it.first
        }.fold(0) { acc, elem ->
            acc + elem.second
        }
        return commandSumMap.getOrDefault("forward", 0) * (
                commandSumMap.getOrDefault("down", 0)
                        - commandSumMap.getOrDefault("up", 0))
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0
        input.forEach {
            val (c, v) = it.split(" ").let { (c, v) -> c to v.toInt() }
            when (c) {
                "down" -> aim += v
                "up" -> aim -= v
                "forward" -> {
                    horizontal += v
                    depth += aim * v
                }
            }
        }
        return horizontal * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
