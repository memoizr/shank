package life.shank

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import life.shank.ParameterScopedModule.scoped0
import life.shank.ParameterScopedModule.scoped1
import life.shank.ParameterScopedModule.scoped2
import life.shank.ParameterScopedModule.scoped3
import life.shank.ParameterScopedModule.scoped4
import life.shank.ParameterScopedModule.scoped5
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo
import shouldBeSameReference
import shouldBeSameReferenceAsAnyOf
import shouldNotBeSameReference
import testConcurrentSingletonGet


private object ParameterScopedModule : ShankModule {
    val scoped0 = scoped { -> DataForTest() }
    val scoped1 = scoped { a: Int -> DataForTest(a) }
    val scoped2 = scoped { a: Int, b: Int -> DataForTest(a, b) }
    val scoped3 = scoped { a: Int, b: Int, c: Int -> DataForTest(a, b, c) }
    val scoped4 = scoped { a: Int, b: Int, c: Int, d: Int -> DataForTest(a, b, c, d) }
    val scoped5 = scoped { a: Int, b: Int, c: Int, d: Int, e: Int -> DataForTest(a, b, c, d, e) }
}

class ScopedTests : Scoped {
    override val scope: Scope = Scope("singleton")

    @Before
    fun setUp() {
        Shank.internalInstancesInScopesCache.clear()
    }

    @Test
    fun `can register factories for scoped instance with up to 5 parameters`() {
        scoped0() shouldBeEqualTo DataForTest()
        scoped0() shouldBeSameReference scoped0()

        scoped1(1) shouldBeEqualTo DataForTest(1)
        scoped1(1) shouldBeSameReference scoped1(1)

        scoped2(1, 2) shouldBeEqualTo DataForTest(1, 2)
        scoped2(1, 2) shouldBeSameReference scoped2(1, 2)

        scoped3(1, 2, 3) shouldBeEqualTo DataForTest(1, 2, 3)
        scoped3(1, 2, 3) shouldBeSameReference scoped3(1, 2, 3)

        scoped4(1, 2, 3, 4) shouldBeEqualTo DataForTest(1, 2, 3, 4)
        scoped4(1, 2, 3, 4) shouldBeSameReference scoped4(1, 2, 3, 4)

        scoped5(1, 2, 3, 4, 5) shouldBeEqualTo DataForTest(1, 2, 3, 4, 5)
        scoped5(1, 2, 3, 4, 5) shouldBeSameReference scoped5(1, 2, 3, 4, 5)
    }

    @Test
    fun `different parameters will create new scoped for those parameters within same scope`() {
        scoped1(1) shouldNotBeSameReference scoped1(2)
        scoped2(1, 2) shouldNotBeSameReference scoped2(2, 4)
        scoped3(1, 2, 3) shouldNotBeSameReference scoped3(2, 4, 6)
        scoped4(1, 2, 3, 4) shouldNotBeSameReference scoped4(2, 4, 6, 8)
        scoped5(1, 2, 3, 4, 5) shouldNotBeSameReference scoped5(2, 4, 6, 8, 10)
    }

    @Test
    fun `supports concurrent and suspend requests`() = runBlocking(Dispatchers.Default) {
        testConcurrentSingletonGet { scoped0() }
        testConcurrentSingletonGet { scoped1(1) }
        testConcurrentSingletonGet { scoped2(1, 2) }
        testConcurrentSingletonGet { scoped3(1, 2, 3) }
        testConcurrentSingletonGet { scoped4(1, 2, 3, 4) }
        testConcurrentSingletonGet { scoped5(1, 2, 3, 4, 5) }
    }

    @Test
    fun `scope can be cleared`() {
        val initial0 = scoped0()
        val initial1 = scoped1(1)
        val initial2 = scoped2(1, 2)
        val initial3 = scoped3(1, 2, 3)
        val initial4 = scoped4(1, 2, 3, 4)
        val initial5 = scoped5(1, 2, 3, 4, 5)

        scope.clear()
        val new0 = scoped0()
        val new1 = scoped1(1)
        val new2 = scoped2(1, 2)
        val new3 = scoped3(1, 2, 3)
        val new4 = scoped4(1, 2, 3, 4)
        val new5 = scoped5(1, 2, 3, 4, 5)

        initial0 shouldNotBeSameReference new0
        initial1 shouldNotBeSameReference new1
        initial2 shouldNotBeSameReference new2
        initial3 shouldNotBeSameReference new3
        initial4 shouldNotBeSameReference new4
        initial5 shouldNotBeSameReference new5
    }

    @Test
    fun `multiple clear actions can be registered that will be called for every instance in scope`() {
        var clearAction1CallCount = 0
        var clearAction2CallCount = 0

        val value0 = scoped0()
        val value1 = scoped1(1)

        val clearAction1: (Any?) -> Unit = { any: Any? ->
            clearAction1CallCount++
            any shouldBeSameReferenceAsAnyOf listOf(value0, value1)
        }
        val clearAction2: (Any?) -> Unit = {
            clearAction2CallCount++
        }

        scope.addOnClearAction(clearAction1)
        scope.addOnClearAction(clearAction2)
        scope.clear()
        scope.removeOnClearAction(clearAction1)
        scoped0()
        scoped1(1)
        scope.clear()

        clearAction1CallCount shouldBeEqualTo 2
        clearAction2CallCount shouldBeEqualTo 4
    }

    @Test
    fun `multiple global clear actions can be registered that will be called for every instance in scope`() {
        var clearAction1CallCount = 0
        var clearAction2CallCount = 0

        val value0 = scoped0()
        val value1 = scoped1(1)

        val clearAction1: (Any?) -> Unit = { any: Any? ->
            clearAction1CallCount++
            any shouldBeSameReferenceAsAnyOf listOf(value0, value1)
        }
        val clearAction2: (Any?) -> Unit = {
            clearAction2CallCount++
        }

        Shank.addScopedInstanceClearAction(clearAction1)
        Shank.addScopedInstanceClearAction(clearAction2)
        scope.clear()
        Shank.removeScopedInstanceClearAction(clearAction1)
        scoped0()
        scoped1(1)
        scope.clear()

        clearAction1CallCount shouldBeEqualTo 2
        clearAction2CallCount shouldBeEqualTo 4
    }
}
