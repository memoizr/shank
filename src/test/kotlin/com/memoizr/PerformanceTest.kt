package com.memoizr

import com.memoizr.Module1.fib8
import org.junit.Test
import kotlin.system.measureNanoTime


class PerformanceTest {
    object Moo {
//        val a = System.nanoTime()
//        val b = System.nanoTime()
//        val c = System.nanoTime()
//        val d = System.nanoTime()
//        val e = System.nanoTime()
//        val f = System.nanoTime()
//        val g = System.nanoTime()
    }

    @Test
    fun SimpleP() {
//        println(measureNanoTime {
//            val x = AtomicInteger()
//            x.incrementAndGet()
//            x.incrementAndGet()
//            x.incrementAndGet()
//            x.incrementAndGet()
//            x.incrementAndGet()
//            x.incrementAndGet()
//            x.incrementAndGet()
//        })
//        println(measureNanoTime {
////            val a = System.nanoTime()
////            val b = System.nanoTime()
////            val c = System.nanoTime()
////            val d = System.nanoTime()
////            val e = System.nanoTime()
////            val f = System.nanoTime()
////            val g = System.nanoTime()
//        })

        val x = measureTime {
            fib8()
        }

        println(x)

    }

    @Test
    fun `performance`() {
        val register = (1..100).map {
            measureTime {
                //                registerModules(Module1)
            }
        }.median()

        val get = (1..10).map {
            measureTime {
                fib8().number()
            }
        }.median()

        println(register)
        println(get)
    }

    fun List<Double>.median() = sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
    fun measureTime(block: () -> Unit): Double = measureNanoTime(block) / 1000000.0
}

