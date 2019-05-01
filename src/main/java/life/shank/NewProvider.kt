package life.shank

interface NewProvider0<T> : Provider<T, () -> T> {
    infix fun override(f: () -> T): NewProvider0<T>
    operator fun invoke(): T
}

interface NewProvider1<A, T> : Provider<T, (A) -> T> {
    infix fun override(f: (A) -> T): NewProvider1<A, T>
    operator fun invoke(a: A): T
}

interface NewProvider2<A, B, T> : Provider<T, (A, B) -> T> {
    infix fun override(f: (A, B) -> T): NewProvider2<A, B, T>
    operator fun invoke(a: A, b: B): T
}

interface NewProvider3<A, B, C, T> : Provider<T, (A, B, C) -> T> {
    infix fun override(f: (A, B, C) -> T): NewProvider3<A, B, C, T>
    operator fun invoke(a: A, b: B, c: C): T
}

interface NewProvider4<A, B, C, D, T> : Provider<T, (A, B, C, D) -> T> {
    infix fun override(f: (A, B, C, D) -> T): NewProvider4<A, B, C, D, T>
    operator fun invoke(a: A, b: B, c: C, d: D): T
}

interface NewProvider5<A, B, C, D, E, T> : Provider<T, (A, B, C, D, E) -> T> {
    infix fun override(f: (A, B, C, D, E) -> T): NewProvider5<A, B, C, D, E, T>
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T
}

inline fun <T> ShankModule.new(crossinline factory: () -> T) = object : NewProvider0<T> {
    private var o: (() -> T)? = null
    override fun override(f: () -> T): NewProvider0<T> = apply { o = f }
    override fun invoke(): T = o?.invoke() ?: factory()
}

inline fun <A, T> ShankModule.new(crossinline factory: (A) -> T) = object : NewProvider1<A, T> {
    private var o: ((A) -> T)? = null
    override fun override(f: (A) -> T): NewProvider1<A, T> = apply { o = f }
    override fun invoke(a: A): T = o?.invoke(a) ?: factory(a)
}

inline fun <A, B, T> ShankModule.new(crossinline factory: (A, B) -> T) = object : NewProvider2<A, B, T> {
    private var o: ((A, B) -> T)? = null
    override fun override(f: (A, B) -> T): NewProvider2<A, B, T> = apply { o = f }
    override fun invoke(a: A, b: B): T = o?.invoke(a, b) ?: factory(a, b)
}

inline fun <A, B, C, T> ShankModule.new(crossinline factory: (A, B, C) -> T) = object : NewProvider3<A, B, C, T> {
    private var o: ((A, B, C) -> T)? = null
    override fun override(f: (A, B, C) -> T): NewProvider3<A, B, C, T> = apply { o = f }
    override fun invoke(a: A, b: B, c: C): T = o?.invoke(a, b, c) ?: factory(a, b, c)
}

inline fun <A, B, C, D, T> ShankModule.new(crossinline factory: (A, B, C, D) -> T) = object : NewProvider4<A, B, C, D, T> {
    private var o: ((A, B, C, D) -> T)? = null
    override fun override(f: (A, B, C, D) -> T): NewProvider4<A, B, C, D, T> = apply { o = f }
    override fun invoke(a: A, b: B, c: C, d: D): T = o?.invoke(a, b, c, d) ?: factory(a, b, c, d)
}

inline fun <A, B, C, D, E, T> ShankModule.new(crossinline factory: (A, B, C, D, E) -> T) = object : NewProvider5<A, B, C, D, E, T> {
    private var o: ((A, B, C, D, E) -> T)? = null
    override fun override(f: (A, B, C, D, E) -> T): NewProvider5<A, B, C, D, E, T> = apply { o = f }
    override fun invoke(a: A, b: B, c: C, d: D, e: E): T = o?.invoke(a, b, c, d, e) ?: factory(a, b, c, d, e)
}
