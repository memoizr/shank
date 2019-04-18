package com.memoizr

abstract class ShankModule {

    private val factories: MutableList<Pair<Provider<out Any>, Function<Any>>> = mutableListOf()

    fun registerFactories() {
        factories.forEach { ShankCache.factories[it.first] = it.second }
    }

    fun <T : Any> new(factory: () -> T) = NewProvider(factory).also { factories.add(it to factory) }
    fun <A : Any, T : Any> new(factory: (A) -> T) = NewProvider1(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, T : Any> new(factory: (A, B) -> T) = NewProvider2(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, C : Any, T : Any> new(factory: (A, B, C) -> T) = NewProvider3(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, C : Any, D : Any, T : Any> new(factory: (A, B, C, D) -> T) = NewProvider4(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> new(factory: (A, B, C, D, E) -> T) =
        NewProvider5(factory).also { factories.add(it to factory) }

    fun <T : Any> scoped(factory: ScopedFactory.() -> T) = ScopedProvider(factory).also { factories.add(it to factory) }
    fun <A : Any, T : Any> scoped(factory: ScopedFactory.(A) -> T) = ScopedProvider1(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, T : Any> scoped(factory: ScopedFactory.(A, B) -> T) =
        ScopedProvider2(factory).also { factories.add(it to factory) }

    fun <A : Any, B : Any, C : Any, T : Any> scoped(factory: ScopedFactory.(A, B, C) -> T) =
        ScopedProvider3(factory).also { factories.add(it to factory) }

    fun <A : Any, B : Any, C : Any, D : Any, T : Any> scoped(factory: ScopedFactory.(A, B, C, D) -> T) =
        ScopedProvider4(factory).also { factories.add(it to factory) }

    fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> scoped(factory: ScopedFactory.(A, B, C, D, E) -> T) =
        ScopedProvider5(factory).also { factories.add(it to factory) }


    fun <T : Any> singleton(factory: () -> T) = SingletonProvider(factory).also { factories.add(it to factory) }
    fun <A : Any, T : Any> singleton(factory: (A) -> T) = SingletonProvider1(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, T : Any> singleton(factory: (A, B) -> T) = SingletonProvider2(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, C : Any, T : Any> singleton(factory: (A, B, C) -> T) = SingletonProvider3(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, C : Any, D : Any, T : Any> singleton(factory: (A, B, C, D) -> T) = SingletonProvider4(factory).also { factories.add(it to factory) }
    fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> singleton(factory: (A, B, C, D, E) -> T) = SingletonProvider5(factory).also { factories.add(it to factory) }
    operator fun invoke(customize: ShankModule.() -> Unit) = also(customize)
}
