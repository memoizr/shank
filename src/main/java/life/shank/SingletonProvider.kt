package life.shank

import life.shank.ShankCache.globalScope

class SingletonProvider<T> : Provider<T, () -> T> {
    @JvmField val hashcode = hashCode()
    inline override fun hashCode(): Int = hashcode

    fun eager(): SingletonProvider<T> {
        invoke()
        return this
    }

    @Synchronized
    operator fun invoke(): T = get(globalScope, 1299821) { i() }
}

class SingletonProvider1<A, T> : Provider<T, (A) -> T> {
    @JvmField val hashcode = hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A): SingletonProvider1<A, T> {
        invoke(a)
        return this
    }

    @Synchronized
    operator fun invoke(a: A): T = get(globalScope, Params1(a)) { i(a) }
}

class SingletonProvider2<A, B, T> : Provider<T, (A, B) -> T> {
    @JvmField val hashcode = hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B): SingletonProvider2<A, B, T> {
        invoke(a, b)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B): T = get(globalScope, Params2(a, b)) { i(a, b) }
}


class SingletonProvider3<A, B, C, T> : Provider<T, (A, B, C) -> T> {
    @JvmField val hashcode = hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B, c: C): SingletonProvider3<A, B, C, T> {
        invoke(a, b, c)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B, c: C): T = get(globalScope, Params3(a, b, c)) { i(a, b, c) }
}

class SingletonProvider4<A, B, C, D, T> : Provider<T, (A, B, C, D) -> T> {
    @JvmField val hashcode = hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B, c: C, d: D): SingletonProvider4<A, B, C, D, T> {
        invoke(a, b, c, d)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B, c: C, d: D): T = get(globalScope, Params4(a, b, c, d)) { i(a, b, c, d) }
}

class SingletonProvider5<A, B, C, D, E, T> : Provider<T, (A, B, C, D, E) -> T> {
    @JvmField val hashcode = hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B, c: C, d: D, e: E): SingletonProvider5<A, B, C, D, E, T> {
        invoke(a, b, c, d, e)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = get(globalScope, Params5(a, b, c, d, e)) { i(a, b, c, d, e) }
}
