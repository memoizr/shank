package life.shank

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import life.shank.ParameterSingletonModule.single0
import life.shank.ParameterSingletonModule.single1
import life.shank.ParameterSingletonModule.single2
import life.shank.ParameterSingletonModule.single3
import life.shank.ParameterSingletonModule.single4
import life.shank.ParameterSingletonModule.single5
import org.junit.Test
import shouldBeEqualTo
import shouldBeSameReference
import shouldNotBeSameReference
import testConcurrentSingletonGet

private object ParameterSingletonModule : ShankModule {
    val single0 = single { -> DataForTest() }
    val single1 = single { a: Int -> DataForTest(a) }
    val single2 = single { a: Int, b: Int -> DataForTest(a, b) }
    val single3 = single { a: Int, b: Int, c: Int -> DataForTest(a, b, c) }
    val single4 = single { a: Int, b: Int, c: Int, d: Int -> DataForTest(a, b, c, d) }
    val single5 = single { a: Int, b: Int, c: Int, d: Int, e: Int -> DataForTest(a, b, c, d, e) }
}

class SingletonTests {

    @Test
    fun `can register factories for singleton instance with up to 5 parameters`() {
        single0() shouldBeEqualTo DataForTest()
        single0() shouldBeSameReference single0()

        single1(1) shouldBeEqualTo DataForTest(1)
        single1(1) shouldBeSameReference single1(1)

        single2(1, 2) shouldBeEqualTo DataForTest(1, 2)
        single2(1, 2) shouldBeSameReference single2(1, 2)

        single3(1, 2, 3) shouldBeEqualTo DataForTest(1, 2, 3)
        single3(1, 2, 3) shouldBeSameReference single3(1, 2, 3)

        single4(1, 2, 3, 4) shouldBeEqualTo DataForTest(1, 2, 3, 4)
        single4(1, 2, 3, 4) shouldBeSameReference single4(1, 2, 3, 4)

        single5(1, 2, 3, 4, 5) shouldBeEqualTo DataForTest(1, 2, 3, 4, 5)
        single5(1, 2, 3, 4, 5) shouldBeSameReference single5(1, 2, 3, 4, 5)
    }

    @Test
    fun `different parameters will create new singleton for those parameters`() {
        single1(1) shouldNotBeSameReference single1(2)
        single2(1, 2) shouldNotBeSameReference single2(2, 4)
        single3(1, 2, 3) shouldNotBeSameReference single3(2, 4, 6)
        single4(1, 2, 3, 4) shouldNotBeSameReference single4(2, 4, 6, 8)
        single5(1, 2, 3, 4, 5) shouldNotBeSameReference single5(2, 4, 6, 8, 10)
    }

    @Test
    fun `supports concurrent and suspend requests`() = runBlocking(Dispatchers.Default) {
        testConcurrentSingletonGet { single0() }
        testConcurrentSingletonGet { single1(1) }
        testConcurrentSingletonGet { single2(1, 2) }
        testConcurrentSingletonGet { single3(1, 2, 3) }
        testConcurrentSingletonGet { single4(1, 2, 3, 4) }
        testConcurrentSingletonGet { single5(1, 2, 3, 4, 5) }
    }
}

