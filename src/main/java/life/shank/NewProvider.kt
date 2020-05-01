package life.shank

interface NewProvider0<T> : Provider0<T>
interface NewProvider1<A, T> : Provider1<A, T>
interface NewProvider2<A, B, T> : Provider2<A, B, T>
interface NewProvider3<A, B, C, T> : Provider3<A, B, C, T>
interface NewProvider4<A, B, C, D, T> : Provider4<A, B, C, D, T>
interface NewProvider5<A, B, C, D, E, T> : Provider5<A, B, C, D, E, T>

inline fun <T> ShankModule.new(crossinline factory: () -> T) = object : NewProvider0<T> {
    override fun invoke(): T = factory()
}

inline fun <A, T> ShankModule.new(crossinline factory: (A) -> T) = object : NewProvider1<A, T> {
    override fun invoke(a: A): T = factory(a)
}

inline fun <A, B, T> ShankModule.new(crossinline factory: (A, B) -> T) = object : NewProvider2<A, B, T> {
    override fun invoke(a: A, b: B): T = factory(a, b)
}

inline fun <A, B, C, T> ShankModule.new(crossinline factory: (A, B, C) -> T) = object : NewProvider3<A, B, C, T> {
    override fun invoke(a: A, b: B, c: C): T = factory(a, b, c)
}

inline fun <A, B, C, D, T> ShankModule.new(crossinline factory: (A, B, C, D) -> T) = object : NewProvider4<A, B, C, D, T> {
    override fun invoke(a: A, b: B, c: C, d: D): T = factory(a, b, c, d)
}

inline fun <A, B, C, D, E, T> ShankModule.new(crossinline factory: (A, B, C, D, E) -> T) = object : NewProvider5<A, B, C, D, E, T> {
    override fun invoke(a: A, b: B, c: C, d: D, e: E): T = factory(a, b, c, d, e)
}
