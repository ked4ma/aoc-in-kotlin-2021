import java.util.ArrayDeque
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun solve(input: List<String>, target: String): Int {
        fun toKey(d: Array<CharArray>) =
            d.flatMap { row -> row.filter { it !in arrayOf('#', ' ') } }.joinToString(separator = "")

        val init = input.map { it.toCharArray() }.toTypedArray()
        val memo = mutableMapOf<String, Int>()
        val queue = ArrayDeque<Pair<String, Int>>()
        queue.add(init.joinToString(",", transform = CharArray::concatToString) to 0)

        while (queue.isNotEmpty()) {
            val (data, score) = queue.removeFirst().let { (data, score) ->
                data.split(",").map { it.toCharArray() }.toTypedArray() to score
            }
            val key = toKey(data)
            if (key in memo && memo.getValue(key) <= score) continue
            memo[key] = score
            for (i in data.indices) {
                for (j in data[i].indices) {
                    if (data[i][j] !in arrayOf('A', 'B', 'C', 'D')) continue
                    val ds = arrayOf(1, 10, 100, 1000)[data[i][j] - 'A']
                    if (i > 1) { // up
                        if ((1 until i).any { data[it][j] != '.' }) continue
                        if ((i until data.lastIndex).all { j == 2 * (data[it][j] - 'A') + 3 }) continue
                        listOf(1, 2, 4, 6, 8, 10, 11).forEach { b ->
                            if ((min(j, b)..max(j, b)).all { it == j || data[1][it] == '.' }) {
                                val d = data.map { it.copyOf() }.toTypedArray()
                                d[1][b] = d[i][j]
                                d[i][j] = '.'
                                queue.add(
                                    d.joinToString(
                                        ",",
                                        transform = CharArray::concatToString
                                    ) to score + ds * (i - 1 + abs(j - b))
                                )
                            }
                        }
                    } else { // i == 1
                        // down
                        val bDest = 2 * (data[i][j] - 'A') + 3
                        if ((min(j, bDest)..max(j, bDest)).any { it != j && data[i][it] != '.' }) continue
                        var a = i
                        while (data[a + 1][bDest] == '.') {
                            a++
                        }
                        if (a > i && ((a + 1) until data.lastIndex).all { bDest == 2 * (data[it][bDest] - 'A') + 3 }) {
                            val d = data.map { it.copyOf() }.toTypedArray()
                            d[a][bDest] = d[i][j]
                            d[i][j] = '.'
                            queue.add(
                                d.joinToString(
                                    ",",
                                    transform = CharArray::concatToString
                                ) to (score + ds * (a - i + abs(bDest - j)))
                            )
                        }
                    }
                }
            }
        }
        return memo.getValue(target)
    }

    fun part1(input: List<String>): Int {
        return solve(input, "...........ABCDABCD")
    }

    fun part2(input: List<String>): Int {
        return input.toMutableList().apply {
            addAll(
                3, listOf(
                    "  #D#C#B#A#",
                    "  #D#B#A#C#"
                )
            )
            forEach(::println)
        }.toList().let {
            solve(it, "...........ABCDABCDABCDABCD").apply { println("score: $this") }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 12_521)
    check(part2(testInput) == 44_169)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
