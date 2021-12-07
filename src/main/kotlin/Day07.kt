import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input.first().split(",").map(String::toInt)
        val pMin = positions.minOrNull() ?: 0
        val pMax = positions.maxOrNull() ?: 0
        var min = Int.MAX_VALUE
        for (p in pMin..pMax) {
            val cost = positions.map { abs(it - p) }.sum()
            if (cost < min) {
                min = cost
            }
        }
        return min
    }

    fun part2(input: List<String>): Int {
        val positions = input.first().split(",").map(String::toInt)
        val pMin = positions.minOrNull() ?: 0
        val pMax = positions.maxOrNull() ?: 0
        var min = Int.MAX_VALUE
        for (p in pMin..pMax) {
            val cost = positions.map {
                val d = abs(it - p)
                (d + 1) * d / 2
            }.sum()
            if (cost < min) {
                min = cost
            }
        }
        return min
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
