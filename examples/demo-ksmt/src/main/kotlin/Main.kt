import org.ksmt.KContext
import org.ksmt.solver.KSolverStatus
import org.ksmt.solver.z3.KZ3Solver
import org.ksmt.utils.mkConst

fun main() {
    val ctx = KContext()
    val solver = KZ3Solver(ctx)

    with(ctx) {
        val queens = List(8) { intSort.mkConst("q_$it") }

        for (it in queens) {
            solver.assert((0.expr le it) and (it le 7.expr))
        }

        solver.assert(mkDistinct(queens))

        val magic1 = queens.mapIndexed { column, queen -> queen + column.expr }
        solver.assert(mkDistinct(magic1))

        val magic2 = queens.mapIndexed { column, queen -> queen - column.expr }
        solver.assert(mkDistinct(magic2))

        var status = solver.check()
        var cnt = 0

        while (status == KSolverStatus.SAT) {
            cnt++
            val model = solver.model()

            val positions = queens.map { model.eval(it) }
            println(positions.toString())

            val constraints = positions.mapIndexed { column, pos -> queens[column] eq pos }
            solver.assert(mkAnd(constraints).not())

            status = solver.check()
        }

        println(cnt)
    }
}