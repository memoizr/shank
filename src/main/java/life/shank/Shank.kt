package life.shank

import life.shank.ShankCache.globalScope
import life.shank.ShankCache.scopedCache
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object _cache {
    @JvmField
    val factories: ConcurrentHashMap<Provider<*, *>, Any> = ConcurrentHashMap(32)
}

internal object OverriddenCache {
    @JvmField
    internal val factories = ConcurrentHashMap<Provider<*, *>, Any>()
}

internal object ShankCache {
    @JvmField
    internal val globalScope = Scope(hashCode())
    @JvmField
    internal val scopedCache = ConcurrentHashMap<Scope, ConcurrentHashMap<Long, Any?>>()
}

fun resetShank() {
    OverriddenCache.factories
        .forEach {
            _cache.factories[it.key] = it.value
        }
    OverriddenCache.factories.clear()
    scopedCache.clear()
    globalScope.clear()
}

