package life.shank

import org.junit.Test
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureNanoTime


class PerformanceTest {

    val arrayHash = ConcurrentHashMap<Any, Any>()


    @Test
    fun arrayvspair() {
//        params().also { arrayHash[it] = true }.also { arrayHash[it] }
//        Pair(Any(), "no").also { arrayHash[it] = true }.also { arrayHash[it] }

        measureNanoTime {
            MyThing
        }
            .also { println(it/1000) }
    }

//    private inline fun params() = Any() mash "no"


    val hash = hashMapOf("yo" to 666)
}


object MyThing: ShankModule {
    @JvmField val a1 = new { -> "" }
}
