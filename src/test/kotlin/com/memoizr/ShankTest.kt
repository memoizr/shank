package com.memoizr

import com.memoizr.MyModule.listOfInts
import com.memoizr.MyModule.listOfStrings
import com.memoizr.MyModule.newFoo
import com.memoizr.MyModule.newWith1Params
import com.memoizr.MyModule.otherScopeSingleton
import com.memoizr.MyModule.scopedSingleton
import com.memoizr.MyModule.singleton
import com.memoizr.ParamModule.string
import org.junit.Before
import org.junit.Test
import shouldBeEqualTo
import shouldBeInstanceOf
import shouldNotBeEqualTo
import java.util.*


object ParamModule : ShankModule() {
    lateinit var string: String

    val myClass = new { -> MyClass(string) }
}

object MyModule : ShankModule() {
    val newFoo = new { -> MyClass("my value") }
    val listOfStrings = new { -> ArrayList<String>().also { it.add("one") } }
    val listOfInts = new { -> ArrayList<Int>().also { it.add(1) } }

    val scopedSingleton = scoped { -> OtherClass() }
    val otherScopeSingleton = scoped { -> MyClass("value") }

    val singleton = singleton { -> OtherClass() }

    val newWith1Params = new(::MyClass)

    val nestedScope = scoped { a: String, b: String -> MyClass(otherScopeSingleton().value) }
}

class ShankTest : Scoped {
    override val scope = Scope("myscope")

    @Before
    fun setup() {
        registerModules(MyModule, ParamModule { string = "booo" })
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
        println(ParamModule.myClass().value)
    }
}


class OtherClass
class MyClass(val value: String)
