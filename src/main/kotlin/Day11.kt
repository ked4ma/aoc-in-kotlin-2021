fun main() {
    fun step(octopus: Array<Array<Int>>) {
        // check 9s
        val overNines = ArrayDeque<Pair<Int, Int>>()
        val visited = mutableSetOf<Pair<Int, Int>>()
        (0 until 10).forEach { i ->
            (0 until 10).forEach { j ->
                if (octopus[i][j] == 9) {
                    overNines.add(i to j)
                }
            }
        }
        while (overNines.isNotEmpty()) {
            val (i, j) = overNines.removeFirst()
            if (i to j in visited) continue
            visited.add(i to j)
            listOf(
                i - 1 to j,
                i + 1 to j,
                i to j - 1,
                i to j + 1,
                i - 1 to j - 1,
                i - 1 to j + 1,
                i + 1 to j - 1,
                i + 1 to j + 1
            ).filter { (m, n) ->
                m in 0 until 10 && n in 0 until 10
            }.forEach { (m, n) ->
                if (++octopus[m][n] >= 9) {
                    overNines.add(m to n)
                }
            }
        }

        // increment
        (0 until 10).forEach { i ->
            (0 until 10).forEach { j ->
                if (++octopus[i][j] > 9) {
                    octopus[i][j] = 0
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val octopus = input.map { row -> row.toCharArray().map { it - '0' }.toTypedArray() }.toTypedArray()
        var flashCount = 0
        repeat(100) {
            step(octopus)
            (0 until 10).forEach { i ->
                (0 until 10).forEach { j ->
                    if (octopus[i][j] == 0) {
                        flashCount++
                    }
                }
            }
        }
        return flashCount
    }

    fun part2(input: List<String>): Int {
        val octopus = input.map { row -> row.toCharArray().map { it - '0' }.toTypedArray() }.toTypedArray()
        var loop = 0
        while (true) {
            step(octopus)
            loop++

            if (octopus.sumOf { it.sum() } == 0) {
                break
            }
        }
        return loop
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
