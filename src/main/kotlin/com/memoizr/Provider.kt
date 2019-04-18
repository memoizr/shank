package com.memoizr

import com.memoizr.ShankCache.factories
import java.util.concurrent.ConcurrentHashMap

abstract class Provider<T>(private val factory: Function<T>) {
    init {
        ShankCache.factories[this] = factory as Function<Any>
    }

    protected fun <T> Any?.invokes() = (this!! as Function0<T>).invoke()
    protected fun <A, T> Any?.invokes(a: A) = (this!! as Function1<A, T>).invoke(a)
    protected fun <A, B, T> Any?.invokes(a: A, b: B) = (this!! as Function2<A, B, T>).invoke(a, b)
    protected fun <A, B, C, T> Any?.invokes(a: A, b: B, c: C) = (this!! as Function3<A, B, C, T>).invoke(a, b, c)
    protected fun <A, B, C, D, T> Any?.invokes(a: A, b: B, c: C, d: D) =
        (this!! as Function4<A, B, C, D, T>).invoke(a, b, c, d)

    protected fun <A, B, C, D, E, T> Any?.invokes(a: A, b: B, c: C, d: D, e: E) =
        (this!! as Function5<A, B, C, D, E, T>).invoke(a, b, c, d, e)

    protected fun <T> Any?.invokescoped(scope: ScopedFactory) = (this!! as ScopedFactory.() -> T).invoke(scope)
    protected fun <A, T> Any?.invokescoped(scope: ScopedFactory, a: A) =
        (this!! as ScopedFactory.(A) -> T).invoke(scope, a)

    protected fun <A, B, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B) =
        (this!! as ScopedFactory.(A, B) -> T).invoke(scope, a, b)

    protected fun <A, B, C, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B, c: C) =
        (this!! as ScopedFactory.(A, B, C) -> T).invoke(scope, a, b, c)

    protected fun <A, B, C, D, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B, c: C, d: D) =
        (this!! as ScopedFactory.(A, B, C, D) -> T).invoke(scope, a, b, c, d)

    protected fun <A, B, C, D, E, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B, c: C, d: D, e: E) =
        (this!! as ScopedFactory.(A, B, C, D, E) -> T).invoke(scope, a, b, c, d, e)


    internal fun getScope(scope: Scope): MutableMap<Pair<Provider<out Any>, Params>, Any>? = ShankCache.scopedCache[scope]

    internal fun remove() {
        ShankCache.scopedCache.forEach { scope ->
            scope.value.forEach { it ->
                if (it.key.first == this) {
                    scope.value.remove(it.key)
                }
            }
        }
    }


    fun restore() {
        factories[this] = factory as Function<Any>
    }

    internal fun get(scope: Scope, params: Params = Params0, f: Any?.() -> T): T {
        fun value() = ShankCache.factories[this].f()

        if (getScope(scope) == null) {
            ShankCache.scopedCache[scope] = ConcurrentHashMap()
        }

        return (getScope(scope)!!.let { newScope ->
            val pair = Pair(this as Provider<out Any>, params)
            newScope[pair] ?: value().also {
                newScope[pair] = it as Any
            }
        }) as T
    }
}
