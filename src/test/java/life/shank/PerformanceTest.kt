package life.shank

import org.junit.Test
import kotlin.system.measureNanoTime


class PerformanceTest {

    val arrayHash = Mapppp<Any>()


    @Test
    fun arrayvspair() {
        println(
            measureNanoTime {
                Mapppp<Any>()
            }
               )
    }

    val hash = hashMapOf("yo" to 666)
}


object MyThing : ShankModule {
    @JvmField
    val a1 = new { -> "" }
}
