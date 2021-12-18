import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.sqrt

fun main() {
    fun part1(input: String): Int {
        val (x1, x2, y1, y2) = Regex(".+?=(.+?)\\.\\.(.+?), .+?=(.+?)\\.\\.(.+)$").find(input)?.groupValues?.takeLast(4)
            ?.map(String::toInt)
            ?: return 0
        val y0 = abs(y1) - 1
        return y0 * (y0 + 1) / 2
    }

    fun part2(input: String): Int {
        val (x1, x2, y1, y2) = Regex(".+?=(.+?)\\.\\.(.+?), .+?=(.+?)\\.\\.(.+)$").find(input)?.groupValues?.takeLast(4)
            ?.map(String::toInt)
            ?: return 0
        val minX0 = ceil((-1 + sqrt((1 + 8 * x1).toDouble())).div(2)).toInt()
        var count = 0
        for (dx in minX0..x2) {
            for (dy in y1 until -y1) {
                var vx = dx
                var vy = dy
                var x = 0
                var y = 0
                while (x < x2 && y > y1) {
                    x += vx
                    vx = max(0, vx - 1)
                    y += vy--
                    if (x in x1..x2 && y in y1..y2) {
                        count++
                        break
                    }
                }
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput.first()) == 45)
    check(part2(testInput.first()) == 112)

    val input = readInput("Day17")
    println(part1(input.first()))
    println(part2(input.first()))
}
