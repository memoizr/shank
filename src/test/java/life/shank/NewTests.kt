package life.shank

import life.shank.ParameterNewModule.fiveParamNew
import life.shank.ParameterNewModule.fourParamNew
import life.shank.ParameterNewModule.noParamNew
import life.shank.ParameterNewModule.oneParamNew
import life.shank.ParameterNewModule.threeParamNew
import life.shank.ParameterNewModule.twoParamNew
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo

private object ParameterNewModule : ShankModule {
    val noParamNew = new { -> ParamData() }
    val oneParamNew = new { a: Int -> ParamData(a) }
    val twoParamNew = new { a: Int, b: Int -> ParamData(a, b) }
    val threeParamNew = new { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParamNew = new { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParamNew = new { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }
}

class NewTests {

    @Before
    fun setUp() {
        resetShank()
    }

    @Test
    fun `supports parameters new`() {
        noParamNew() shouldBeEqualTo ParamData()
        oneParamNew(1) shouldBeEqualTo ParamData(1)
        twoParamNew(1, 2) shouldBeEqualTo ParamData(1, 2)
        threeParamNew(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        fourParamNew(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fiveParamNew(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
    }

    @Test
    fun `allows new override`() {
        noParamNew() shouldBeEqualTo ParamData(null)
        noParamNew.overrideFactory { -> ParamData(2) }
        noParamNew() shouldBeEqualTo ParamData(2)

        oneParamNew(1) shouldBeEqualTo ParamData(1)
        oneParamNew.overrideFactory { a: Int -> ParamData(a * 2) }
        oneParamNew(1) shouldBeEqualTo ParamData(2)

        twoParamNew(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParamNew.overrideFactory { a: Int, b: Int -> ParamData(a * 2, b * 2) }
        twoParamNew(1, 2) shouldBeEqualTo ParamData(2, 4)

        threeParamNew(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParamNew.overrideFactory { a: Int, b: Int, c: Int -> ParamData(a * 2, b * 2, c * 2) }
        threeParamNew(1, 2, 3) shouldBeEqualTo ParamData(2, 4, 6)

        fourParamNew(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParamNew.overrideFactory { a: Int, b: Int, c: Int, d: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2
            )
        }
        fourParamNew(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)

        fiveParamNew(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamNew.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2,
                e * 2
            )
        }
        fiveParamNew(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
    }
}
