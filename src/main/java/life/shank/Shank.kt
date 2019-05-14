package life.shank

import life.shank.ShankScopedCache.globalScope
import life.shank.ShankScopedCache.scopedCache
import java.util.concurrent.ConcurrentHashMap

object _cache {
    @JvmField val factories: HashcodeHashMap<Any> = HashcodeHashMap()
}

internal object OverriddenCache {
    @JvmField
    internal val factories = ConcurrentHashMap<Provider<*, *>, Any>()
}

object ShankGlobalCache {
    @JvmField val globalCache: HashcodeHashMap<Any> = HashcodeHashMap()
}

internal object ShankScopedCache {
    @JvmField
    internal val globalScope = Scope(this.hashCode())
    @JvmField
    internal val scopedCache = HashcodeHashMap<HashcodeHashMap<Any?>>()
    @JvmField
    internal val globalOnClearActions = HashSet<(Any?) -> Unit>()
}

fun resetShank() {
    OverriddenCache.factories
        .forEach {
            _cache.factories.put(it.key.hashCode(), it.value)
        }
    OverriddenCache.factories.clear()
    scopedCache.clear()
    globalScope.clear()
}

