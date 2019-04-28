package life.shank

import life.shank.Caster.cast
import life.shank.ShankCache.scopedCache
import life.shank._cache.factories
import java.util.concurrent.ConcurrentHashMap

internal interface Params
internal object Params0 : Params
internal inline class Params1(val a: Any?) : Params
internal data class Params2(val a: Any?, val b: Any?) : Params
internal data class Params3(val a: Any?, val b: Any?, val c: Any?) : Params
internal data class Params4(val a: Any?, val b: Any?, val c: Any?, val d: Any?) : Params
internal data class Params5(val a: Any?, val b: Any?, val c: Any?, val d: Any?, val e: Any?) : Params

interface Provider<T, F : Function<T>>

fun <T, F : Function<T>> Provider<*, F>.factory(): F = cast<F>(factories[this])
fun <T, F : Function<T>> Provider<*, F>.restore() = also { factories[this] = factory() }

fun <T, F : Function<T>> Provider<*, F>.overrideFactory(f: F) = remove()
    .also { OverriddenCache.factories[this] = this.factory() }
    .also { factories[this] = f }

private inline fun Provider<*, *>.remove() = also {
    scopedCache.forEach { scope ->
        scope.value.forEach { it ->
            if (it.key.first == this) {
                scope.value.remove(it.key)
            }
        }
    }
}

internal inline fun Scope.lookInParents(params: Pair<Provider<*, *>, Params>): Any? {
    var s: Scope? = parent
    while (s != null) {
        val v = findCached(s, params)
        if (v != null) {
            return v
        }
        s = s.parent
    }
    return null
}

internal inline fun findCached(scope: Scope, params: Pair<Provider<*, *>, Params>) = (
        ShankCache.scopedCache[scope] ?: ConcurrentHashMap<Pair<Provider<*, *>, Params>, Any?>().also { ShankCache.scopedCache[scope] = it }
        ).let { it[params] }

internal inline fun <T, F : Function<T>> Provider<*, F>.get(scope: Scope, params: Params = Params0, f: Any?.() -> T): T = Pair(this, params).let { p ->
    cast<T>(findCached(scope, p) ?: scope.lookInParents(p) ?: factories[this].f().also { ShankCache.scopedCache[scope]!![p] = it })
}

internal inline fun <T> Any?.i() = cast<Function0<T>>(this).invoke()
internal inline fun <A, T> Any?.i(a: A) = cast<Function1<A, T>>(this).invoke(a)
internal inline fun <A, B, T> Any?.i(a: A, b: B) = cast<Function2<A, B, T>>(this).invoke(a, b)
internal inline fun <A, B, C, T> Any?.i(a: A, b: B, c: C) = cast<Function3<A, B, C, T>>(this).invoke(a, b, c)
internal inline fun <A, B, C, D, T> Any?.i(a: A, b: B, c: C, d: D) = cast<Function4<A, B, C, D, T>>(this).invoke(a, b, c, d)
internal inline fun <A, B, C, D, E, T> Any?.i(a: A, b: B, c: C, d: D, e: E) = cast<Function5<A, B, C, D, E, T>>(this).invoke(a, b, c, d, e)

internal inline fun <T> Any?.ii(scope: ScopedFactory) = cast<ScopedFactory.() -> T>(this).invoke(scope)
internal inline fun <A, T> Any?.ii(scope: ScopedFactory, a: A) = cast<ScopedFactory.(A) -> T>(this).invoke(scope, a)
internal inline fun <A, B, T> Any?.ii(scope: ScopedFactory, a: A, b: B) = cast<ScopedFactory.(A, B) -> T>(this).invoke(scope, a, b)
internal inline fun <A, B, C, T> Any?.ii(scope: ScopedFactory, a: A, b: B, c: C) = cast<ScopedFactory.(A, B, C) -> T>(this).invoke(scope, a, b, c)
internal inline fun <A, B, C, D, T> Any?.ii(scope: ScopedFactory, a: A, b: B, c: C, d: D) = cast<ScopedFactory.(A, B, C, D) -> T>(this).invoke(scope, a, b, c, d)
internal inline fun <A, B, C, D, E, T> Any?.ii(scope: ScopedFactory, a: A, b: B, c: C, d: D, e: E) = cast<ScopedFactory.(A, B, C, D, E) -> T>(this).invoke(scope, a, b, c, d, e)
