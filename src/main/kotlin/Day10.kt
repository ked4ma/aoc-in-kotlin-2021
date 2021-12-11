fun main() {
    fun part1(input: List<String>): Int {
        val scoreMap = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
        return input.map { line ->
            val stack = ArrayDeque<Char>()
            for (c in line) {
                when (c) {
                    '(' -> stack.add(')')
                    '[' -> stack.add(']')
                    '{' -> stack.add('}')
                    '<' -> stack.add('>')
                    else -> if (stack.lastOrNull() == c) stack.removeLast() else return@map scoreMap.getValue(c)
                }
            }
            return@map 0
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val scoreMap = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )
        return input.map { line ->
            val stack = ArrayDeque<Char>()
            for (c in line) {
                when (c) {
                    '(' -> stack.add(')')
                    '[' -> stack.add(']')
                    '{' -> stack.add('}')
                    '<' -> stack.add('>')
                    else -> if (stack.lastOrNull() == c) stack.removeLast() else return@map 0
                }
            }
            var score = 0L
            while (stack.isNotEmpty()) {
                score = score * 5 + scoreMap.getValue(stack.removeLast())
            }
            return@map score
        }.filterNot { it == 0L }.sorted().let {
            it[it.size / 2]
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
