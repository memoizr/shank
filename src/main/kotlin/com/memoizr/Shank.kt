package com.memoizr

import com.memoizr.ShankCache.globalScope
import com.memoizr.ShankCache.scopedCache

internal object ShankCache {
    internal val factories = HashMap<Provider<*>, Any>()
    internal val scopedCache = HashMap<Scope, MutableMap<Pair<Provider<*>, Params>, Any?>>()
    internal val globalScope = Scope(hashCode().toString())
}

fun resetShank() {
    scopedCache.clear()
    globalScope.clear()
}

internal interface Params
internal object Params0: Params
internal data class Params1(val a: Any?): Params
internal data class Params2(val a: Any?, val b: Any?): Params
internal data class Params3(val a: Any?, val b: Any?, val c: Any?): Params
internal data class Params4(val a: Any?, val b: Any?, val c: Any?, val d: Any?): Params
internal data class Params5(val a: Any?, val b: Any?, val c: Any?, val d: Any?, val e: Any?): Params

fun registerModules(vararg modules: ShankModule) {
    resetShank()
    modules.forEach {
        it.registerFactories()
    }
}
