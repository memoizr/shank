package com.memoizr

inline class ScopedProvider<T>(override val i: Long) : Provider<T, ScopedFactory.() ->T> {
    operator fun invoke(scope: Scope): T = get(scope) { invokescoped(ScopedFactory(scope)) }
}

inline class ScopedProvider1<A, T>(override val i: Long) : Provider<T, ScopedFactory.(A) -> T> {
    operator fun invoke(scope: Scope, a: A): T = get(scope, Params1(a)) { invokescoped(ScopedFactory(scope), a) }
}

inline class ScopedProvider2<A, B, T>(override val i: Long) : Provider<T, ScopedFactory.(A, B) ->T> {
    operator fun invoke(scope: Scope, a: A, b: B): T =
        get(scope, Params2(a, b)) { invokescoped(ScopedFactory(scope), a, b) }
}

inline class ScopedProvider3<A, B, C, T>(override val i: Long) : Provider<T, ScopedFactory.(A, B, C) -> T > {
    operator fun invoke(scope: Scope, a: A, b: B, c: C): T =
        get(scope, Params3(a, b, c)) { invokescoped(ScopedFactory(scope), a, b, c) }
}

inline class ScopedProvider4<A, B, C, D, T>(override val i: Long) : Provider<T,ScopedFactory.(A, B, C, D) -> T > {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D): T =
        get(scope, Params4(a, b, c, d)) { invokescoped(ScopedFactory(scope), a, b, c, d) }
}

inline class ScopedProvider5<A, B, C, D, E, T>(override val i: Long) : Provider<T, ScopedFactory.(A, B, C, D, E) -> T> {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D, e: E): T =
        get(scope, Params5(a, b, c, d, e)) { invokescoped(ScopedFactory(scope), a, b, c, d, e) }
}

