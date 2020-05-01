package life.shank

interface SingleProvider0<T> : Provider<T, () -> T> {
    operator fun invoke(): T
}

interface SingleProvider1<P1, T> : Provider<T, (P1) -> T> {
    operator fun invoke(p1: P1): T
}

interface SingleProvider2<P1, P2, T> : Provider<T, (P1, P2) -> T> {
    operator fun invoke(p1: P1, p2: P2): T
}

interface SingleProvider3<P1, P2, P3, T> : Provider<T, (P1, P2, P3) -> T> {
    operator fun invoke(p1: P1, p2: P2, p3: P3): T
}

interface SingleProvider4<P1, P2, P3, P4, T> : Provider<T, (P1, P2, P3, P4) -> T> {
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): T
}

interface SingleProvider5<P1, P2, P3, P4, P5, T> : Provider<T, (P1, P2, P3, P4, P5) -> T> {
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T
}

inline fun <T> ShankModule.single(crossinline factory: () -> T) = object : SingleProvider0<T> {
    @Synchronized
    override fun invoke(): T = createOrGetSingletonInstance(hashCode()) { factory() }
}

inline fun <P1, T> ShankModule.single(crossinline factory: (P1) -> T) = object : SingleProvider1<P1, T> {
    @Synchronized
    override fun invoke(p1: P1): T = createOrGetSingletonInstance(shankInternalMashHashCodes(this, p1)) { factory(p1) }
}

inline fun <P1, P2, T> ShankModule.single(crossinline factory: (P1, P2) -> T) = object : SingleProvider2<P1, P2, T> {
    @Synchronized
    override fun invoke(p1: P1, p2: P2): T = createOrGetSingletonInstance(shankInternalMashHashCodes(this, p1, p2)) { factory(p1, p2) }
}

inline fun <P1, P2, P3, T> ShankModule.single(crossinline factory: (P1, P2, P3) -> T) = object : SingleProvider3<P1, P2, P3, T> {
    @Synchronized
    override fun invoke(p1: P1, p2: P2, p3: P3): T = createOrGetSingletonInstance(shankInternalMashHashCodes(this, p1, p2, p3)) { factory(p1, p2, p3) }
}

inline fun <P1, P2, P3, P4, T> ShankModule.single(crossinline factory: (P1, P2, P3, P4) -> T) = object : SingleProvider4<P1, P2, P3, P4, T> {
    @Synchronized
    override fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): T =
        createOrGetSingletonInstance(shankInternalMashHashCodes(this, p1, p2, p3, p4)) { factory(p1, p2, p3, p4) }
}

inline fun <P1, P2, P3, P4, P5, T> ShankModule.single(crossinline factory: (P1, P2, P3, P4, P5) -> T) = object : SingleProvider5<P1, P2, P3, P4, P5, T> {
    @Synchronized
    override fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T =
        createOrGetSingletonInstance(shankInternalMashHashCodes(this, p1, p2, p3, p4, p5)) { factory(p1, p2, p3, p4, p5) }
}
