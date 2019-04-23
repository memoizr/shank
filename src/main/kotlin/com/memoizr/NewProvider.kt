package com.memoizr

import com.memoizr.ShankFactoryCache.factories

inline class NewProvider<T>(val i: Long) : Provider<T, () -> T> {
    operator fun invoke(): T = factories[this].invokes()
}

inline class NewProvider1<A, T>(val i: Long) : Provider<T, (A) -> T> {
    operator fun invoke(a: A): T = factories[this].invokes(a)
}

inline class NewProvider2<A, B, T>(val i: Long) : Provider<T, (A, B) ->T> {
    operator fun invoke(a: A, b: B): T = factories[this].invokes(a, b)
}

inline class NewProvider3<A, B, C, T>(val i: Long) : Provider<T, (A, B, C) -> T> {
    operator fun invoke(a: A, b: B, c: C): T = factories[this].invokes(a, b, c)
}

inline class NewProvider4<A, B, C, D, T>(val i: Long) : Provider<T,  (A, B, C, D) -> T> {
    operator fun invoke(a: A, b: B, c: C, d: D): T = factories[this].invokes(a, b, c, d)
}

inline class NewProvider5<A, B, C, D, E, T>(val i: Long) : Provider<T, (A, B, C, D, E) -> T> {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = factories[this].invokes(a, b, c, d, e)
}
