package life.shank

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import shouldBeEqualTo

suspend inline fun CoroutineScope.testConcurrentAccess(crossinline x: () -> Long) {
    (1..8).map { async { x() } }.map { it.await() }.toSet().size shouldBeEqualTo 1
}
