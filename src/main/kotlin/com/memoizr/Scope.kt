package com.memoizr

import java.io.Serializable

data class Scope(val value: String) : Serializable

data class ScopedFactory(override val scope: Scope) : Scoped

fun Scope.clear() { ShankCache.scopedCache.remove(this) }

fun Scope.clearWithAction(action: (Provider<*>, Any) -> Unit) {
    ShankCache.scopedCache
        .also {
            it[this]?.forEach {
                action(it.key.first, it.value)
            }
        }
        .remove(this)
}


interface Scoped {
    val scope: Scope

    operator fun <T : Any> ScopedProvider<T>.invoke() = this(scope)
    operator fun <A : Any, T : Any> ScopedProvider1<A, T>.invoke(a: A) = this(scope, a)
    operator fun <A : Any, B : Any, T : Any> ScopedProvider2<A, B, T>.invoke(a: A, b: B) = this(scope, a, b)
    operator fun <A : Any, B : Any, C : Any, T : Any> ScopedProvider3<A, B, C, T>.invoke(a: A, b: B, c: C) = this(scope, a, b, c)
    operator fun <A : Any, B : Any, C : Any, D : Any, T : Any> ScopedProvider4<A, B, C, D, T>.invoke( a: A, b: B, c: C, d: D ) = this(scope, a, b, c, d)
    operator fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> ScopedProvider5<A, B, C, D, E, T>.invoke( a: A, b: B, c: C, d: D, e: E ) = this(scope, a, b, c, d, e)
}
