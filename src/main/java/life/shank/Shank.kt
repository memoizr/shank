package life.shank

import life.shank.ShankCache.globalScope
import life.shank.ShankCache.scopedCache
import java.util.concurrent.ConcurrentHashMap

object _cache {
    @JvmField val factories  = Mapppp<Any>()
}

internal object OverriddenCache {
    @JvmField internal val factories = ConcurrentHashMap<Provider<*, *>, Any>()
}

internal object ShankCache {
    @JvmField internal val globalScope = Scope(System.identityHashCode(this))
    @JvmField internal val scopedCache = ConcurrentHashMap<Scope, Mapppp<Any?>>()
}

fun resetShank() {
    OverriddenCache.factories
        .forEach {
            _cache.factories.put(it.key, it.value)
        }
    OverriddenCache.factories.clear()
    scopedCache.clear()
    globalScope.clear()
}

