fun main() {
    class Dice {
        var cur = 1
        fun roll(): Int {
            var res = 0
            repeat(3) {
                res += cur
                cur += 1
                if (cur > 100) cur = 1
            }
            return res
        }
    }

    fun part1(input: List<String>): Int {
        val positions = input.map { it.split(": ")[1].toInt() }.toIntArray()
        val scores = arrayOf(0, 0)
        var turn = 0
        val dice = Dice()
        while (scores.all { it < 1000 }) {
            val step = dice.roll()
            positions[turn % 2] = positions[turn % 2].minus(1).plus(step).rem(10).plus(1)
            scores[turn % 2] += positions[turn % 2]
            turn++
        }
        return scores.minOrNull()!! * turn * 3
    }

    fun part2(input: List<String>): Long {
        val positions = input.map { it.split(": ")[1].toInt() }.toIntArray()
        val dims = (1..3).flatMap { a ->
            (1..3).flatMap { b ->
                (1..3).map { c ->
                    a + b + c
                }
            }
        }.groupingBy { it }.eachCount()
        val dp = mutableMapOf<List<Int>, LongArray>()

        fun dfs(depth: Int, positions: IntArray, scores: IntArray): LongArray {
            val key = listOf(depth % 2, positions[0], positions[1], scores[0], scores[1])
            if (key in dp) {
                return dp.getValue(key)
            }
            if (scores.any { it >= 21 }) {
                return scores.map { if (it >= 21) 1L else 0L }.toLongArray()
            }
            return dims.map { (step, count) ->
                val npos = positions.mapIndexed { index, pos ->
                    if (depth % 2 == index) pos.plus(step).rem(10) else pos
                }.toIntArray()
                val nscore = scores.mapIndexed { index, score ->
                    if (depth % 2 == index) score + npos[index] + 1 else score
                }.toIntArray()
                val (count0, count1) = dfs(depth + 1, npos, nscore)
                longArrayOf(
                    count0 * count,
                    count1 * count
                )
            }.reduce { (acc0, acc1), (count0, count1) ->
                longArrayOf(
                    acc0 + count0,
                    acc1 + count1,
                )
            }.apply {
                dp[key] = this
            }
        }

        return dfs(0, positions.map { it - 1 }.toIntArray(), intArrayOf(0, 0)).maxOrNull() ?: 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739_785)
    check(part2(testInput) == 444_356_092_776_315L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
