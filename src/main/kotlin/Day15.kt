fun main() {
    // dijkstra
    fun solve(data: Array<Array<Int>>): Int {
        val risk = Array(data.size) {
            Array(data[0].size) { Int.MAX_VALUE }
        }
        risk[0][0] = 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        val cand = mutableSetOf(0 to 0)
        while (visited.size < data.size * data[0].size) {
            var m = Int.MAX_VALUE
            var p = 0 to 0
            cand.forEach { (x, y) ->
                if (risk[y][x] < m) {
                    m = risk[y][x]
                    p = x to y
                }
            }
            if (m == Int.MAX_VALUE) break
            visited.add(p)
            cand.remove(p)
            // update risk
            val (x, y) = p
            listOf(
                p.first to p.second - 1,
                p.first to p.second + 1,
                p.first - 1 to p.second,
                p.first + 1 to p.second
            ).filter { (i, j) ->
                i in data[0].indices && j in data.indices
            }.filter {
                it !in visited
            }.forEach { (i, j) ->
                risk[j][i] = minOf(
                    risk[j][i],
                    risk[y][x] + data[j][i]
                )
                cand.add(i to j)
            }
        }
        return risk.last().last()
    }

    fun part1(input: List<String>): Int {
        val data = input.map { line ->
            line.toCharArray().map { it - '0' }.toTypedArray()
        }.toTypedArray()
        return solve(data)
    }

    fun part2(input: List<String>): Int {
        val origin = input.map { line ->
            line.toCharArray().map { it - '0' }.toTypedArray()
        }.toTypedArray()
        val data = Array(origin.size * 5) {
            Array(origin[0].size * 5) {
                0
            }
        }
        for (i in data.indices) {
            for (j in data[0].indices) {
                data[i][j] = origin[i % origin.size][j % origin[0].size]
                    .plus(i / origin.size)
                    .plus(j / origin[0].size)
                    .let {
                        if (it >= 10) it % 10 + 1 else it
                    }
            }
        }
        return solve(data)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
