package com.memoizr

import com.memoizr.MyModule.newFoo
import com.memoizr.ParameterScopedModule.fiveParamScoped
import com.memoizr.ParameterScopedModule.fourParamScoped
import com.memoizr.ParameterScopedModule.noParamScoped
import com.memoizr.ParameterScopedModule.oneParamScoped
import com.memoizr.ParameterScopedModule.threeParamScoped
import com.memoizr.ParameterScopedModule.twoParamScoped
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo
import shouldBeSameReference


private object ParameterScopedModule : ShankModule() {
    val noParamScoped = scoped { -> ParamData() }
    val oneParamScoped = scoped { a: Int -> ParamData(a) }
    val twoParamScoped = scoped { a: Int, b: Int -> ParamData(a, b) }
    val threeParamScoped = scoped { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParamScoped = scoped { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParamScoped = scoped { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }
}

class ScopedTests: Scoped {
    override val scope: Scope = Scope("singleton")

    @Before
    fun setUp() {
        resetShank()
    }

    @Test
    fun `supports parameters`() {
        oneParamScoped(1) shouldBeEqualTo ParamData(1)
        oneParamScoped(1) shouldBeSameReference oneParamScoped(1)

        twoParamScoped(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParamScoped(1, 2) shouldBeSameReference twoParamScoped(1, 2)

        threeParamScoped(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParamScoped(1, 2, 3) shouldBeSameReference threeParamScoped(1, 2, 3)

        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParamScoped(1, 2, 3, 4) shouldBeSameReference fourParamScoped(1, 2, 3, 4)

        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamScoped(1, 2, 3, 4, 5) shouldBeSameReference fiveParamScoped(1, 2, 3, 4, 5)
    }

    @Test
    fun `allows override`() {
        noParamScoped() shouldBeEqualTo ParamData()
        noParamScoped.overrideFactory { -> ParamData(2) }
        noParamScoped() shouldBeEqualTo ParamData(2)
        noParamScoped.restore()

        oneParamScoped(1) shouldBeEqualTo ParamData(1)
        oneParamScoped.overrideFactory { a: Int -> ParamData(a * 2) }
        oneParamScoped(1) shouldBeEqualTo ParamData(2)
        oneParamScoped.restore()

        twoParamScoped(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParamScoped.overrideFactory { a: Int, b: Int -> ParamData(a * 2, b * 2) }
        twoParamScoped(1, 2) shouldBeEqualTo ParamData(2, 4)
        twoParamScoped.restore()

        threeParamScoped(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParamScoped.overrideFactory { a: Int, b: Int, c: Int -> ParamData(a * 2, b * 2, c * 2) }
        threeParamScoped(1, 2, 3) shouldBeEqualTo ParamData(2, 4, 6)
        threeParamScoped.restore()

        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParamScoped.overrideFactory { a: Int, b: Int, c: Int, d: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2
            )
        }
        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)
        fourParamScoped.restore()

        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamScoped.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2,
                e * 2
            )
        }
        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
        fiveParamScoped.restore()
    }

    @Test
    fun `provides different values per params`() {
        oneParamScoped(1) shouldBeSameReference oneParamScoped(1)
        oneParamScoped(2).a shouldBeEqualTo 2

        twoParamScoped(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParamScoped(2, 3) shouldBeEqualTo ParamData(2, 3)

        threeParamScoped(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParamScoped(2, 3, 4) shouldBeEqualTo ParamData(2, 3, 4)

        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParamScoped(2, 3, 4, 5) shouldBeEqualTo ParamData(2, 3, 4, 5)

        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamScoped(2, 3, 4, 5, 6) shouldBeEqualTo ParamData(2, 3, 4, 5, 6)
    }


    @Test
    fun `clears scope with final action`() {
        noParamScoped()

        scope.clearWithAction { wrapper, value ->
            when {
                wrapper == newFoo -> println(newFoo())
            }
        }
    }

}
