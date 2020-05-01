package life.shank

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import life.shank.ParameterScopedModule.noParamNonClassScoped
import life.shank.ParameterScopedModule.noParamScoped
import life.shank.ParameterScopedModule.oneParamScoped
import life.shank.ParameterScopedModule.threeParamScoped
import life.shank.ParameterScopedModule.twoParamScoped
import life.shank.ScopedTests.ClearableScoped.clearable0
import life.shank.ScopedTests.ClearableScoped.clearable1
import life.shank.ScopedTests.ClearableScoped.clearable2
import life.shank.ScopedTests.ClearableScoped.clearable3
import life.shank.ScopedTests.ConcurrentScoped.nanotime0
import life.shank.ScopedTests.ConcurrentScoped.nanotime1
import life.shank.ScopedTests.ConcurrentScoped.nanotime2
import life.shank.ScopedTests.ConcurrentScoped.nanotime3
//import life.shank.ScopedTests.ConcurrentScoped.nanotime4
//import life.shank.ScopedTests.ConcurrentScoped.nanotime5
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import shouldBeEqualTo
import shouldBeSameReference
import shouldNotBeEqualTo


private object ParameterScopedModule : ShankModule {
    val noParamScoped = scoped { -> DataForTest() }
    val oneParamScoped = scoped { a: Int -> DataForTest(a) }
    val twoParamScoped = scoped { a: Int, b: Int -> DataForTest(a, b) }
    val threeParamScoped = scoped { a: Int, b: Int, c: Int -> DataForTest(a, b, c) }

    val noParamNonClassScoped = scoped { -> Any() }
}

class ScopedTests : Scoped {
    override val scope: Scope = Scope("singleton")

    @Before
    fun setUp() {
        resetShank()
    }

    @Test
    fun `supports parameters`() {
        oneParamScoped(1) shouldBeEqualTo DataForTest(1)
        oneParamScoped(1) shouldBeSameReference oneParamScoped(1)

        twoParamScoped(1, 2) shouldBeEqualTo DataForTest(1, 2)
        twoParamScoped(1, 2) shouldBeSameReference twoParamScoped(1, 2)

        threeParamScoped(1, 2, 3) shouldBeEqualTo DataForTest(1, 2, 3)
        threeParamScoped(1, 2, 3) shouldBeSameReference threeParamScoped(1, 2, 3)

//        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
//        fourParamScoped(1, 2, 3, 4) shouldBeSameReference fourParamScoped(1, 2, 3, 4)
//
//        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
//        fiveParamScoped(1, 2, 3, 4, 5) shouldBeSameReference fiveParamScoped(1, 2, 3, 4, 5)
    }

    @Test
    @Ignore
    fun `allows override`() {
        noParamScoped() shouldBeEqualTo DataForTest()
        noParamScoped.overrideFactory { -> DataForTest(2) }
        noParamScoped() shouldBeEqualTo DataForTest(2)
        noParamScoped.restore()

        oneParamScoped(1) shouldBeEqualTo DataForTest(1)
        oneParamScoped.overrideFactory { a: Int -> DataForTest(a * 2) }
        oneParamScoped(1) shouldBeEqualTo DataForTest(2)
        oneParamScoped.restore()

        twoParamScoped(1, 2) shouldBeEqualTo DataForTest(1, 2)
        twoParamScoped.overrideFactory { a: Int, b: Int -> DataForTest(a * 2, b * 2) }
        twoParamScoped(1, 2) shouldBeEqualTo DataForTest(2, 4)
        twoParamScoped.restore()

        threeParamScoped(1, 2, 3) shouldBeEqualTo DataForTest(1, 2, 3)
        threeParamScoped.overrideFactory { a: Int, b: Int, c: Int -> DataForTest(a * 2, b * 2, c * 2) }
        threeParamScoped(1, 2, 3) shouldBeEqualTo DataForTest(2, 4, 6)
        threeParamScoped.restore()

//        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
//        fourParamScoped.overrideFactory { a: Int, b: Int, c: Int, d: Int ->
//            ParamData(
//                a * 2,
//                b * 2,
//                c * 2,
//                d * 2
//            )
//        }
//        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)
//        fourParamScoped.restore()
//
//        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
//        fiveParamScoped.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int ->
//            ParamData(
//                a * 2,
//                b * 2,
//                c * 2,
//                d * 2,
//                e * 2
//            )
//        }
//        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
//        fiveParamScoped.restore()
    }

    @Test
    fun `provides different values per params`() {
        oneParamScoped(1) shouldBeSameReference oneParamScoped(1)
        oneParamScoped(2).a shouldBeEqualTo 2

        twoParamScoped(1, 2) shouldBeEqualTo DataForTest(1, 2)
        twoParamScoped(2, 3) shouldBeEqualTo DataForTest(2, 3)

        threeParamScoped(1, 2, 3) shouldBeEqualTo DataForTest(1, 2, 3)
        threeParamScoped(2, 3, 4) shouldBeEqualTo DataForTest(2, 3, 4)

//        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
//        fourParamScoped(2, 3, 4, 5) shouldBeEqualTo ParamData(2, 3, 4, 5)
//
//        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
//        fiveParamScoped(2, 3, 4, 5, 6) shouldBeEqualTo ParamData(2, 3, 4, 5, 6)
    }


