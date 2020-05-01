package life.shank


interface ScopedProvider0<T> : Provider<T, () -> T> {
    operator fun invoke(scope: Scope): T
}

interface ScopedProvider1<P1, T> : Provider<T, (P1) -> T> {
    operator fun invoke(scope: Scope, p1: P1): T
}

interface ScopedProvider2<P1, P2, T> : Provider<T, (P1, P2) -> T> {
    operator fun invoke(scope: Scope, p1: P1, p2: P2): T
}

interface ScopedProvider3<P1, P2, P3, T> : Provider<T, (P1, P2, P3) -> T> {
    operator fun invoke(scope: Scope, p1: P1, p2: P2, p3: P3): T
}

interface ScopedProvider4<P1, P2, P3, P4, T> : Provider<T, (P1, P2, P3, P4) -> T> {
    operator fun invoke(scope: Scope, p1: P1, p2: P2, p3: P3, p4: P4): T
}

interface ScopedProvider5<P1, P2, P3, P4, P5, T> : Provider<T, (P1, P2, P3, P4, P5) -> T> {
    operator fun invoke(scope: Scope, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T
}

inline fun <T> ShankModule.scoped(crossinline factory: Scoped.() -> T): ScopedProvider0<T> = object : ScopedProvider0<T> {
    @Synchronized
    override fun invoke(scope: Scope): T = createOrGetScopedInstance(scope, hashCode()) { factory(InternalScoped(scope)) }
}

inline fun <P1, T> ShankModule.scoped(crossinline factory: Scoped.(P1) -> T): ScopedProvider1<P1, T> = object : ScopedProvider1<P1, T> {
    @Synchronized
    override fun invoke(scope: Scope, p1: P1): T = createOrGetScopedInstance(scope, shankInternalMashHashCodes(this, p1)) { factory(InternalScoped(scope), p1) }
}

inline fun <P1, P2, T> ShankModule.scoped(crossinline factory: Scoped.(P1, P2) -> T): ScopedProvider2<P1, P2, T> = object : ScopedProvider2<P1, P2, T> {
    @Synchronized
    override fun invoke(scope: Scope, p1: P1, p2: P2): T =
        createOrGetScopedInstance(scope, shankInternalMashHashCodes(this, p1, p2)) { factory(InternalScoped(scope), p1, p2) }
}

inline fun <P1, P2, P3, T> ShankModule.scoped(crossinline factory: Scoped.(P1, P2, P3) -> T): ScopedProvider3<P1, P2, P3, T> =
    object : ScopedProvider3<P1, P2, P3, T> {
        @Synchronized
        override fun invoke(scope: Scope, p1: P1, p2: P2, p3: P3): T =
            createOrGetScopedInstance(scope, shankInternalMashHashCodes(this, p1, p2, p3)) { factory(InternalScoped(scope), p1, p2, p3) }
    }

inline fun <P1, P2, P3, P4, T> ShankModule.scoped(crossinline factory: Scoped.(P1, P2, P3, P4) -> T): ScopedProvider4<P1, P2, P3, P4, T> =
    object : ScopedProvider4<P1, P2, P3, P4, T> {
        @Synchronized
        override fun invoke(scope: Scope, p1: P1, p2: P2, p3: P3, p4: P4): T =
            createOrGetScopedInstance(scope, shankInternalMashHashCodes(this, p1, p2, p3, p4)) { factory(InternalScoped(scope), p1, p2, p3, p4) }
    }

inline fun <P1, P2, P3, P4, P5, T> ShankModule.scoped(crossinline factory: Scoped.(P1, P2, P3, P4, P5) -> T): ScopedProvider5<P1, P2, P3, P4, P5, T> =
    object : ScopedProvider5<P1, P2, P3, P4, P5, T> {
        @Synchronized
        override fun invoke(scope: Scope, p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T =
            createOrGetScopedInstance(scope, shankInternalMashHashCodes(this, p1, p2, p3, p4, p5)) { factory(InternalScoped(scope), p1, p2, p3, p4, p5) }
    }
