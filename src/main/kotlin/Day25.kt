fun main() {
    fun parse(input: List<String>): Array<Array<Char>> = input.map { it.toCharArray().toTypedArray() }.toTypedArray()

    fun step(data: Array<Array<Char>>): Int {
        val h = data.size
        val w = data[0].size
        var moves = 0

        // >
        var origin = data.map { it.copyOf() }.toTypedArray()
        for (i in data.indices) {
            for (j in data[0].indices) {
                if (origin[i][j] == '>') {
                    val nj = (j + 1) % w
                    if (origin[i][nj] == '.') {
                        data[i][j] = '.'
                        data[i][nj] = '>'
                        moves++
                    }
                }
            }
        }
        // v
        origin = data.map { it.copyOf() }.toTypedArray()
        for (i in data.indices) {
            for (j in data[0].indices) {
                if (origin[i][j] == 'v') {
                    val ni = (i + 1) % h
                    if (origin[ni][j] == '.') {
                        data[i][j] = '.'
                        data[ni][j] = 'v'
                        moves++
                    }
                }
            }
        }
        return moves
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)
        var steps = 0
        do {
            val moves = step(map)
            steps++
        } while (moves > 0)
        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
}
