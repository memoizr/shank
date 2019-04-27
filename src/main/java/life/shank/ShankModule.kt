package life.shank

import life.shank._cache.factories

interface ShankModule

operator fun ShankModule.invoke(customize: ShankModule.() -> Unit) = also(customize)

inline fun <T> ShankModule.new(noinline factory: () -> T): NewProvider<T> = NewProvider<T>().also { factories.put(it, factory) }

fun <A, T> ShankModule.new(factory: (A) -> T) = NewProvider1<A, T>().also { factories.put(it, factory) }
fun <A, B, T> ShankModule.new(factory: (A, B) -> T) = NewProvider2<A, B, T>().also { factories.put(it, factory) }
fun <A, B, C, T> ShankModule.new(factory: (A, B, C) -> T) = NewProvider3<A, B, C, T>().also { factories[it] = factory }
fun <A, B, C, D, T> ShankModule.new(factory: (A, B, C, D) -> T) = NewProvider4<A, B, C, D, T>().also { factories[it] = factory }
fun <A, B, C, D, E, T> ShankModule.new(factory: (A, B, C, D, E) -> T) = NewProvider5<A, B, C, D, E, T>().also { factories[it] = factory }

fun <T> ShankModule.scoped(factory: ScopedFactory.() -> T) =
    ScopedProvider<T>().also { factories[it] = factory }

fun <A, T> ShankModule.scoped(factory: ScopedFactory.(A) -> T) =
    ScopedProvider1<A, T>().also { factories[it] = factory }

fun <A, B, T> ShankModule.scoped(factory: ScopedFactory.(A, B) -> T) =
    ScopedProvider2<A, B, T>().also { factories[it] = factory }

fun <A, B, C, T> ShankModule.scoped(factory: ScopedFactory.(A, B, C) -> T) =
    ScopedProvider3<A, B, C, T>().also { factories[it] = factory }

fun <A, B, C, D, T> ShankModule.scoped(factory: ScopedFactory.(A, B, C, D) -> T) =
    ScopedProvider4<A, B, C, D, T>().also { factories[it] = factory }

fun <A, B, C, D, E, T> ShankModule.scoped(factory: ScopedFactory.(A, B, C, D, E) -> T) =
    ScopedProvider5<A, B, C, D, E, T>().also { factories[it] = factory }

fun <T> ShankModule.singleton(factory: () -> T) =
    SingletonProvider<T>().also { factories[it] = factory }

fun <A, T> ShankModule.singleton(factory: (A) -> T) =
    SingletonProvider1<A, T>().also { factories[it] = factory }

fun <A, B, T> ShankModule.singleton(factory: (A, B) -> T) =
    SingletonProvider2<A, B, T>().also { factories[it] = factory }

fun <A, B, C, T> ShankModule.singleton(factory: (A, B, C) -> T) =
    SingletonProvider3<A, B, C, T>().also { factories[it] = factory }

fun <A, B, C, D, T> ShankModule.singleton(factory: (A, B, C, D) -> T) =
    SingletonProvider4<A, B, C, D, T>().also { factories[it] = factory }

fun <A, B, C, D, E, T> ShankModule.singleton(factory: (A, B, C, D, E) -> T) =
    SingletonProvider5<A, B, C, D, E, T>().also { factories[it] = factory }

