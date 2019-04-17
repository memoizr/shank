package com.memoizr

import java.io.Serializable

data class Scope(val value: String) : Serializable

data class ScopedFactory(override val scope: Scope) : Scoped

fun Scope.clear() = ShankCache.scopedCache.remove(this)
fun Scope.clearWithAction(action: (Wrapper<*>, Any) -> Unit) = ShankCache.scopedCache
    .also {
        it[this]?.forEach {
            action(it.key, it.value)
        }
    }
    .remove(this)


interface Scoped {
    val scope: Scope

    operator fun <T : Any> ScopedSingletonProviderWrapper<T>.invoke() = this(scope)
}
