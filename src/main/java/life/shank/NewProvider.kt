package life.shank

import life.shank.Caster.cast
import life.shank._cache.factories

 class NewProvider<T> : Provider<T, () -> T> {
     inline operator fun invoke(): T = cast<Function0<T>>(factories[this]).invoke()
 }

class NewProvider1<A, T> : Provider<T, (A) -> T> {
    inline operator fun invoke(a: A): T = cast<Function1<A, T>>(factories[this]).invoke(a)
}

class NewProvider2<A, B, T> : Provider<T, (A, B) -> T> {
    inline operator fun invoke(a: A, b: B): T = cast<Function2<A, B, T>>(factories[this]).invoke(a, b)
}

class NewProvider3<A, B, C, T> : Provider<T, (A, B, C) -> T> {
    inline operator fun invoke(a: A, b: B, c: C): T = cast<Function3<A, B, C, T>>(factories[this]).invoke(a, b, c)
}

class NewProvider4<A, B, C, D, T> : Provider<T, (A, B, C, D) -> T> {
    inline operator fun invoke(a: A, b: B, c: C, d: D): T = cast<Function4<A, B, C, D, T>>(factories[this]).invoke(a, b, c, d)
}

class NewProvider5<A, B, C, D, E, T> : Provider<T, (A, B, C, D, E) -> T> {
    inline operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = cast<Function5<A, B, C, D, E, T>>(factories[this]).invoke(a, b, c, d, e)
}
