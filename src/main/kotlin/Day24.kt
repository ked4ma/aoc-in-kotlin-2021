fun main() {
    val ope = arrayOf("add", "mul", "div", "mod", "eql")

    open class Ops
    data class Inp(val idx: Int) : Ops()
    data class Calc(val op: Int, val a: Int, val b: Int, val isVar: Boolean = true) : Ops()

    fun split(input: List<String>) = sequence {
        var list = mutableListOf<Ops>()
        for (line in input) {
            if (line.startsWith("inp")) {
                if (list.isNotEmpty()) yield(list.toList())
                list = mutableListOf()
            }
            list.add(
                when {
                    line.startsWith("inp") -> Inp(line.split(" ")[1][0] - 'w')
                    else -> line.split(" ").let { (op, a, b) ->
                        if (b[0] in 'w'..'z') {
                            Calc(ope.indexOf(op), a[0] - 'w', b[0] - 'w', true)
                        } else {
                            Calc(ope.indexOf(op), a[0] - 'w', b.toInt(), false)
                        }
                    }
                }
            )
        }
        yield(list)
    }

    fun dfs(
        index: Int,
        cur: Long,
        opsGroup: List<List<Ops>>,
        vars: LongArray
    ): Long {
        if (index == opsGroup.size) {
            println("$cur ${vars.last()}")
            return if (vars.last() == 0L) cur else 0
        }
        val ops = opsGroup[index]
        (9 downTo 1).forEach{
            val v = vars.copyOf()
            ops.forEach { op ->
                when (op) {
                    is Inp -> v[op.idx] = it.toLong()
                    is Calc -> {
                        val a = op.a
                        val b = if (op.isVar) v[op.b] else op.b.toLong()
                        when (op.op) {
                            0 -> v[a] += b // add
                            1 -> v[a] *= b // mul
                            2 -> v[a] /= b // div
                            3 -> v[a] %= b // mod
                            4 -> v[a] = if (v[a] == b) 1 else 0 // eql
                        }
                    }
                }
            }
            val res = dfs(index + 1, cur * 10 + it, opsGroup, v)
            if (res > 0) {
                return res
            }
        }
        return 0
    }

    fun part1(input: List<String>): String {
        val ops = split(input).toList()
        val vars = longArrayOf(0, 0, 0, 0)
        return dfs(0, 0, ops, vars).toString()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day23_test")
//    check(part1(testInput) == 12_521)
//    check(part2(testInput) == 44_169)

    val input = readInput("Day24")
    println(part1(input).apply {
        check(this == "96299896449997")
    })
//    println(part2(input))
}
