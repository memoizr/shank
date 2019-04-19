package com.memoizr

import com.memoizr.ParameterSingletonModule.fiveParam
import com.memoizr.ParameterSingletonModule.fourParam
import com.memoizr.ParameterSingletonModule.noParam
import com.memoizr.ParameterSingletonModule.oneParam
import com.memoizr.ParameterSingletonModule.threeParam
import com.memoizr.ParameterSingletonModule.twoParam
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo
import shouldBeSameReference

private object ParameterSingletonModule : ShankModule() {
    val noParam = singleton { -> ParamData() }
    val oneParam = singleton { a: Int -> ParamData(a) }
    val twoParam = singleton { a: Int, b: Int -> ParamData(a, b) }
    val threeParam = singleton { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParam = singleton { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParam = singleton { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }
}

class SingletonTests {

    @Before
    fun setUp() {
        resetShank()
    }

    @Test
    fun `supports parameters`() {
        oneParam(1) shouldBeEqualTo ParamData(1)
        oneParam(1) shouldBeSameReference oneParam(1)

        twoParam(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParam(1, 2) shouldBeSameReference twoParam(1, 2)

        threeParam(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParam(1, 2, 3) shouldBeSameReference threeParam(1, 2, 3)

        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParam(1, 2, 3, 4) shouldBeSameReference fourParam(1, 2, 3, 4)

        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParam(1, 2, 3, 4, 5) shouldBeSameReference fiveParam(1, 2, 3, 4, 5)
    }

    @Test
    fun `provides different values per params`() {
        oneParam(1) shouldBeSameReference oneParam(1)
        oneParam(2).a shouldBeEqualTo 2

        twoParam(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParam(2, 3) shouldBeEqualTo ParamData(2, 3)

        threeParam(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParam(2, 3, 4) shouldBeEqualTo ParamData(2, 3, 4)

        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParam(2, 3, 4, 5) shouldBeEqualTo ParamData(2, 3, 4, 5)

        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParam(2, 3, 4, 5, 6) shouldBeEqualTo ParamData(2, 3, 4, 5, 6)
    }


    @Test
    fun `allows override`() {
        noParam() shouldBeEqualTo ParamData()
        noParam.overrideFactory { -> ParamData(2) }
        noParam() shouldBeEqualTo ParamData(2)
        noParam.restore()

        oneParam(1) shouldBeEqualTo ParamData(1)
        oneParam.overrideFactory { a: Int -> ParamData(a * 2) }
        oneParam(1) shouldBeEqualTo ParamData(2)
        oneParam.restore()

        twoParam(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParam.overrideFactory { a: Int, b: Int -> ParamData(a * 2, b * 2) }
        twoParam(1, 2) shouldBeEqualTo ParamData(2, 4)
        twoParam.restore()

        threeParam(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParam.overrideFactory { a: Int, b: Int, c: Int -> ParamData(a * 2, b * 2, c * 2) }
        threeParam(1, 2, 3) shouldBeEqualTo ParamData(2, 4, 6)
        threeParam.restore()

        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParam.overrideFactory { a: Int, b: Int, c: Int, d: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2
            )
        }
        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)
        fourParam.restore()

        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParam.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2,
                e * 2
            )
        }
        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
        fiveParam.restore()
    }
}
