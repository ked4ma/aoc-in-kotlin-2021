import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return input.flatMap {
            it.split("|")[1].trim().split(" ")
        }.filter {
            it.length in arrayOf(2, 3, 4, 7)
        }.size
    }

    fun part2(input: List<String>): Int {
        fun analysis(d: String): Int {
            val baseData = d.split("|").first().trim().split(" ").map { str ->
                str.sumOf { 1 shl (it - 'a') }
            }.sortedBy { it.countOneBits() }

            val digitMap = buildMap<Int, Int> {
                put(1, baseData[0])
                put(7, baseData[1])
                put(4, baseData[2])
                put(8, baseData[9])
                // 2 and 3 and 5 -> ≡
                val land = baseData[3] and baseData[4] and baseData[5]
                put(3, land or getValue(1))
                put(9, getValue(3) or getValue(4))
                put(0, getValue(8) - ((getValue(4) - getValue(1)) and getValue(3)))
                // remain 2, 5, 6
                val x = getValue(3) - getValue(1) + (getValue(8) - getValue(9))
                put(2, baseData.first {
                    ((it or x) - x).countOneBits() == 1
                })
                put(5, (getValue(8) - getValue(2)) or land)
                put(6, getValue(5) or (getValue(0) - getValue(1)))
            }.map { (k, v) ->
                v to k
            }.toMap()

            return d.split("|").last().trim().split(" ").map { str ->
                str.sumOf { 1 shl (it - 'a') }
            }.fold(0) { acc, v ->
                acc * 10 + digitMap.getValue(v)
            }
        }

        return input.sumOf(::analysis)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)

    val smallTestInput = readInput("Day08_test_small")
    check(part2(smallTestInput) == 5353)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
