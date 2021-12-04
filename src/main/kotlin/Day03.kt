fun main() {
    fun part1(input: List<String>): Int {
        val countArr = IntArray(input.first().length)
        input.forEach { binary ->
            binary.forEachIndexed { index, b ->
                if (b == '1') countArr[index]++
            }
        }
        val gamma = countArr.map {
            if (it * 2 >= input.size) '1' else '0'
        }.toCharArray().concatToString().let {
            Integer.parseInt(it, 2)
        }
        val epsilon = countArr.map {
            if (it * 2 < input.size) '1' else '0'
        }.toCharArray().concatToString().let {
            Integer.parseInt(it, 2)
        }
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        fun calcRating(input: List<String>, bitCriteria: (size: Int, oneCount: Int) -> Boolean): Int {
            var remains = input
            var i = 0
            while (remains.size > 1 && i < input.first().length) {
                val oneCount = remains.count { it[i] == '1' }
                val bc = bitCriteria(remains.size, oneCount)
                remains = remains.filter {
                    (bc && it[i] == '1') || (!bc && it[i] == '0')
                }
                i++
            }
            return Integer.parseInt(remains.first(), 2)
        }

        val ogr = calcRating(input) { size, count ->
            count * 2 >= size
        }
        val csr = calcRating(input) { size, count ->
            count * 2 < size
        }
        return ogr * csr
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
