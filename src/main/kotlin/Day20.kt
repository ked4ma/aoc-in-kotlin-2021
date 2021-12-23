fun main() {
    fun parse(input: List<String>): Pair<IntArray, Array<Array<Int>>> {
        val splitIndex = input.indexOfFirst(String::isEmpty)
        val convArr = input.subList(0, splitIndex).joinToString("").toCharArray().map {
            if (it == '#') 1 else 0
        }.toIntArray()
        val arr = input.subList(splitIndex + 1, input.size).map { line ->
            line.toCharArray().map {
                if (it == '#') 1 else 0
            }.toTypedArray()
        }.toTypedArray()

        return convArr to arr
    }

    fun step(convArr: IntArray, input: Array<Array<Int>>, default: Int = 0): Pair<Array<Array<Int>>, Int> {
        val res = Array(size = input.size + 2) { Array(size = input[0].size + 2) { 0 } }
        (res.indices).forEach { i ->
            (res[0].indices).forEach { j ->
                var v = 0
                (i - 1..i + 1).forEach { k ->
                    (j - 1..j + 1).forEach { l ->
                        v = (v shl 1) or (input.getOrNull(k - 1)?.getOrNull(l - 1) ?: default)
                    }
                }
                res[i][j] = convArr[v]
            }
        }
        return res to if (default == 0) convArr.first() else convArr.last()
    }

    fun solve(convArr: IntArray, input: Array<Array<Int>>, rep: Int = 2): Int {
        var default = 0
        val res = (0 until rep).fold(input) { acc, _ ->
            val (res, d) = step(convArr, acc, default)
            default = d
            res
        }
        return res.sumOf { row -> row.count { it == 1 } }
    }

    fun part1(input: List<String>): Int {
        val (convArr, arr) = parse(input)
        return solve(convArr, arr, 2)
    }

    fun part2(input: List<String>): Int {
        val (convArr, arr) = parse(input)
        return solve(convArr, arr, 50)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
