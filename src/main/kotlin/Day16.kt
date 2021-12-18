import java.lang.IllegalArgumentException

fun main() {
    data class Packet(
        val version: Int,
        val type: Int,
        val sub: List<Packet> = emptyList(),
        val data: List<String> = emptyList()
    )

    fun toBinaryString(hex: String) = hex.map {
        Integer.toBinaryString(
            Integer.parseInt(it.toString(), 16)
        ).padStart(4, '0')
    }.joinToString("")

    fun toDecimal(binary: String) = Integer.parseInt(binary, 2)

    fun parse(binary: String): Pair<Packet, Int> {
        val version = toDecimal(binary.substring(0, 3))
        val type = toDecimal(binary.substring(3, 6))
        var i = 6
        val operation = toDecimal(binary.substring(6, 7))
        return when {
            type == 4 -> { // literal
                val data = buildList {
                    while (i + 5 <= binary.length) {
                        add(binary.substring(i, i + 5))
                        i += 5
                        if (binary[i - 5] == '0') break
                    }
                }
                Packet(version = version, type = type, data = data) to i
            }
            operation == 0 -> { // total length in bits (next 15bits)
                i = 7
                val l = toDecimal(binary.substring(i, i + 15))
                i += 15
                var cur = 0
                val sub = buildList {
                    while (cur < l) {
                        val (s, n) = parse(binary.substring(i + cur))
                        add(s)
                        cur += n
                    }
                }
                Packet(version = version, type = type, sub = sub) to i + cur
            }
            operation == 1 -> { // number of sub-packets immediately contained (next 11bits)
                i = 7
                val l = toDecimal(binary.substring(i, i + 11))
                i += 11
                var cur = 0
                val sub = buildList {
                    repeat(l) {
                        val (s, n) = parse(binary.substring(i + cur))
                        add(s)
                        cur += n
                    }
                }
                Packet(version = version, type = type, sub = sub) to i + cur
            }
            else -> throw IllegalArgumentException()
        }
    }

    fun part1(input: String): Int {
        val (p, _) = parse(toBinaryString(input))
        fun cul(p: Packet): Int = p.version + p.sub.sumOf(::cul)
        return cul(p)
    }

    fun part2(input: String): Long {
        val (p, _) = parse(toBinaryString(input))
        fun cul(p: Packet): Long = when (p.type) {
            0 -> p.sub.sumOf(::cul)
            1 -> p.sub.map(::cul).reduce { acc, i -> acc * i }
            2 -> p.sub.minOf(::cul)
            3 -> p.sub.maxOf(::cul)
            4 -> p.data.joinToString("") { it.substring(1) }.toCharArray().fold(0L) { acc, c ->
                (acc shl 1) + (c - '0')
            }
            5 -> p.sub.map(::cul).let { if (it[0] > it[1]) 1 else 0 }
            6 -> p.sub.map(::cul).let { if (it[0] < it[1]) 1 else 0 }
            7 -> p.sub.map(::cul).let { if (it[0] == it[1]) 1 else 0 }
            else -> throw IllegalArgumentException()
        }
        return cul(p)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(testInput.map(::part1) == listOf(16, 12, 23, 31))
    val testInput2 = readInput("Day16_test2")
    check(testInput2.map(::part2) == listOf(3L, 54L, 7L, 9L, 1L, 0L, 0L, 1L))

    val input = readInput("Day16")
    println(part1(input.first()))
    println(part2(input.first()))
}
