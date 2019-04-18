package com.memoizr

import com.memoizr.ShankCache.factories

class NewProvider<T>(f: () -> T) : Provider<T>(f) {
    operator fun invoke(): T = factories[this].invokes()
    fun overrideFactory(f: () -> T) {
        factories[this] = f as Function<Any>
    }
}

class NewProvider1<A, T>(f: (A) -> T) : Provider<T>(f) {
    operator fun invoke(a: A): T = factories[this].invokes(a)
    fun overrideFactory(f: (A) -> T) {
        factories[this] = f as Function<Any>
    }
}

class NewProvider2<A, B, T>(f: (A, B) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B): T = factories[this].invokes(a, b)
    fun overrideFactory(f: (A, B) -> T) {
        factories[this] = f as Function<Any>
    }
}

class NewProvider3<A, B, C, T>(f: (A, B, C) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B, c: C): T = factories[this].invokes(a, b, c)
    fun overrideFactory(f: (A, B, C) -> T) {
        factories[this] = f as Function<Any>
    }
}

class NewProvider4<A, B, C, D, T>(f: (A, B, C, D) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B, c: C, d: D): T = factories[this].invokes(a, b, c, d)
    fun overrideFactory(f: (A, B, C, D) -> T) {
        factories[this] = f as Function<Any>
    }
}

class NewProvider5<A, B, C, D, E, T>(f: (A, B, C, D, E) -> T) : Provider<T>(f) {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = factories[this].invokes(a, b, c, d, e)
    fun overrideFactory(f: (A, B, C, D, E) -> T) {
        factories[this] = f as Function<Any>
    }
}
