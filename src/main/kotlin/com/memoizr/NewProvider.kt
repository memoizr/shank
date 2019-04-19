package com.memoizr

import com.memoizr.ShankCache.factories

class NewProvider<T>(f: () -> T) : Provider<T, () -> T>(f) {
    operator fun invoke(): T = factories[this].invokes()
}

class NewProvider1<A, T>(f: (A) -> T) : Provider<T, (A) -> T>(f) {
    operator fun invoke(a: A): T = factories[this].invokes(a)
}

class NewProvider2<A, B, T>(f: (A, B) -> T) : Provider<T, (A, B) ->T>(f) {
    operator fun invoke(a: A, b: B): T = factories[this].invokes(a, b)
}

class NewProvider3<A, B, C, T>(f: (A, B, C) -> T) : Provider<T, (A, B, C) -> T>(f) {
    operator fun invoke(a: A, b: B, c: C): T = factories[this].invokes(a, b, c)
}

class NewProvider4<A, B, C, D, T>(f: (A, B, C, D) -> T) : Provider<T,  (A, B, C, D) -> T>(f) {
    operator fun invoke(a: A, b: B, c: C, d: D): T = factories[this].invokes(a, b, c, d)
}

class NewProvider5<A, B, C, D, E, T>(f: (A, B, C, D, E) -> T) : Provider<T, (A, B, C, D, E) -> T>(f) {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = factories[this].invokes(a, b, c, d, e)
}
