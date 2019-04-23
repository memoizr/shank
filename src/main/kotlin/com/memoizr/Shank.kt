package com.memoizr

import com.memoizr.ShankCache.globalScope
import com.memoizr.ShankCache.scopedCache
import java.util.concurrent.ConcurrentHashMap

object ShankFactoryCache {
    val factories = ConcurrentHashMap<Long, Any>()
}

internal object OverriddenCache {
    @JvmStatic
    internal val factories = ConcurrentHashMap<Long, Any>()
}

internal object ShankCache {
    @JvmStatic
    internal val globalScope = Scope(hashCode())
    @JvmStatic
    internal val scopedCache = ConcurrentHashMap<Scope, MutableMap<Pair<Long, Params>, Any?>>()
}

fun resetShank() {
    scopedCache.clear()
    globalScope.clear()
    OverriddenCache.factories
        .forEach {
            ShankFactoryCache.factories[it.key] = it.value
            OverriddenCache.factories.remove(it.key)
        }
}

