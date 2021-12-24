import kotlin.math.max
import kotlin.math.min

fun main() {
    fun parse(input: String): Pair<Triple<IntRange, IntRange, IntRange>, Boolean> {
        return Regex("""^(on|off) x=(.+?)\.\.(.+?),y=(.+?)\.\.(.+?),z=(.+?)\.\.(.+?)$""").find(input)!!.groupValues.let {
            val mode = it[1] == "on"
            val xRange = it[2].toInt()..it[3].toInt()
            val yRange = it[4].toInt()..it[5].toInt()
            val zRange = it[6].toInt()..it[7].toInt()
            Triple(xRange, yRange, zRange) to mode
        }
    }

    fun part1(input: List<String>): Int {
        val cube = Array(101) {
            Array(101) {
                Array(101) { false }
            }
        }
        var count = 0
        input.map(::parse).map { (cor, mode) ->
            val xRange = max(cor.first.first, -50)..min(cor.first.last, 50)
            val yRange = max(cor.second.first, -50)..min(cor.second.last, 50)
            val zRange = max(cor.third.first, -50)..min(cor.third.last, 50)
            Triple(xRange, yRange, zRange) to mode
        }.forEach { (tr, mode) ->
            tr.first.forEach { x ->
                tr.second.forEach { y ->
                    tr.third.forEach { z ->
                        if (cube[x + 50][y + 50][z + 50] != mode) {
                            count += if (mode) 1 else -1
                        }
                        cube[x + 50][y + 50][z + 50] = mode
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Long {
        fun isCross(a: IntRange, b: IntRange) = a.last >= b.first && b.last >= a.first
        fun crossedCor(
            a: Triple<IntRange, IntRange, IntRange>,
            b: Triple<IntRange, IntRange, IntRange>
        ): Triple<IntRange, IntRange, IntRange>? {
            return if (isCross(a.first, b.first) && isCross(a.second, b.second) && isCross(a.third, b.third)) {
                Triple(
                    max(a.first.first, b.first.first)..min(a.first.last, b.first.last),
                    max(a.second.first, b.second.first)..min(a.second.last, b.second.last),
                    max(a.third.first, b.third.first)..min(a.third.last, b.third.last),
                )
            } else {
                null
            }
        }

        return input.map(::parse).fold(
            emptyList<Pair<Triple<IntRange, IntRange, IntRange>, Int>>()
        ) { acc, (cor, mode) ->
            buildList {
                for ((c, m) in acc) {
                    add(c to m)
                    val crossed = crossedCor(c, cor) ?: continue
                    when {
                        m == 1 && mode -> {
                            add(crossed to -1)
                        }
                        m == -1 && mode -> {
                            add(crossed to 1)
                        }
                        m == 1 && !mode -> {
                            add(crossed to -1)
                        }
                        m == -1 && !mode -> {
                            add(crossed to 1)
                        }
                    }
                }
                if (mode) {
                    add(cor to 1)
                }
            }
        }.fold(0L) { acc, (cor, mode) ->
            acc + (cor.first.last - cor.first.first + 1).toLong()
                .times((cor.second.last - cor.second.first + 1).toLong())
                .times((cor.third.last - cor.third.first + 1).toLong()).times(mode)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 590_784)
    val testInput2 = readInput("Day22_test2")
    check(part2(testInput2) == 2_758_514_936_282_235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
