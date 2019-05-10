package life.shank

class _ScopedProvider0<T> : ScopedProvider0<T> {
    @Synchronized
    override operator fun invoke(scope: Scope): T = get(scope, 1299821) { ii(SSF(scope)) }
}

class _ScopedProvider1<A, T> : ScopedProvider1<A, T> {
    @Synchronized
    override operator fun invoke(scope: Scope, a: A): T = get(scope, Params1(a)) { ii(SSF(scope), a) }
}

class _ScopedProvider2<A, B, T> : ScopedProvider2<A, B, T> {
    @Synchronized
    override operator fun invoke(scope: Scope, a: A, b: B): T = get(scope, Params2(a, b)) { ii(SSF(scope), a, b) }
}

class _ScopedProvider3<A, B, C, T> : ScopedProvider3<A, B, C, T> {
    @Synchronized
    override operator fun invoke(scope: Scope, a: A, b: B, c: C): T = get(scope, Params3(a, b, c)) { ii(SSF(scope), a, b, c) }
}

interface ScopedProvider0<T> : Provider<T, () -> T> {
    //    infix fun override(f: ((Scope) -> T)?): ScopedProvider0<T>
    operator fun invoke(scope: Scope): T
}

interface ScopedProvider1<A, T> : Provider<T, (A) -> T> {
    //    infix fun override(f: ((Scope,A) -> T)?): ScopedProvider1<A, T>
    operator fun invoke(scope: Scope, a: A): T
}

interface ScopedProvider2<A, B, T> : Provider<T, (A, B) -> T> {
    //    infix fun override(f: ((Scope,A, B) -> T)?): ScopedProvider2<A, B, T>
    operator fun invoke(scope: Scope, a: A, b: B): T
}

interface ScopedProvider3<A, B, C, T> : Provider<T, (A,B,C) -> T> {
    //    infix fun override(f: ((Scope, A, B, C) -> T)?): ScopedProvider3<A, B, C, T>
    operator fun invoke(scope: Scope, a: A, b: B, c: C): T
}

