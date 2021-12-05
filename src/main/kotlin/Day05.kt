import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    fun isDirect() = x1 == x2 || y1 == y2

    /**
     * Return points of vertical/horizontal lines
     */
    fun directPoints(): List<Pair<Int, Int>> {
        if (!isDirect()) return emptyList()

        return if (x1 == x2) {
            (min(y1, y2)..max(y1, y2)).map {
                x1 to it
            }
        } else {
            (min(x1, x2)..max(x1, x2)).map {
                it to y1
            }
        }
    }

    /**
     * Return points of vertical/horizontal/diagonal lines
     */
    fun directAndDiagonalPoints(): List<Pair<Int, Int>> = when {
        // horizontal
        x1 == x2 -> {
            (min(y1, y2)..max(y1, y2)).map {
                x1 to it
            }
        }
        // vertical
        y1 == y2 -> {
            (min(x1, x2)..max(x1, x2)).map {
                it to y1
            }
        }
        // diagonal
        abs(x1 - x2) == abs(y1 - y2) -> {
            val n = abs(x1 - x2)
            val dx = (x2 - x1) / n
            val dy = (y2 - y1) / n
            (0..n).map {
                x1 + dx * it to y1 + dy * it
            }
        }
        // else
        else -> emptyList()
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Line> {
        return input.map {
            val (x1, y1, x2, y2) = it.replace(" -> ", ",").split(",").map(String::toInt)
            Line(x1, y1, x2, y2)
        }
    }

    fun part1(input: List<String>): Int {
        val lines = parseInput(input)
        return lines
            .flatMap(Line::directPoints)
            .groupingBy { it }
            .eachCount()
            .filterValues { it >= 2 }
            .size
    }

    fun part2(input: List<String>): Int {
        val lines = parseInput(input)
        return lines
            .flatMap(Line::directAndDiagonalPoints)
            .groupingBy { it }
            .eachCount()
            .filterValues { it >= 2 }
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
