package com.memoizr

class ScopedSingletonProviderWrapper<T : Any>(factory: ScopedFactory.() -> T) : Wrapper<T>(factory) {
    operator fun invoke(scope: Scope): T = extractValue(scope) { invokescoped(ScopedFactory(scope)) }
}

class ScopedSingletonProviderWrapper1<A : Any, T : Any>(factory: ScopedFactory.(A) -> T) : Wrapper<T>(factory) {
    operator fun invoke(scope: Scope, a: A): T = extractValue(scope) { invokescoped(ScopedFactory(scope), a) }
}

class ScopedSingletonProviderWrapper2<A : Any, B : Any, T : Any>(factory: ScopedFactory.(A, B) -> T) :
    Wrapper<T>(factory) {
    operator fun invoke(scope: Scope, a: A, b: B): T = extractValue(scope) { invokescoped(ScopedFactory(scope), a, b) }
}

class ScopedSingletonProviderWrapper3<A : Any, B : Any, C : Any, T : Any>(factory: ScopedFactory.(A, B, C) -> T) :
    Wrapper<T>(factory) {
    operator fun invoke(scope: Scope, a: A, b: B, c: C): T =
        extractValue(scope) { invokescoped(ScopedFactory(scope), a, b, c) }
}

class ScopedSingletonProviderWrapper4<A : Any, B : Any, C : Any, D : Any, T : Any>(factory: ScopedFactory.(A, B, C, D) -> T) :
    Wrapper<T>(factory) {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D): T =
        extractValue(scope) { invokescoped(ScopedFactory(scope), a, b, c, d) }
}

class ScopedSingletonProviderWrapper5<A : Any, B : Any, C : Any, D : Any, E : Any, T : Any>(factory: ScopedFactory.(A, B, C, D, E) -> T) :
    Wrapper<T>(factory) {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D, e: E): T =
        extractValue(scope) { invokescoped(ScopedFactory(scope), a, b, c, d, e) }
}

