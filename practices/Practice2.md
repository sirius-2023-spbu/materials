# Практика #2

Использование KSMT.

### Подключение KSMT

```
repositories {
    ...
    maven(url = uri("https://jitpack.io"))
}

dependencies {
    ...
    
    // ksmt core
    implementation("com.github.UnitTestBot.ksmt:ksmt-core:0.3.1")

    // ksmt z3 solver
    implementation("com.github.UnitTestBot.ksmt:ksmt-z3:0.3.1")
}
    
```

### Пример

Существует ли целое число `a` такое, что `a > 0`, но `a + 1 <= 0`?

```kotlin
fun main() {
    val ctx = KContext()

    val a = ctx.intSort.mkConst("a")
    val aPlus1 = ctx.mkArithAdd(a, ctx.mkIntNum(1))

    val solver = KZ3Solver(ctx)

    solver.assert(ctx.mkArithGt(a, ctx.mkIntNum(0)))
    solver.assert(ctx.mkArithLe(aPlus1, ctx.mkIntNum(0)))

    val result = solver.check()
    if (result == KSolverStatus.SAT) {
        println("SAT")
        val model = solver.model()
        println(model.eval(a))
    } else {
        println("UNSAT")
    }

    solver.close()
    ctx.close()
}
```

### Работа с выражением в KSMT

В KSMT у любого выражения должен быть определён тип (sort).

```kotlin
val bool = ctx.boolSort.mkConst("bool")
val int = ctx.intSort.mkConst("int")
val bv = ctx.bv32sort.mkConst("bv32")
val fp = ctx.fp32Sort.mkConst("fp32")

val func = ctx.mkFuncDecl("func", ctx.intSort, listOf(ctx.intSort))
val arr = mkArrayConst(mkArraySort(intSort, intSort), 1.expr)
```

Можно получать новые выражения с помощью операций:

```kotlin
val bv1 = mkBv(1)
val bv2 = mkBv(2)
val sum = mkBvAddExpr(bv1, bv2)

val int1 = 1.expr
val int10 = 10.expr

val sum = int1 + int2
val tr = sum eq 3.expr
```

### Работа с Солвером:

```kotlin
val ctx = KContext()
val solver = KZ3Solver(ctx)

with(ctx) {
    val int1 = intSort.mkConst("int1")
    val int2 = intSort.mkConst("int2")

    val sum = int1 + int2

    val tr = sum eq 3.expr

    solver.assert(tr and (int1 eq 10.expr))
    val status = solver.check()
    println(status)

    val model = solver.model()
    println(model)

    println(model.eval(int2 + 4.expr))
}
```

### Работа с массивами

```kotlin
var array = mkArraySort(intSort, boolSort).mkConst("array")

solver.assert(!(array.select(0.expr) eq array.select(1.expr)))

array = array.store(0.expr, trueExpr)

solver.assert(array.select(0.expr) eq array.select(1.expr))
solver.check()

val model = solver.model()
println(model)
```

## Задание

1. Выведите с помощью KSMT все расстановки ферзей на доске
   - Как не выводить повторяющиеся элементы?
2. Попробуйте закодировать задачу про рыцарей с помощью KSMT
   - [Пример](https://microsoft.github.io/z3guide/docs/logic/Quantifiers/) как это сделать с помощью SMT-LIB