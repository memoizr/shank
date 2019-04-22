package com.memoizr

import com.memoizr.ShankCache.globalScope

class SingletonProvider<T>(override val factory: () -> T) : Provider<T, () -> T> {
    fun eager() = also { invoke() }
    operator fun invoke(): T = get(globalScope) { invokes() }
}

class SingletonProvider1<A, T>(override val factory: (A) -> T) : Provider<T, (A) -> T> {
    fun eager(a: A) = also { invoke(a) }
    operator fun invoke(a: A): T = get(globalScope, Params1(a)) { invokes(a) }
}

class SingletonProvider2<A, B, T>(override val factory: (A, B) -> T) : Provider<T, (A, B) -> T> {
    fun eager(a: A, b: B) = also { invoke(a, b) }
    operator fun invoke(a: A, b: B): T = get(globalScope, Params2(a, b)) { invokes(a, b) }
}

class SingletonProvider3<A, B, C, T>(override val factory: (A, B, C) -> T) : Provider<T, (A, B, C) -> T> {
    fun eager(a: A, b: B, c: C) = also { invoke(a, b, c) }
    operator fun invoke(a: A, b: B, c: C): T = get(globalScope, Params3(a, b, c)) { invokes(a, b, c) }
}

class SingletonProvider4<A, B, C, D, T>(override val factory: (A, B, C, D) -> T) : Provider<T, (A, B, C, D) -> T> {
    fun eager(a: A, b: B, c: C, d: D) = also { invoke(a, b, c, d) }
    operator fun invoke(a: A, b: B, c: C, d: D): T = get(globalScope, Params4(a, b, c, d)) { invokes(a, b, c, d) }
}

class SingletonProvider5<A, B, C, D, E, T>(override val factory: (A, B, C, D, E) -> T) : Provider<T, (A, B, C, D, E) -> T> {
    fun eager(a: A, b: B, c: C, d: D, e: E) = also { invoke(a, b, c, d, e) }
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = get(globalScope, Params5(a, b, c, d, e)) { invokes(a, b, c, d, e) }
}
