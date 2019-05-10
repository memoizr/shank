@file:Suppress("NOTHING_TO_INLINE")

package life.shank

import life.shank._cache.factories

interface ShankModule

operator fun <T : ShankModule> T.invoke(customize: T.() -> Unit) = also(customize)

inline fun <T> ShankModule.scoped(noinline factory: ScopedFactory.() -> T): ScopedProvider0<T> = _ScopedProvider0<T>().also { factories.put(it.hashCode(), factory) }
inline fun <A, T> ShankModule.scoped(noinline factory: ScopedFactory.(A) -> T): ScopedProvider1<A, T> = _ScopedProvider1<A, T>().also { factories.put(it.hashCode(), factory) }
inline fun <A, B, T> ShankModule.scoped(noinline factory: ScopedFactory.(A, B) -> T): ScopedProvider2<A, B, T> = _ScopedProvider2<A, B, T>().also { factories.put(it.hashCode(), factory) }
inline fun <A, B, C, T> ShankModule.scoped(noinline factory: ScopedFactory.(A, B, C) -> T): ScopedProvider3<A, B, C, T> = _ScopedProvider3<A, B, C, T>().also { factories.put(it.hashCode(), factory) }

