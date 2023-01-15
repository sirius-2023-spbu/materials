import org.kosat.Kosat

interface Base

class B : Base

class A : Base

fun varNumber(u: Int, c: Int): Int {
    return 3 * u + c + 1
}

fun main() {
    val n = 4
    val graph = listOf(
        listOf(1, 2, 3),
        listOf(0, 2, 3),
        listOf(0, 1, 3),
        listOf(0, 1, 2),
    )

    val solver = Kosat(mutableListOf(), 3 * n)

    (0 until n).forEach { u ->
        val c1 = varNumber(u, 0)
        val c2 = varNumber(u, 1)
        val c3 = varNumber(u, 2)

        solver.addClause(c1, c2, c3)

        solver.addClause(-c1, -c2)
        solver.addClause(-c2, -c3)
        solver.addClause(-c1, -c3)
    }

    for (u in graph.indices) {
        for (v in graph[u]) {
            for (c in 0 until 3) {
                val cu = varNumber(u, c)
                val cv = varNumber(v, c)
                solver.addClause(-cu, -cv)
            }
        }
    }

    val sat = solver.solve()
    println(sat)

    if (!sat) {
        return
    }

    for (u in graph.indices) {
        val color = (0 until 3).first {
            val c = varNumber(u, it)
            solver.getValue(c)
        }
        println(color)
    }

}