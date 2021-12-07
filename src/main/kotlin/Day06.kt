fun main() {
    fun part1(input: List<String>, rep: Int = 80): Int {
        var lanternfishes = input.first().split(",").map(String::toInt)
        repeat(rep) { _ ->
            lanternfishes = buildList {
                lanternfishes.map {
                    if (it > 6) {
                        add(it - 1)
                    } else {
                        add((it - 1 + 7) % 7)
                    }
                }
                repeat(lanternfishes.count { it == 0 }) {
                    add(8)
                }
            }
        }
        return lanternfishes.size
    }

    fun part2(input: List<String>): Long {
        var lanternfishMap = input.first().split(",").map(String::toInt).groupingBy {
            it
        }.eachCount().mapValues { (_, v) -> v.toLong() }
        repeat(256) {
            lanternfishMap = buildMap {
                (0..8).forEach {
                    compute(it) { k, v ->
                        (v ?: 0L)
                        +when (k) {
                            in 0..5 -> lanternfishMap.getOrDefault(k + 1, 0L)
                            6 -> lanternfishMap.getOrDefault(0, 0L) + lanternfishMap.getOrDefault(k + 1, 0L)
                            7 -> lanternfishMap.getOrDefault(k + 1, 0L)
                            8 -> lanternfishMap.getOrDefault(0, 0L)
                            else -> 0L
                        }
                    }
                }
            }
        }
        return lanternfishMap.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput, 18) == 26)
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26_984_457_539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
