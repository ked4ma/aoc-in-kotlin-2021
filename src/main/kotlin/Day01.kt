fun main() {
    fun solve(input: List<Long>): Int {
        var count = 0
        var current = Long.MAX_VALUE
        input.forEach {
            if (it > current) {
                count++
            }
            current = it
        }
        return count
    }

    fun part1(input: List<String>): Int {
        return solve(input.map(String::toLong))
    }

    fun part2(input: List<String>): Int {
        if (input.size < 3) return 0
        return input.map(String::toLong).windowed(
            size = 3,
            step = 1,
            partialWindows = false,
            transform = List<Long>::sum
        ).let(::solve)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
