package com.memoizr

import com.memoizr.MyModule.listOfInts
import com.memoizr.MyModule.listOfStrings
import com.memoizr.MyModule.newFoo
import com.memoizr.MyModule.newWith1Params
import com.memoizr.MyModule.nullable1
import com.memoizr.MyModule.otherScopeSingleton
import com.memoizr.MyModule.scopedSingleton
import com.memoizr.MyModule.singleton
import com.memoizr.ParameterModule.fiveParamNew
import com.memoizr.ParameterModule.fiveParamScoped
import com.memoizr.ParameterModule.fiveParamSingleton
import com.memoizr.ParameterModule.fourParamNew
import com.memoizr.ParameterModule.fourParamScoped
import com.memoizr.ParameterModule.fourParamSingleton
import com.memoizr.ParameterModule.noParamNew
import com.memoizr.ParameterModule.noParamScoped
import com.memoizr.ParameterModule.noParamSingleton
import com.memoizr.ParameterModule.oneParamNew
import com.memoizr.ParameterModule.oneParamScoped
import com.memoizr.ParameterModule.oneParamSingleton
import com.memoizr.ParameterModule.threeParamNew
import com.memoizr.ParameterModule.threeParamScoped
import com.memoizr.ParameterModule.threeParamSingleton
import com.memoizr.ParameterModule.twoParamNew
import com.memoizr.ParameterModule.twoParamScoped
import com.memoizr.ParameterModule.twoParamSingleton
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo
import shouldBeInstanceOf
import shouldBeSameReference
import shouldNotBeEqualTo
import java.util.*

object MyModule : ShankModule() {
    val newFoo = new { -> MyClass("my value") }
    val listOfStrings = new { -> ArrayList<String>().also { it.add("one") } }
    val listOfInts = new { -> ArrayList<Int>().also { it.add(1) } }

    val scopedSingleton = scoped { -> OtherClass() }
    val otherScopeSingleton = scoped { -> MyClass("value") }

    val singleton = singleton { -> OtherClass() }

    val newWith1Params = new(::MyClass)

    val nullable = new { -> if ("" == "") MyClass("") else null }
    val nullable1 = new { s: String?, b: String-> if (s == "") MyClass("") else null }
}

class ShankTest : Scoped {
    override val scope = Scope("myscope")

    @Before
    fun setUp() {
        resetShank()
        nullable1(null, "")?.value
    }

    @Test
    fun `provides new value`() {
        newFoo().value shouldBeEqualTo "my value"
        newFoo() shouldNotBeEqualTo newFoo()
    }

    @Test
    fun `supports generics`() {
        listOfStrings()[0] shouldBeEqualTo "one"
        listOfInts()[0] shouldBeEqualTo 1
    }

    @Test
    fun `caches values for a specific scope`() {
        val scope = Scope("scope")
        scopedSingleton(scope) shouldBeEqualTo scopedSingleton(scope)
        otherScopeSingleton(scope) shouldBeInstanceOf MyClass::class
    }

    @Test
    fun `clears a specific scope`() {
        val scope = Scope("scope")
        val instance1 = scopedSingleton(scope)
        val instance2 = scopedSingleton(scope)
        instance1 shouldBeEqualTo instance2
        scope.clear()
        val instance3 = scopedSingleton(scope)
        instance1 shouldNotBeEqualTo instance3
        instance2 shouldNotBeEqualTo instance3

        val instance4 = scopedSingleton(scope)
        instance3 shouldBeEqualTo instance4
    }

    @Test
    fun `supports global scope`() {
        singleton() shouldBeEqualTo singleton()
    }

    @Test
    fun `automatically injects scope for scoped class`() {
        scopedSingleton() shouldBeEqualTo scopedSingleton()
    }

    @Test
    fun `clears scope with final action`() {
        otherScopeSingleton()

        scope.clearWithAction { wrapper, value ->
            when {
                wrapper == newFoo -> println(newFoo())
            }
        }
    }

    @Test
    fun `supports creation parameters`() {
        newWith1Params("hello").value shouldBeEqualTo "hello"
    }

    @Test
    fun `supports nested scoping`() {
//        println(ParamModule.myClass().value)
    }

    @Test
    fun `allows new override`() {
        noParamNew() shouldBeEqualTo ParamData(null)
        noParamNew.overrideFactory {  -> ParamData(2) }
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
        fourParamNew.overrideFactory { a: Int, b: Int, c: Int, d: Int -> ParamData(a * 2, b * 2, c * 2, d * 2) }
        fourParamNew(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)

        fiveParamNew(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamNew.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a * 2, b * 2, c * 2, d * 2, e * 2) }
        fiveParamNew(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
    }

