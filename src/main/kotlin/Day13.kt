fun main() {
    fun parse(input: List<String>): Pair<Array<Array<Boolean>>, List<Pair<String, Int>>> {
        val data = input.filter {
            it.isNotBlank()
        }.groupBy(
            { if (it.startsWith("fold")) "instruct" else "init" },
            { it.split(" ").last() }
        )
        val inits = data.getOrDefault("init", emptyList()).map {
            val (x, y) = it.split(",").map(String::toInt)
            x to y
        }
        val arr = Array(inits.maxOf { it.second } + 1) {
            Array(inits.maxOf { it.first } + 1) { false }
        }
        inits.forEach { (x, y) ->
            arr[y][x] = true
        }
        return arr to data.getOrDefault("instruct", emptyList()).map {
            val (axis, s) = it.split("=")
            axis to s.toInt()
        }
    }

    fun operate(arr: Array<Array<Boolean>>, fold: Pair<String, Int>): Array<Array<Boolean>> {
        val (axis, n) = fold
        val next = Array(if (axis == "y") n else arr.size) {
            Array(if (axis == "x") n else arr[0].size) { false }
        }
        arr.indices.forEach y@{ i ->
            val y = when {
                axis == "x" -> i
                i < n -> i
                i == n -> return@y
                2 * n - i < 0 -> return@y
                else -> 2 * n - i
            }
            arr[0].indices.forEach x@{ j ->
                val x = when {
                    axis == "y" -> j
                    j < n -> j
                    j == n -> return@x
                    2 * n - j < 0 -> return@x
                    else -> 2 * n - j
                }
                next[y][x] = arr[y][x] || arr[i][j]
            }
        }
        return next
    }

    fun part1(input: List<String>): Int {
        var arr: Array<Array<Boolean>>
        val instructs: List<Pair<String, Int>>
        parse(input).let { (ar, ins) ->
            arr = ar
            instructs = ins
        }
        instructs.take(1).forEach {
            arr = operate(arr, it)
        }
        return arr.flatten().count { it }
    }

    fun part2(input: List<String>): Array<Array<Boolean>> {
        var arr: Array<Array<Boolean>>
        val instructs: List<Pair<String, Int>>
        parse(input).let { (ar, ins) ->
            arr = ar
            instructs = ins
        }
        instructs.forEach {
            arr = operate(arr, it)
        }
        return arr
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    part2(input).forEach {
        println(it.map { if (it) "#" else "." }.joinToString(""))
    }
}
