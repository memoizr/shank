package life.shank

import life.shank._cache.factories

 class NewProvider<T> : Provider<T, () -> T> {
     operator inline fun invoke(): T = (Caster.cast<Function0<T>>(factories[this])).invoke()
 }

class NewProvider1<A, T> : Provider<T, (A) -> T> {
    operator fun invoke(a: A): T = factories[this].i(a)

}

class NewProvider2<A, B, T> : Provider<T, (A, B) -> T> {
    operator fun invoke(a: A, b: B): T = factories[this].i(a, b)
}

class NewProvider3<A, B, C, T> : Provider<T, (A, B, C) -> T> {
    operator fun invoke(a: A, b: B, c: C): T = factories[this].i(a, b, c)
}

class NewProvider4<A, B, C, D, T> : Provider<T, (A, B, C, D) -> T> {
    operator fun invoke(a: A, b: B, c: C, d: D): T = factories[this].i(a, b, c, d)
}

class NewProvider5<A, B, C, D, E, T> : Provider<T, (A, B, C, D, E) -> T> {
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = factories[this].i(a, b, c, d, e)
}