    @Test
    fun `clears scope with final action`() {
        noParamScoped()

        scope.clearWithAction { value ->
            when {
//                wrapper == newFoo -> println(newFoo())
            }
        }
    }

    val nested = scope.nest()

    @Test
    fun `supports nested scopes`() {
        noParamNonClassScoped(nested) shouldBeEqualTo noParamNonClassScoped(nested)
    }

    @Test
    fun `nested scope is different from parent`() {
        noParamNonClassScoped(nested) shouldNotBeEqualTo noParamNonClassScoped(scope)
    }

    @Test
    fun `clearing child scope does not clear parent`() {
        val child = noParamNonClassScoped(nested)
        val parent = noParamNonClassScoped(scope)
        nested.clear()

        child shouldNotBeEqualTo noParamNonClassScoped(nested)
        parent shouldBeEqualTo noParamNonClassScoped(scope)
    }

    @Test
    fun `clearing parent scope clears parent`() {
        val child = noParamNonClassScoped(nested)
        val parent = noParamNonClassScoped(scope)
        scope.clear()

        parent shouldNotBeEqualTo noParamNonClassScoped(scope)
        child shouldNotBeEqualTo noParamNonClassScoped(nested)
    }

    @Test
    fun `gets cached value from parent scope if no nested cached value is found`() {
        val parent = noParamNonClassScoped(scope)
        val child = noParamNonClassScoped(nested)

        parent shouldBeEqualTo child
    }

    @Test
    fun `clearable objects are notified on clear`() {
        addGlobalOnClearAction { if (it is Clearable) it.onClear() }

        val c0 = clearable0()
        val c1 = clearable1(0)
        val c2 = clearable2(0, 0)
        val c3 = clearable3(0, 0, 0)

        c0.cleared shouldBeEqualTo false
        c1.cleared shouldBeEqualTo false
        c2.cleared shouldBeEqualTo false
        c0.cleared shouldBeEqualTo false

        scope.clear()

        c1.cleared shouldBeEqualTo true
        c2.cleared shouldBeEqualTo true
        c3.cleared shouldBeEqualTo true
        c3.cleared shouldBeEqualTo true
    }

    @Test
    fun `adds on clear actions`() {
        var result = "failed"
        var otherResult = "failed"
        scope.addOnClearAction { result = "success" }
        scope.addOnClearAction { otherResult = "success" }
        noParamNonClassScoped()
        result shouldBeEqualTo "failed"
        otherResult shouldBeEqualTo "failed"

        scope.clear()

        result shouldBeEqualTo "success"
        otherResult shouldBeEqualTo "success"
    }

    @Test
    fun `on clear actions can be removed`() {
        var result = "success"
        var otherResult = "success"

        val action = { result = "failed" }
        val otherAction = { otherResult = "failed" }

        scope.addOnClearAction(action)
        scope.addOnClearAction(otherAction)
        noParamNonClassScoped()
        result shouldBeEqualTo "success"
        otherResult shouldBeEqualTo "success"


        scope.removeOnClearAction(action)
        scope.removeOnClearAction(otherAction)
        scope.clear()

        result shouldBeEqualTo "success"
        otherResult shouldBeEqualTo "success"
    }

    @Test
    fun `supports concurrent requests`() {
        runBlocking(Dispatchers.Default) {
            testConcurrentAccess { nanotime0() }
            testConcurrentAccess { nanotime1(0) }
            testConcurrentAccess { nanotime2(0, 0) }
            testConcurrentAccess { nanotime3(0, 0, 0) }
//            testConcurrentAccess { nanotime4(0, 0, 0, 0) }
//            testConcurrentAccess { nanotime5(0, 0, 0, 0, 0) }
        }

    }

    private object ConcurrentScoped : ShankModule {
        val nanotime0 = scoped { -> getTimeSlow() }
        val nanotime1 = scoped { _: Any -> getTimeSlow() }
        val nanotime2 = scoped { _: Any, _: Any -> getTimeSlow() }
        val nanotime3 = scoped { _: Any, _: Any, _: Any -> getTimeSlow() }
//        val nanotime4 = scoped { _: Any, _: Any, _: Any, _: Any -> getTimeSlow() }
//        val nanotime5 = scoped { _: Any, _: Any, _: Any, _: Any, _: Any -> getTimeSlow() }

        private inline fun getTimeSlow(): Long {
            Thread.sleep(1)
            return System.nanoTime()
        }
    }

    object ClearableScoped : ShankModule {
        val clearable0 = scoped { -> ClearableClass() }
        val clearable1 = scoped { _: Any -> ClearableClass() }
        val clearable2 = scoped { _: Any, _: Any -> ClearableClass() }
        val clearable3 = scoped { _: Any, _: Any, _: Any -> ClearableClass() }
    }

    class ClearableClass : Clearable {

        var cleared = false

        override fun onClear() {
            cleared = true
        }
    }
}

interface Clearable {
    fun onClear()
}
