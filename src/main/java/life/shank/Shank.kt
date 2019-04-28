package life.shank

import life.shank.ShankCache.globalScope
import life.shank.ShankCache.scopedCache
import java.util.concurrent.ConcurrentHashMap

object _cache {
    @JvmField
    val factories: ConcurrentHashMap<Provider<*, *>, Any> = ConcurrentHashMap(32)
}

internal object OverriddenCache {
    @JvmStatic
    internal val factories = ConcurrentHashMap<Provider<*, *>, Any>()
}

internal object ShankCache {
    @JvmStatic
    internal val globalScope = Scope(hashCode())
    @JvmStatic
    internal val scopedCache = ConcurrentHashMap<Scope, ConcurrentHashMap<Pair<Provider<*, *>, Params>, Any?>>()
}

fun resetShank() {
    scopedCache.clear()
    globalScope.clear()
    OverriddenCache.factories
        .forEach {
            _cache.factories[it.key] = it.value
            OverriddenCache.factories.remove(it.key)
        }
}

