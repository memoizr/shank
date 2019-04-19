package com.memoizr

import java.io.Serializable

data class Scope(val value: Serializable) : Serializable {

    fun clear() {
        ShankCache.scopedCache.remove(this)
    }

    fun clearWithAction(action: (Provider<*, *>, Any?) -> Unit) {
        ShankCache.scopedCache.also { it[this]?.forEach { action(it.key.first, it.value) } }.remove(this)
    }
}

data class ScopedFactory(override val scope: Scope) : Scoped

interface Scoped {
    val scope: Scope

    operator fun <T> ScopedProvider<T>.invoke() = this(scope)
    operator fun <A, T> ScopedProvider1<A, T>.invoke(a: A) = this(scope, a)
    operator fun <A, B, T> ScopedProvider2<A, B, T>.invoke(a: A, b: B) = this(scope, a, b)
    operator fun <A, B, C, T> ScopedProvider3<A, B, C, T>.invoke(a: A, b: B, c: C) = this(scope, a, b, c)
    operator fun <A, B, C, D, T> ScopedProvider4<A, B, C, D, T>.invoke(a: A, b: B, c: C, d: D) = this(scope, a, b, c, d)
    operator fun <A, B, C, D, E, T> ScopedProvider5<A, B, C, D, E, T>.invoke(a: A, b: B, c: C, d: D, e: E) = this(scope, a, b, c, d, e)
}
