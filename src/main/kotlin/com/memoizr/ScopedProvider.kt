package com.memoizr

class ScopedProvider<T>(factory: ScopedFactory.() -> T) : Provider<T, ScopedFactory.() ->T>(factory) {
    operator fun invoke(scope: Scope): T = get(scope) { invokescoped(ScopedFactory(scope)) }
}

class ScopedProvider1<A, T>(factory: ScopedFactory.(A) -> T) : Provider<T, ScopedFactory.(A) -> T>(factory) {
    operator fun invoke(scope: Scope, a: A): T = get(scope, Params1(a)) { invokescoped(ScopedFactory(scope), a) }
}

class ScopedProvider2<A, B, T>(factory: ScopedFactory.(A, B) -> T) : Provider<T, ScopedFactory.(A, B) ->T>(factory) {
    operator fun invoke(scope: Scope, a: A, b: B): T =
        get(scope, Params2(a, b)) { invokescoped(ScopedFactory(scope), a, b) }
}

class ScopedProvider3<A, B, C, T>(factory: ScopedFactory.(A, B, C) -> T) : Provider<T, ScopedFactory.(A, B, C) -> T >(factory) {
    operator fun invoke(scope: Scope, a: A, b: B, c: C): T =
        get(scope, Params3(a, b, c)) { invokescoped(ScopedFactory(scope), a, b, c) }
}

class ScopedProvider4<A, B, C, D, T>(factory: ScopedFactory.(A, B, C, D) -> T) : Provider<T,ScopedFactory.(A, B, C, D) -> T >(factory) {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D): T =
        get(scope, Params4(a, b, c, d)) { invokescoped(ScopedFactory(scope), a, b, c, d) }
}

class ScopedProvider5<A, B, C, D, E, T>(factory: ScopedFactory.(A, B, C, D, E) -> T) : Provider<T, ScopedFactory.(A, B, C, D, E) -> T>(factory) {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D, e: E): T =
        get(scope, Params5(a, b, c, d, e)) { invokescoped(ScopedFactory(scope), a, b, c, d, e) }
}

