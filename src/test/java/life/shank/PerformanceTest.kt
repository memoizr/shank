package life.shank

import life.shank.Module1.fib8
import org.junit.Test
import kotlin.system.measureNanoTime


class PerformanceTest {
    class Egg(chicken: Chicken)
    class Chicken(god: God)
    class God(human: Human)
    class Human(plant: Plant)
    class Plant(eggshell: EggShell?)
    class EggShell(egg: Egg)

    object a : ShankModule {
//        val egg = new { -> Egg(chicken()) }
//        val chicken = new { -> Chicken(god()) }
//        val god = new { -> God(human()) }
//        val human = new { -> Human(plant()) }
//        val plant = new { -> Plant(eggshell()) }
    }

    object b: ShankModule {
//        val eggshell = new { -> EggShell(egg()) }
    }

    @Test
    fun SimpleP() {

        val x = measureTime {
            fib8()
        }

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

