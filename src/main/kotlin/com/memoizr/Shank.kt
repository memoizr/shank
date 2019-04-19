package com.memoizr

import com.memoizr.ShankCache.globalScope
import com.memoizr.ShankCache.scopedCache

internal object ShankCache {
    internal val factories = HashMap<Provider<*,*>, Any>()
    internal val scopedCache = HashMap<Scope, MutableMap<Pair<Provider<*, *>, Params>, Any?>>()
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
