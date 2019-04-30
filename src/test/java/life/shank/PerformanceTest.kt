package life.shank

import org.junit.Test
import kotlin.system.measureNanoTime


class PerformanceTest {

    val arrayHash = Mapppp<Any>()


    @Test
    fun arrayvspair() {
        val a = Any()
        println(
            measureNanoTime {
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
                a.hashCode()
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
//                System.identityHashCode(a)
            }
               )
    }

    val hash = hashMapOf("yo" to 666)
}


object MyThing : ShankModule {
    @JvmField
    val a1 = new { -> "" }
}
