package com.memoizr

import com.memoizr.ShankCache.factories
import com.memoizr.ShankCache.globalScope

class SingletonProvider<T>(f: () -> T) : Provider<T>(f) {
    fun overrideFactory(f: () -> T) = remove().also { factories[this] = f }

    operator fun invoke(): T = get(globalScope) { invokes() }
}

class SingletonProvider1<A, T>(f: (A) -> T) : Provider<T>(f) {
    fun overrideFactory(f: (A) -> T) = remove().also { factories[this] = f }

    operator fun invoke(a: A): T = get(globalScope) { invokes(a) }
}

class SingletonProvider2<A, B, T>(f: (A, B) -> T) : Provider<T>(f) {
    fun overrideFactory(f: (A, B) -> T) = remove().also { factories[this] = f }

    operator fun invoke(a: A, b: B): T = get(globalScope) { invokes(a, b) }
}

class SingletonProvider3<A, B, C, T>(f: (A, B, C) -> T) : Provider<T>(f) {
    fun overrideFactory(f: (A, B, C) -> T) = remove().also { factories[this] = f }

    operator fun invoke(a: A, b: B, c: C): T = get(globalScope) { invokes(a, b, c) }
}

class SingletonProvider4<A, B, C, D, T>(f: (A, B, C, D) -> T) : Provider<T>(f) {
    fun overrideFactory(f: (A, B, C, D) -> T) = remove().also { factories[this] = f }

    operator fun invoke(a: A, b: B, c: C, d: D): T = get(globalScope) { invokes(a, b, c, d) }
}

class SingletonProvider5<A, B, C, D, E, T>(f: (A, B, C, D, E) -> T) : Provider<T>(f) {
    fun overrideFactory(f: (A, B, C, D, E) -> T) = remove().also { factories[this] = f }

    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = get(globalScope) { invokes(a, b, c, d, e) }
}
