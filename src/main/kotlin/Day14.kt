fun main() {
    fun step(input: Map<String, Long>, mapping: Map<String, String>): Map<String, Long> = buildMap {
        input.forEach { (k, v) ->
            if (k in mapping) {
                compute("${k[0]}${mapping.getValue(k)}") { _, cur ->
                    (cur ?: 0) + v
                }
                compute("${mapping.getValue(k)}${k[1]}") { _, cur ->
                    (cur ?: 0) + v
                }
            } else {
                compute(k) { _, cur ->
                    v + (cur ?: 0)
                }
            }
        }
    }

    fun solve(init: String, input: Map<String, Long>): Long = input.flatMap { (k, v) ->
        listOf(k[0] to v, k[1] to v)
    }.groupingBy { it.first }.aggregate { _, acc: Long?, element, _ ->
        (acc ?: 0L) + element.second
    }.mapValues { (k, v) ->
        (v + (if (k == init.first()) 1 else 0) + (if (k == init.last()) 1 else 0)) / 2
    }.values.run {
        maxOf { it } - minOf { it }
    }

    fun part1(input: List<String>): Long {
        val template = input[0]
        val mapping = input.subList(2, input.size).associate {
            val (pair, ins) = it.split(" -> ")
            pair to ins
        }
        return (0 until 10).fold(
            initial = template.windowed(2, 1, false).groupingBy {
                it
            }.eachCount().mapValues { (_, v) -> v.toLong() }
        ) { acc, _ -> step(acc, mapping) }.let {
            solve(template, it)
        }
    }

    fun part2(input: List<String>): Long {
        val template = input[0]
        val mapping = input.subList(2, input.size).associate {
            val (pair, ins) = it.split(" -> ")
            pair to ins
        }
        return (0 until 40).fold(
            initial = template.windowed(2, 1, false).groupingBy {
                it
            }.eachCount().mapValues { (_, v) -> v.toLong() }
        ) { acc, _ -> step(acc, mapping) }.let {
            solve(template, it)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2_188_189_693_529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}