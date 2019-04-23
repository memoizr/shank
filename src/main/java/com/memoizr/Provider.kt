package com.memoizr

import com.memoizr.`_cache`.factories
import java.util.concurrent.ConcurrentHashMap

internal interface Params
internal object Params0 : Params
internal data class Params1(val a: Any?) : Params
internal data class Params2(val a: Any?, val b: Any?) : Params
internal data class Params3(val a: Any?, val b: Any?, val c: Any?) : Params
internal data class Params4(val a: Any?, val b: Any?, val c: Any?, val d: Any?) : Params
internal data class Params5(val a: Any?, val b: Any?, val c: Any?, val d: Any?, val e: Any?) : Params

interface Provider<T, F : Function<T>>

fun <T, F : Function<T>> Provider<*, F>.factory(): F = factories[this] as F

fun <T, F : Function<T>> Provider<*, F>.restore() {
    factories[this] = factory()
}

fun <T, F : Function<T>> Provider<*, F>.overrideFactory(f: F) = remove()
    .also { OverriddenCache.factories[this] = this.factory() }
    .also { factories[this] = f }

internal inline fun <T> Any?.invokes() = (Caster.cast<Function0<T>>(this)).invoke()
internal inline fun <A, T> Any?.invokes(a: A) = (this!! as Function1<A, T>).invoke(a)
internal inline fun <A, B, T> Any?.invokes(a: A, b: B) = (this!! as Function2<A, B, T>).invoke(a, b)
internal inline fun <A, B, C, T> Any?.invokes(a: A, b: B, c: C) = (this!! as Function3<A, B, C, T>).invoke(a, b, c)
internal inline fun <A, B, C, D, T> Any?.invokes(a: A, b: B, c: C, d: D) =
    (this!! as Function4<A, B, C, D, T>).invoke(a, b, c, d)

internal inline fun <A, B, C, D, E, T> Any?.invokes(a: A, b: B, c: C, d: D, e: E) =
    (this!! as Function5<A, B, C, D, E, T>).invoke(a, b, c, d, e)

internal inline fun <T> Any?.invokescoped(scope: ScopedFactory) = (this!! as ScopedFactory.() -> T).invoke(scope)
internal inline fun <A, T> Any?.invokescoped(scope: ScopedFactory, a: A) =
    (this!! as ScopedFactory.(A) -> T).invoke(scope, a)

internal inline fun <A, B, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B) =
    (this!! as ScopedFactory.(A, B) -> T).invoke(scope, a, b)

internal inline fun <A, B, C, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B, c: C) =
    (this!! as ScopedFactory.(A, B, C) -> T).invoke(scope, a, b, c)

internal inline fun <A, B, C, D, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B, c: C, d: D) =
    (this!! as ScopedFactory.(A, B, C, D) -> T).invoke(scope, a, b, c, d)

internal inline fun <A, B, C, D, E, T> Any?.invokescoped(scope: ScopedFactory, a: A, b: B, c: C, d: D, e: E) =
    (this!! as ScopedFactory.(A, B, C, D, E) -> T).invoke(scope, a, b, c, d, e)

private inline fun getScope(scope: Scope) = ShankCache.scopedCache[scope]

private inline fun Provider<*, *>.remove() {
    ShankCache.scopedCache.forEach { scope ->
        scope.value.forEach { it ->
            if (it.key.first == this) {
                scope.value.remove(it.key)
            }
        }
    }
}

internal inline fun <T, F : Function<T>> Provider<*, F>.get(
    scope: Scope,
    params: Params = Params0,
    f: Any?.() -> T
): T {
    if (getScope(scope) == null) {
        ShankCache.scopedCache[scope] = ConcurrentHashMap()
    }

    return (getScope(scope)!!.let { newScope ->
        val pair = Pair(this, params)
        newScope[pair] ?: factories[this].f().also {
            newScope[pair] = it as Any
        }
    }) as T
}
