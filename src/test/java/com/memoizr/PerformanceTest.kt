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
//            hashCode()
//            hashCode()
//            hashCode()
//            hashCode()
//            hashCode()
//            hashCode()
//            hashCode()
//        })
//        println(measureNanoTime {
//            val x = System.nanoTime().toInt()
//            val y = System.nanoTime().toInt()
//            val z = System.nanoTime()
//            System.nanoTime()
//            System.nanoTime()
//            System.nanoTime()
//            System.nanoTime()
//            println(x)
//            println(y)
//            println(z)
//        })

        val x = measureTime {
            fib8()
        }

        println(x)
//        val dfe = Caster.cast<Int, String>(3)

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

