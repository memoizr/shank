package com.memoizr

import com.memoizr.ShankCache.globalScope
import com.memoizr.ShankCache.scopedCache
import java.util.concurrent.ConcurrentHashMap

internal object ShankCache {
    internal val factories = ConcurrentHashMap<Provider<*,*>, Any>()
    internal val scopedCache = ConcurrentHashMap<Scope, MutableMap<Pair<Provider<*, *>, Params>, Any?>>()
    internal val globalScope = Scope(hashCode().toString())
}

fun resetShank() {
    scopedCache.clear()
    globalScope.clear()
}

fun registerModules(vararg modules: ShankModule) {
    resetShank()
    modules.forEach {
        it.registerFactories()
    }
}
