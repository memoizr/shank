package life.shank

interface NewProvider0<T> : Provider<T, () -> T> {
    operator fun invoke(): T
}

interface NewProvider1<P1, T> : Provider<T, (P1) -> T> {
    operator fun invoke(p1: P1): T
}

interface NewProvider2<P1, P2, T> : Provider<T, (P1, P2) -> T> {
    operator fun invoke(p1: P1, p2: P2): T
}

interface NewProvider3<P1, P2, P3, T> : Provider<T, (P1, P2, P3) -> T> {
    operator fun invoke(p1: P1, p2: P2, p3: P3): T
}

interface NewProvider4<P1, P2, P3, P4, T> : Provider<T, (P1, P2, P3, P4) -> T> {
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): T
}

interface NewProvider5<P1, P2, P3, P4, P5, T> : Provider<T, (P1, P2, P3, P4, P5) -> T> {
    operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T
}

inline fun <T> ShankModule.new(crossinline factory: () -> T) = object : NewProvider0<T> {
    override fun invoke(): T = factory()
}

inline fun <P1, T> ShankModule.new(crossinline factory: (P1) -> T) = object : NewProvider1<P1, T> {
    override fun invoke(p1: P1): T = factory(p1)
}

inline fun <P1, P2, T> ShankModule.new(crossinline factory: (P1, P2) -> T) = object : NewProvider2<P1, P2, T> {
    override fun invoke(p1: P1, p2: P2): T = factory(p1, p2)
}

inline fun <P1, P2, P3, T> ShankModule.new(crossinline factory: (P1, P2, P3) -> T) = object : NewProvider3<P1, P2, P3, T> {
    override fun invoke(p1: P1, p2: P2, p3: P3): T = factory(p1, p2, p3)
}

inline fun <P1, P2, P3, P4, T> ShankModule.new(crossinline factory: (P1, P2, P3, P4) -> T) = object : NewProvider4<P1, P2, P3, P4, T> {
    override fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): T = factory(p1, p2, p3, p4)
}

inline fun <P1, P2, P3, P4, P5, T> ShankModule.new(crossinline factory: (P1, P2, P3, P4, P5) -> T) = object : NewProvider5<P1, P2, P3, P4, P5, T> {
    override fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): T = factory(p1, p2, p3, p4, p5)
}
