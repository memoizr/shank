package com.memoizr

import com.memoizr.ShankCache.globalScope

class GlobalSingletonProviderWrapper<T : Any>(factory: () -> T) : Wrapper<T>(factory) {
    operator fun invoke(): T = extractValue(ShankCache.globalScope) { invokes() }
}

class GlobalSingletonProviderWrapper1<A : Any, T : Any>(factory: (A) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A): T = extractValue(globalScope) { invokes(a) }
}

class GlobalSingletonProviderWrapper2<A : Any, B: Any, T : Any>(factory: (A, B) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B): T = extractValue(globalScope) { invokes(a, b) }
}

class GlobalSingletonProviderWrapper3<A : Any, B: Any, C: Any, T : Any>(factory: (A, B, C) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B, c: C): T = extractValue(globalScope) { invokes(a, b, c) }
}

class GlobalSingletonProviderWrapper4<A : Any, B: Any, C: Any, D: Any, T : Any>(factory: (A, B, C, D) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B, c: C, d: D): T = extractValue(globalScope) { invokes(a, b, c, d) }
}

class GlobalSingletonProviderWrapper5<A : Any, B: Any, C: Any, D: Any, E: Any, T : Any>(factory: (A, B, C, D, E) -> T) : Wrapper<T>(factory) {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = extractValue(globalScope) { invokes(a, b, c, d, e) }
}
