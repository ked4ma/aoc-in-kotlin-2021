import kotlin.math.abs
import kotlin.math.max

fun main() {

    fun parse(input: List<String>): Map<Int, List<Triple<Int, Int, Int>>> {
        var i = -1
        var list = mutableListOf<Triple<Int, Int, Int>>()
        return buildMap {
            input.forEach {
                if (it.isBlank()) {
                    put(i, list.toList())
                    return@forEach
                }
                if (it.startsWith("--- scanner")) {
                    i++
                    list = mutableListOf()
                    return@forEach
                }
                it.split(",").map(String::toInt).let { (x, y, z) ->
                    list.add(Triple(x, y, z))
                }
            }
            put(i, list.toList())
        }
    }

    val rotationVec: List<List<List<Int>>> = arrayOf( // sin, cos
        0 to 1,
        1 to 0,
        0 to -1,
        -1 to 0
    ).let { rot ->
        buildSet {
            for (a in rot) {
                for (b in rot) {
                    for (c in rot) {
                        add(
                            listOf(
                                listOf(
                                    a.second * b.second * c.second - a.first * c.first,
                                    -a.second * b.second * c.first - a.first * c.second,
                                    a.second * b.first
                                ),
                                listOf(
                                    a.first * b.second * c.second + a.second * c.first,
                                    -a.first * b.second * c.first + a.second * c.second,
                                    a.first * b.first
                                ),
                                listOf(
                                    -b.first * c.second,
                                    b.first * c.first,
                                    b.second
                                )
                            )
                        )
                    }
                }
            }
        }.toList()
    }

    fun findRelativePosition(
        beacon1: List<Triple<Int, Int, Int>>,
        beacon2: List<Triple<Int, Int, Int>>
    ): Pair<Triple<Int, Int, Int>, Int>? {
        rotationVec.forEachIndexed { index, rot ->
            // rotate
            val rotated = beacon2.map { (x, y, z) ->
                Triple(
                    x * rot[0][0] + y * rot[0][1] + z * rot[0][2],
                    x * rot[1][0] + y * rot[1][1] + z * rot[1][2],
                    x * rot[2][0] + y * rot[2][1] + z * rot[2][2]
                )
            }
            // compare
            for ((x0, y0, z0) in beacon1) {
                for ((x1, y1, z1) in rotated) {
                    val dx = x0 - x1
                    val dy = y0 - y1
                    val dz = z0 - z1
                    if (rotated.count { (x, y, z) ->
                            Triple(x + dx, y + dy, z + dz) in beacon1
                        } >= 12) {
                        return Triple(dx, dy, dz) to index
                    }
                }
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        val scanners = parse(input)
        val converted = mutableMapOf<Int, List<Triple<Int, Int, Int>>>()
        converted[0] = scanners.getValue(0)
        while (converted.size < scanners.size) {
            scanners.keys.filterNot { it == 0 }.forEach key1@{ key1 ->
                if (key1 in converted) return@key1 // already converted
                converted.filterNot { it.key == key1 }.forEach { (_, beacons0) ->
                    val beacons1 = scanners.getValue(key1)
                    findRelativePosition(beacons0, beacons1)?.let { (pos, rotIndex) ->
                        val rot = rotationVec[rotIndex]
                        converted[key1] = beacons1.map { (x, y, z) ->
                            Triple(
                                x * rot[0][0] + y * rot[0][1] + z * rot[0][2] + pos.first,
                                x * rot[1][0] + y * rot[1][1] + z * rot[1][2] + pos.second,
                                x * rot[2][0] + y * rot[2][1] + z * rot[2][2] + pos.third
                            )
                        }
                    }
                }
            }
        }
        return converted.values.flatten().toSet().size
    }

    fun part2(input: List<String>): Int {
        val scanners = parse(input)
        val scannerPos = mutableMapOf<Int, Triple<Int, Int, Int>>()
        val converted = mutableMapOf<Int, List<Triple<Int, Int, Int>>>()
        scannerPos[0] = Triple(0, 0, 0)
        converted[0] = scanners.getValue(0)
        while (converted.size < scanners.size) {
            scanners.keys.filterNot { it == 0 }.forEach key1@{ key1 ->
                if (key1 in converted) return@key1 // already converted
                converted.filterNot { it.key == key1 }.forEach { (_, beacons0) ->
                    val beacons1 = scanners.getValue(key1)
                    findRelativePosition(beacons0, beacons1)?.let { (pos, rotIndex) ->
                        scannerPos[key1] = pos
                        val rot = rotationVec[rotIndex]
                        converted[key1] = beacons1.map { (x, y, z) ->
                            Triple(
                                x * rot[0][0] + y * rot[0][1] + z * rot[0][2] + pos.first,
                                x * rot[1][0] + y * rot[1][1] + z * rot[1][2] + pos.second,
                                x * rot[2][0] + y * rot[2][1] + z * rot[2][2] + pos.third
                            )
                        }
                    }
                }
            }
        }
        var max = Int.MIN_VALUE
        val keys = scannerPos.keys.toList()
        for (i in 0 until keys.lastIndex) {
            val (x0, y0, z0) = scannerPos.getValue(keys[i])
            for (j in i..keys.lastIndex) {
                val (x1, y1, z1) = scannerPos.getValue(keys[j])
                max = max(
                    max,
                    abs(x1 - x0) + abs(y1 - y0) + abs(z1 - z0)
                )
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
