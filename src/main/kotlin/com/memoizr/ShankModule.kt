package com.memoizr

abstract class ShankModule {
    fun <T : Any> new(factory: () -> T) = NewProviderWrapper(factory)
    fun <A : Any, T : Any> new(factory: (A) -> T) = NewProviderWrapper1(factory)
    fun <A : Any, B : Any, T : Any> new(factory: (A, B) -> T) = NewProviderWrapper2(factory)
    fun <A : Any, B : Any, C : Any, T : Any> new(factory: (A, B, C) -> T) = NewProviderWrapper3(factory)
    fun <A : Any, B : Any, C : Any, D : Any, T : Any> new(factory: (A, B, C, D) -> T) = NewProviderWrapper4(factory)
    fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> new(factory: (A, B, C, D, E) -> T) =
        NewProviderWrapper5(factory)

    fun <T : Any> scoped(factory: ScopedFactory.() -> T) = ScopedSingletonProviderWrapper(factory)
    fun <A : Any, T : Any> scoped(factory: ScopedFactory.(A) -> T) = ScopedSingletonProviderWrapper1(factory)
    fun <A : Any, B : Any, T : Any> scoped(factory: ScopedFactory.(A, B) -> T) =
        ScopedSingletonProviderWrapper2(factory)

    fun <A : Any, B : Any, C : Any, T : Any> scoped(factory: ScopedFactory.(A, B, C) -> T) =
        ScopedSingletonProviderWrapper3(factory)

    fun <A : Any, B : Any, C : Any, D : Any, T : Any> scoped(factory: ScopedFactory.(A, B, C, D) -> T) =
        ScopedSingletonProviderWrapper4(factory)

    fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> scoped(factory: ScopedFactory.(A, B, C, D, E) -> T) =
        ScopedSingletonProviderWrapper5(factory)


    fun <T : Any> singleton(factory: () -> T) = GlobalSingletonProviderWrapper(factory)
    fun <A : Any, T : Any> singleton(factory: (A) -> T) = GlobalSingletonProviderWrapper1(factory)
    fun <A : Any, B : Any, T : Any> singleton(factory: (A, B) -> T) = GlobalSingletonProviderWrapper2(factory)
    fun <A : Any, B : Any, C : Any, T : Any> singleton(factory: (A, B, C) -> T) =
        GlobalSingletonProviderWrapper3(factory)

    fun <A : Any, B : Any, C : Any, D : Any, T : Any> singleton(factory: (A, B, C, D) -> T) =
        GlobalSingletonProviderWrapper4(factory)

    fun <A : Any, B : Any, C : Any, D : Any, E : Any, T : Any> singleton(factory: (A, B, C, D, E) -> T) =
        GlobalSingletonProviderWrapper5(factory)

    operator fun invoke(customize: ShankModule.() -> Unit) = also(customize)
}
