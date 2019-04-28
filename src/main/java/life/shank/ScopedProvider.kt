package life.shank

 class ScopedProvider<T>() : Provider<T, ScopedFactory.() -> T> {
    operator fun invoke(scope: Scope): T = get(scope) { ii(SimpleScopedFactory(scope)) }
}

 class ScopedProvider1<A, T>() : Provider<T, ScopedFactory.(A) -> T> {
    operator fun invoke(scope: Scope, a: A): T = get(scope,
        Params1(a)
    ) { ii(SimpleScopedFactory(scope), a) }
}

 class ScopedProvider2<A, B, T>() : Provider<T, ScopedFactory.(A, B) -> T> {
    operator fun invoke(scope: Scope, a: A, b: B): T =
        get(scope, Params2(a, b)) { ii(SimpleScopedFactory(scope), a, b) }
}

 class ScopedProvider3<A, B, C, T>() : Provider<T, ScopedFactory.(A, B, C) -> T> {
    operator fun invoke(scope: Scope, a: A, b: B, c: C): T =
        get(scope, Params3(a, b, c)) { ii(SimpleScopedFactory(scope), a, b, c) }
}

 class ScopedProvider4<A, B, C, D, T>() : Provider<T, ScopedFactory.(A, B, C, D) -> T> {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D): T =
        get(scope, Params4(a, b, c, d)) { ii(SimpleScopedFactory(scope), a, b, c, d) }
}

 class ScopedProvider5<A, B, C, D, E, T>() : Provider<T, ScopedFactory.(A, B, C, D, E) -> T> {
    operator fun invoke(scope: Scope, a: A, b: B, c: C, d: D, e: E): T =
        get(scope, Params5(a, b, c, d, e)) { ii(SimpleScopedFactory(scope), a, b, c, d, e) }
}

