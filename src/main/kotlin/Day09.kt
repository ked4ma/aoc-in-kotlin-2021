import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    fun lowestPoints(input: List<List<Int>>): List<Pair<Int, Int>> {
        val iRange = input.indices
        val jRange = input[0].indices
        return input.flatMapIndexed { i, row ->
            row.mapIndexed { j, n ->
                val hasLowerOrEqual = listOf(
                    i - 1 to j,
                    i + 1 to j,
                    i to j - 1,
                    i to j + 1
                ).filter { (i, j) ->
                    i in iRange && j in jRange
                }.map { (i, j) ->
                    input[i][j]
                }.any {
                    it <= n
                }
                if (hasLowerOrEqual) null else i to j
            }
        }.filterNotNull()
    }

    fun part1(input: List<String>): Int {
        val data = input.map { line ->
            line.toCharArray().map { it - '0' }
        }
        return lowestPoints(data).sumOf { (i, j) ->
            data[i][j] + 1
        }
    }

    fun part2(input: List<String>): Int {
        val data = input.map { line ->
            line.toCharArray().map { it - '0' }
        }
        val basinMap = data.indices.map {
            Array(size = data[0].size) { -1 }
        }.toTypedArray()
        val queue = ArrayDeque<Pair<Int, Int>>()

        val iRange = data.indices
        val jRange = data[0].indices
        lowestPoints(data).forEachIndexed { i, it ->
            queue.add(it)
            while (queue.isNotEmpty()) {
                val (y, x) = queue.removeFirst()
                if (basinMap[y][x] >= 0) continue
                basinMap[y][x] = i
                queue.addAll(listOf(
                    y - 1 to x,
                    y + 1 to x,
                    y to x - 1,
                    y to x + 1
                ).filter { (y, x) ->
                    y in iRange && x in jRange && data[y][x] < 9
                })
            }
        }

        return basinMap.flatten().filterNot {
            it < 0
        }.groupingBy { it }.eachCount().values.sortedDescending().take(3).reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
