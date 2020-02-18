package life.shank

import life.shank.ParameterNewModule.new0
import life.shank.ParameterNewModule.new1
import life.shank.ParameterNewModule.new2
import life.shank.ParameterNewModule.new3
import life.shank.ParameterNewModule.new4
import life.shank.ParameterNewModule.new5
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo

private object ParameterNewModule : ShankModule {
    val new0 = new { -> DataForTest() }
    val new1 = new { a: Int -> DataForTest(a) }
    val new2 = new { a: Int, b: Int -> DataForTest(a, b) }
    val new3 = new { a: Int, b: Int, c: Int -> DataForTest(a, b, c) }
    val new4 = new { a: Int, b: Int, c: Int, d: Int -> DataForTest(a, b, c, d) }
    val new5 = new { a: Int, b: Int, c: Int, d: Int, e: Int -> DataForTest(a, b, c, d, e) }
}

class NewTests {

    @Before
    fun setUp() {
        resetShank()
    }

    @Test
    fun `can register factories for new instance with up to 5 parameters`() {
        new0() shouldBeEqualTo DataForTest()
        new1(1) shouldBeEqualTo DataForTest(1)
        new2(1, 2) shouldBeEqualTo DataForTest(1, 2)
        new3(1, 2, 3) shouldBeEqualTo DataForTest(1, 2, 3)
        new4(1, 2, 3, 4) shouldBeEqualTo DataForTest(1, 2, 3, 4)
        new5(1, 2, 3, 4, 5) shouldBeEqualTo DataForTest(1, 2, 3, 4, 5)
    }
}
