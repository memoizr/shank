package life.shank


interface Provider<T, F : Function<T>>

interface Provider0<T> : Provider<T, () -> T> {
    operator fun invoke(): T
}

interface Provider1<A, T> : Provider<T, (A) -> T> {
    operator fun invoke(a: A): T
}

interface Provider2<A, B, T> : Provider<T, (A, B) -> T> {
    operator fun invoke(a: A, b: B): T
}

interface Provider3<A, B, C, T> : Provider<T, (A, B, C) -> T> {
    operator fun invoke(a: A, b: B, c: C): T
}

interface Provider4<A, B, C, D, T> : Provider<T, (A, B, C, D) -> T> {
    operator fun invoke(a: A, b: B, c: C, d: D): T
}

interface Provider5<A, B, C, D, E, T> : Provider<T, (A, B, C, D, E) -> T> {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T
}

