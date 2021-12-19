import kotlin.math.max

fun main() {
    open class Node
    data class NumNode(
        val num: Int
    ) : Node() {
        override fun toString(): String = num.toString()
    }

    data class PairNode(
        val left: Node,
        val right: Node
    ) : Node() {
        override fun toString(): String = "[$left,$right]"
    }

    fun parseLine(input: String): List<Any> {
        val queue = ArrayDeque<Any>()
        var index = 0
        while (index < input.length) {
            when {
                input[index] in arrayOf('[', ']') -> {
                    queue.add(input[index])
                }
                input[index] == ',' -> Unit
                else -> { // num
                    var i = index
                    while (input[i] !in arrayOf(',', ']')) i++
                    queue.add(input.substring(index, i).toInt())
                }
            }
            index++
        }
        return queue.toList()
    }

    fun parse(input: List<String>): List<List<Any>> {
        return input.map(::parseLine)
    }

    fun add(a: List<Any>, b: List<Any>): List<Any> = ArrayDeque<Any>().apply {
        add('[')
        addAll(a)
        addAll(b)
        add(']')
    }.toList()

    fun reduce(input: List<Any>): List<Any> {
        var data = input
        // judge
        do {
            var nest = 0
            var explode = false
            var split = false
            var target = -1
            for (i in data.indices) {
                when {
                    data[i] == '[' -> {
                        nest++
                        if (nest > 4) {
                            explode = true
                            target = i
                            break
                        }
                    }
                    data[i] == ']' -> nest--
                    data[i] as Int >= 10 -> {
                        split = true
                        if (target == -1) target = i
                    }
                }
            }
            if (explode) {
                var left = target
                while (left >= 0 && data[left] !is Int) left--
                var right = target + 3
                while (right <= data.lastIndex && data[right] !is Int) right++
                val queue = ArrayDeque<Any>()
                data.forEachIndexed { index, d ->
                    when {
                        index == left -> queue.add(d as Int + data[target + 1] as Int)
                        index == target -> Unit
                        index == target + 1 -> Unit
                        index == target + 2 -> queue.add(0)
                        index == target + 3 -> Unit
                        index == right -> queue.add(d as Int + data[target + 2] as Int)
                        else -> queue.add(d)
                    }
                }
                data = queue.toList()
            } else if (split) {
                val queue = ArrayDeque<Any>()
                data.forEachIndexed { index, d ->
                    when (index) {
                        target -> {
                            queue.add('[')
                            queue.add(d as Int / 2)
                            queue.add((d as Int + 1) / 2)
                            queue.add(']')
                        }
                        else -> queue.add(d)
                    }
                }
                data = queue.toList()
            }
        } while (explode || split)
        return data.toList()
    }

    fun magnitude(input: List<Any>): Int {
        val queue = ArrayDeque<Int>()
        input.forEach {
            when (it) {
                is Int -> queue.add(it)
                ']' -> queue.add(queue.removeLast() * 2 + queue.removeLast() * 3)
            }
        }
        return queue.removeLast()
    }

    fun part1(input: List<String>): Int {
        val numbers = parse(input).reduce { acc, fish ->
            add(acc, fish).let(::reduce)
        }
        return magnitude(numbers)
    }

    fun part2(input: List<String>): Int {
        val fishes = parse(input)
        return (0 until fishes.lastIndex).maxOf { i ->
            (i..fishes.lastIndex).maxOf { j ->
                max(
                    add(fishes[i], fishes[j]).let(::reduce).let(::magnitude),
                    add(fishes[j], fishes[i]).let(::reduce).let(::magnitude)
                )
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
