package com.memoizr

import com.memoizr.ShankCache.globalScope
import com.memoizr.ShankCache.scopedCache
import java.util.concurrent.ConcurrentHashMap

object _cache {
    @JvmStatic
    val factories: ConcurrentHashMap<Provider<*, *>, Any> = ConcurrentHashMap(24)
}

internal object OverriddenCache {
    @JvmStatic
    internal val factories = ConcurrentHashMap<Provider<*,*>, Any>()
}

internal object ShankCache {
    @JvmStatic
    internal val globalScope = Scope(hashCode())
    @JvmStatic
    internal val scopedCache = ConcurrentHashMap<Scope, MutableMap<Pair<Provider<*,*>, Params>, Any?>>()
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

