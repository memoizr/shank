package com.memoizr

class NewProviderWrapper<T : Any>(factory: () -> T) : Wrapper<T>(factory) {
    operator fun invoke(): T = ShankCache.factories[this].invokes()
}

class NewProviderWrapper1<A : Any, T : Any>(factory: (A) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A): T = ShankCache.factories[this].invokes(a)
}

class NewProviderWrapper2<A : Any, B : Any, T : Any>(factory: (A, B) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B): T = ShankCache.factories[this].invokes(a, b)
}

class NewProviderWrapper3<A : Any, B : Any, C : Any, T : Any>(factory: (A, B, C) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B, c: C): T = ShankCache.factories[this].invokes(a, b, c)
}

class NewProviderWrapper4<A : Any, B : Any, C : Any, D : Any, T : Any>(factory: (A, B, C, D) -> T) :
    Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B, c: C, d: D): T = ShankCache.factories[this].invokes(a, b, c, d)
}

class NewProviderWrapper5<A : Any, B : Any, C : Any, D : Any, E : Any, T : Any>(factory: (A, B, C, D, E) -> T) :
    Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = ShankCache.factories[this].invokes(a, b, c, d, e)
}
