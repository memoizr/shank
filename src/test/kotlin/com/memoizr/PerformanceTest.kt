package com.memoizr

import com.memoizr.Module1.fib8
import org.junit.Test
import kotlin.system.measureNanoTime


class PerformanceTest {

    @Test
    fun `performance`() {
        val register= (1..100).map {
            measureTime {
                registerModules(Module1)
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

