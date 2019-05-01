package life.shank

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import life.shank.MyThing.a1
import life.shank.MyThing2.a2
import org.junit.Test
import kotlin.system.measureNanoTime

class PerformanceTest {


    @Test
    fun arrayvspair() {
        MyModule.newFoo()

        println(measureNanoTime {
            a1()
        })
        println(measureNanoTime {
            a2()
        })
        println(measureNanoTime {
            a2()
        })
        println(measureNanoTime {
            a1()
        })
        println(measureNanoTime {
            a2()
        })
        println(measureNanoTime {
            a1()
        })
        println(measureNanoTime {
            a2()
        })
        println(measureNanoTime {
            a1()
        })

    }

    val hash = hashMapOf("yo" to 666)

    fun yo() {
        a1()
    }

    fun yo2() {
        MyModule.newFoo()
    }
}


object MyThing: X {
    val a1 = new0 { -> "cuck you" }
    val a2 = new0 { -> "cuck you" }
    val a3 = new0 { -> "cuck you" }
    val a4 = new0 { -> "cuck you" }
    val a5 = new0 { -> "cuck you" }
    val a6 = new0 { -> "cuck you" }
    val a7 = new0 { -> "cuck you" }
    val a8 = new0 { -> "cuck you" }
}

object MyThing2 : ShankModule {
    val a2 = new { -> "fuck you" }
    val a3 = new { -> "fuck you" }
    val a4 = new { -> "fuck you" }
    val a5 = new { -> "fuck you" }
    val a6 = new { -> "fuck you" }
    val a7 = new { -> "fuck you" }
    val a8 = new { -> "fuck you" }
    val a9 = new { -> "fuck you" }
}

interface X

inline fun <T> X.new0(crossinline b: () -> T): New0<T> {
    return object : New0<T> {
        override fun override(f: () -> T): New0<T> = apply { o = f }
        var o: (() -> T)? = null
        inline override fun invoke(): T = o?.invoke() ?: b()
    }
}

interface New0<T> {
    fun override(f: () -> T): New0<T>
    operator fun invoke(): T
}

class machine<T> {
    lateinit var x: Deferred<T>


    suspend inline fun process(block: () -> T = { null as T }): machine<T> {
        return this
    }

    fun invoke() = runBlocking { x.await() }
}
