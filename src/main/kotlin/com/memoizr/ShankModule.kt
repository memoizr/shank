package com.memoizr

fun ShankModule.getModuleFactories() = (ShankCache.moduleFactories[this] ?: ArrayList<Pair<Provider<*,*>, Function<*>>>().also { ShankCache.moduleFactories.put(this, it)})

interface ShankModule {
    fun registerFactories() : Unit = getModuleFactories().forEach { ShankCache.factories[it.first] = it.second }

    fun <T> new(factory: () -> T) = NewProvider(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, T> new(factory: (A) -> T) = NewProvider1(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, T> new(factory: (A, B) -> T) = NewProvider2(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, T> new(factory: (A, B, C) -> T) = NewProvider3(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, D, T> new(factory: (A, B, C, D) -> T) = NewProvider4(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, D, E, T> new(factory: (A, B, C, D, E) -> T) = NewProvider5(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }

    fun <T> scoped(factory: ScopedFactory.() -> T) = ScopedProvider(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, T> scoped(factory: ScopedFactory.(A) -> T) = ScopedProvider1(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, T> scoped(factory: ScopedFactory.(A, B) -> T) = ScopedProvider2(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, T> scoped(factory: ScopedFactory.(A, B, C) -> T) = ScopedProvider3(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, D, T> scoped(factory: ScopedFactory.(A, B, C, D) -> T) = ScopedProvider4(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, D, E, T> scoped(factory: ScopedFactory.(A, B, C, D, E) -> T) = ScopedProvider5(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }

    fun <T> singleton(factory: () -> T) = SingletonProvider(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, T> singleton(factory: (A) -> T) = SingletonProvider1(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, T> singleton(factory: (A, B) -> T) = SingletonProvider2(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, T> singleton(factory: (A, B, C) -> T) = SingletonProvider3(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, D, T> singleton(factory: (A, B, C, D) -> T) = SingletonProvider4(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }
    fun <A, B, C, D, E, T> singleton(factory: (A, B, C, D, E) -> T) = SingletonProvider5(factory).also { getModuleFactories().add(it to factory) }.also { ShankCache.factories[it] = factory }

    operator fun invoke(customize: ShankModule.() -> Unit) = also(customize)
}
