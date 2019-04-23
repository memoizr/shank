package com.memoizr

import com.memoizr.ShankFactoryCache.factories


interface ShankModule {
    fun <T> ShankModule.new(factory: () -> T) = NewProvider(factory).also { factories[it] = factory }
    fun <A, T> ShankModule.new(factory: (A) -> T) = NewProvider1(factory).also { factories[it] = factory }
    fun <A, B, T> ShankModule.new(factory: (A, B) -> T) = NewProvider2(factory).also { factories[it] = factory }
    fun <A, B, C, T> ShankModule.new(factory: (A, B, C) -> T) = NewProvider3(factory).also { factories[it] = factory }
    fun <A, B, C, D, T> ShankModule.new(factory: (A, B, C, D) -> T) = NewProvider4(factory).also { factories[it] = factory }
    fun <A, B, C, D, E, T> ShankModule.new(factory: (A, B, C, D, E) -> T) = NewProvider5(factory).also { factories[it] = factory }

    fun <T> ShankModule.scoped(factory: ScopedFactory.() -> T) = ScopedProvider(factory).also { factories[it] = factory }
    fun <A, T> ShankModule.scoped(factory: ScopedFactory.(A) -> T) = ScopedProvider1(factory).also { factories[it] = factory }
    fun <A, B, T> ShankModule.scoped(factory: ScopedFactory.(A, B) -> T) = ScopedProvider2(factory).also { factories[it] = factory }
    fun <A, B, C, T> ShankModule.scoped(factory: ScopedFactory.(A, B, C) -> T) = ScopedProvider3(factory).also { factories[it] = factory }
    fun <A, B, C, D, T> ShankModule.scoped(factory: ScopedFactory.(A, B, C, D) -> T) = ScopedProvider4(factory).also { factories[it] = factory }
    fun <A, B, C, D, E, T> ShankModule.scoped(factory: ScopedFactory.(A, B, C, D, E) -> T) = ScopedProvider5(factory).also { factories[it] = factory }

    fun <T> ShankModule.singleton(factory: () -> T) = SingletonProvider(factory).also { factories[it] = factory }
    fun <A, T> ShankModule.singleton(factory: (A) -> T) = SingletonProvider1(factory).also { factories[it] = factory }
    fun <A, B, T> ShankModule.singleton(factory: (A, B) -> T) = SingletonProvider2(factory).also { factories[it] = factory }
    fun <A, B, C, T> ShankModule.singleton(factory: (A, B, C) -> T) = SingletonProvider3(factory).also { factories[it] = factory }
    fun <A, B, C, D, T> ShankModule.singleton(factory: (A, B, C, D) -> T) = SingletonProvider4(factory).also { factories[it] = factory }
    fun <A, B, C, D, E, T> ShankModule.singleton(factory: (A, B, C, D, E) -> T) = SingletonProvider5(factory).also { factories[it] = factory }
}
operator fun ShankModule.invoke(customize: ShankModule.() -> Unit) = also(customize)

