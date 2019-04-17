package com.memoizr

import java.util.HashMap

internal object ShankCache {
    internal val factories = HashMap<Wrapper<*>, Any>()
    internal val scopedCache = HashMap<Scope, HashMap<Wrapper<out Any>, Any>>()
    internal val globalScope = Scope(hashCode().toString())
}

fun registerModules(vararg modules: ShankModule) = modules