    @Test
    fun `allows scoped override`() {
        noParamScoped() shouldBeEqualTo ParamData()
        noParamScoped.overrideFactory {  -> ParamData( 2) }
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
        fourParamScoped.overrideFactory { a: Int, b: Int, c: Int, d: Int -> ParamData(a * 2, b * 2, c * 2, d * 2) }
        fourParamScoped(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)
        fourParamScoped.restore()

        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamScoped.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a * 2, b * 2, c * 2, d * 2, e * 2) }
        fiveParamScoped(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
        fiveParamScoped.restore()
    }

    @Test
    fun `allows singleton override`() {
        noParamSingleton() shouldBeEqualTo ParamData()
        noParamSingleton.overrideFactory {  -> ParamData( 2) }
        noParamSingleton() shouldBeEqualTo ParamData(2)
        noParamSingleton.restore()

        oneParamSingleton(1) shouldBeEqualTo ParamData(1)
        oneParamSingleton.overrideFactory { a: Int -> ParamData(a * 2) }
        oneParamSingleton(1) shouldBeEqualTo ParamData(2)
        oneParamSingleton.restore()

        twoParamSingleton(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParamSingleton.overrideFactory { a: Int, b: Int -> ParamData(a * 2, b * 2) }
        twoParamSingleton(1, 2) shouldBeEqualTo ParamData(2, 4)
        twoParamSingleton.restore()

        threeParamSingleton(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParamSingleton.overrideFactory { a: Int, b: Int, c: Int -> ParamData(a * 2, b * 2, c * 2) }
        threeParamSingleton(1, 2, 3) shouldBeEqualTo ParamData(2, 4, 6)
        threeParamSingleton.restore()

        fourParamSingleton(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParamSingleton.overrideFactory { a: Int, b: Int, c: Int, d: Int -> ParamData(a * 2, b * 2, c * 2, d * 2) }
        fourParamSingleton(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)
        fourParamSingleton.restore()

        fiveParamSingleton(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamSingleton.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a * 2, b * 2, c * 2, d * 2, e * 2) }
        fiveParamSingleton(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
        fiveParamSingleton.restore()
    }

    @Test
    fun `supports parameters new`() {
        oneParamNew(1) shouldBeEqualTo ParamData(1)
        twoParamNew(1, 2) shouldBeEqualTo ParamData(1, 2)
        threeParamNew(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        fourParamNew(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fiveParamNew(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
    }

    @Test
    fun `supports parameters singleton`() {
        oneParamSingleton(1) shouldBeEqualTo ParamData(1)
        oneParamSingleton(1) shouldBeSameReference oneParamSingleton(1)

        twoParamSingleton(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParamSingleton(1, 2) shouldBeSameReference twoParamSingleton(1, 2)

        threeParamSingleton(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParamSingleton(1, 2, 3) shouldBeSameReference threeParamSingleton(1, 2, 3)

        fourParamSingleton(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParamSingleton(1, 2, 3, 4) shouldBeSameReference fourParamSingleton(1, 2, 3, 4)

        fiveParamSingleton(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParamSingleton(1, 2, 3, 4, 5) shouldBeSameReference fiveParamSingleton(1, 2, 3, 4, 5)
    }

    @Test
    fun `supports parameters scoped`() {
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
    fun `singleton provides different values per params`() {
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
}

object ParameterModule : ShankModule() {
    val noParamNew = new { -> ParamData() }
    val oneParamNew = new { a: Int -> ParamData(a) }
    val twoParamNew = new { a: Int, b: Int -> ParamData(a, b) }
    val threeParamNew = new { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParamNew = new { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParamNew = new { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }

    val noParamSingleton = singleton { -> ParamData() }
    val oneParamSingleton = singleton { a: Int -> ParamData(a) }
    val twoParamSingleton = singleton { a: Int, b: Int -> ParamData(a, b) }
    val threeParamSingleton = singleton { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParamSingleton = singleton { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParamSingleton = singleton { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }

    val noParamScoped = scoped { -> ParamData() }
    val oneParamScoped = scoped { a: Int -> ParamData(a) }
    val twoParamScoped = scoped { a: Int, b: Int -> ParamData(a, b) }
    val threeParamScoped = scoped { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParamScoped = scoped { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParamScoped = scoped { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }
}


class OtherClass
class MyClass(val value: String)

data class ParamData(val a: Int? = null, val b: Int? = null, val c: Int? = null, val d: Int? = null, val e: Int? = null)
