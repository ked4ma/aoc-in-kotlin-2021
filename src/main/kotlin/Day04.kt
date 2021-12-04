import kotlin.math.abs

private class Board(data: List<String>) {
    private val counts = IntArray(10)
    private val boardStat: Array<Array<Boolean>> = Array(5) { Array(5) { false } }
    private val numPositionMap: Map<Int, Pair<Int, Int>>

    init {
        numPositionMap = buildMap {
            data.forEachIndexed { i, row ->
                row.trim().split(regex = Regex("\\s+")).map(String::toInt).forEachIndexed { j, n ->
                    put(n, i to j)
                }
            }
        }
    }

    var isBingo = false
        private set

    /**
     * Set n as marked
     * @return true if board is BINGO.
     */
    fun mark(n: Int): Boolean {
        numPositionMap[n]?.let {
            if (!boardStat[it.first][it.second]) {
                boardStat[it.first][it.second] = true
                counts[it.first]++
                counts[5 + it.second]++
            }
            if (!isBingo) {
                isBingo = checkBingo()
            }
        }
        return isBingo
    }

    private fun checkBingo(): Boolean {
        return counts.contains(5)
    }

    fun remainedSum(): Int {
        return numPositionMap.filterValues { (r, c) ->
            !boardStat[r][c]
        }.keys.sum()
    }
}

fun main() {
    fun parseInput(input: List<String>): Pair<List<Int>, List<Board>> {
        val numbers = input.first().split(",").map(String::toInt)
        val boards = (2 until input.size step 6).map { index ->
            Board(input.subList(index, index + 5))
        }
        return numbers to boards
    }

    fun part1(input: List<String>): Int {
        val (numbers, boards) = parseInput(input)
        numbers.forEach { n ->
            boards.forEach {
                if (it.mark(n)) {
                    return it.remainedSum() * n
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val (numbers, boards) = parseInput(input)
        var bingoCount = 0
        numbers.forEach { n ->
            boards.forEach board@{
                if (it.isBingo) return@board
                if (it.mark(n)) {
                    bingoCount++
                }
                if (bingoCount == boards.size) {
                    return it.remainedSum() * n
                }
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
