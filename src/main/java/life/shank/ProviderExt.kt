package life.shank

import life.shank.Caster.cast
import life.shank.ShankGlobalCache.globalCache
import life.shank.ShankScopedCache.scopedCache
import life.shank._cache.factories

internal fun <T, F : Function<T>> Provider<*, F>.factory(): F = cast<F>(factories.get(this.hashCode()))
fun <T, F : Function<T>> Provider<*, F>.restore() = also { factories.put(this.hashCode(), factory()) }
fun <T, F : Function<T>> Provider<*, F>.overrideFactory(f: F) = remove()
    .also { OverriddenCache.factories[this] = this.factory() }
    .also { factories.put(this, f) }

private inline fun Provider<*, *>.remove() = also {
    globalCache.keys.forEach {
        if (((it - (hashCode() * 31)) % 127) == 0) {
            globalCache.remove(it)
        }
    }
    scopedCache.values.filterNotNull().forEach { scope ->
        val hashMap = scope as HashcodeHashMap<Any>
        hashMap.keys.forEach {
            if (((it - (hashCode() * 31)) % 127) == 0) {
                hashMap.remove(it)
            }
        }
    }
}

internal typealias ProvToParams = Int
internal inline fun Scope.lookInParents(params: ProvToParams): Any? {
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

internal inline fun findCached(scope: Scope, params: ProvToParams) = (
        ShankScopedCache.scopedCache[scope.hashcode]
            ?: HashcodeHashMap<Any?>().also { it: HashcodeHashMap<Any?> -> ShankScopedCache.scopedCache.put(scope.hashcode, it) }
        ).let { it[params] }

internal inline fun <T, F : Function<T>> Provider<*, F>.get(scope: Scope, params: Params, f: Any?.() -> T): T = mash(params).let { p ->
    cast<T>(findCached(scope, p) ?: scope.lookInParents(p) ?: factories[this].f().also { ShankScopedCache.scopedCache.get(scope.hashcode)!!.put(p, it) })
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

internal inline infix fun Provider<*, *>.mash(other: Params) = hashCode() * 31 + other * 127

inline infix fun Any?.or(other: Any?) = this.hashCode() * 31 + other.hashCode()
internal typealias Params = Int

internal inline fun Params1(a: Any?): Params = a.hashCode()
internal inline fun Params2(a: Any?, b: Any?): Params = a or b
internal inline fun Params3(a: Any?, b: Any?, c: Any?): Params = a or b or c
internal inline fun Params4(a: Any?, b: Any?, c: Any?, d: Any?): Params = a or b or c or d
internal inline fun Params5(a: Any?, b: Any?, c: Any?, d: Any?, e: Any?): Params = a or b or c or d or e
