package life.shank

import life.shank.Caster.cast
import life.shank.ShankGlobalCache.globalCache
import life.shank._cache.factories

class SingletonProvider<T> : Provider<T, () -> T> {
    @JvmField val hashcode = super.hashCode()
    inline override fun hashCode(): Int = hashcode

    fun eager(): SingletonProvider<T> {
        invoke()
        return this
    }

    @Synchronized
    operator fun invoke(): T = getGlobal(hashcode, 1299821) { i() }
}

private inline fun <T, F : Function<T>> Provider<*, F>.getGlobal(h: Int, params: Params, f: Any?.() -> T): T =
    mash(params).let { hash -> cast<T>(globalCache[hash]) ?: factories[h].f().also { globalCache.put(hash, it) } }


class SingletonProvider1<A, T> : Provider<T, (A) -> T> {
    @JvmField val hashcode = super.hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A): SingletonProvider1<A, T> {
        invoke(a)
        return this
    }

    @Synchronized
    operator fun invoke(a: A): T = getGlobal(hashcode, Params1(a)) { i(a) }
}

class SingletonProvider2<A, B, T> : Provider<T, (A, B) -> T> {
    @JvmField val hashcode = super.hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B): SingletonProvider2<A, B, T> {
        invoke(a, b)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B): T = getGlobal(hashcode, Params2(a, b)) { i(a, b) }
}


class SingletonProvider3<A, B, C, T> : Provider<T, (A, B, C) -> T> {
    @JvmField val hashcode = super.hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B, c: C): SingletonProvider3<A, B, C, T> {
        invoke(a, b, c)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B, c: C): T = getGlobal(hashcode, Params3(a, b, c)) { i(a, b, c) }
}

class SingletonProvider4<A, B, C, D, T> : Provider<T, (A, B, C, D) -> T> {
    @JvmField val hashcode = super.hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B, c: C, d: D): SingletonProvider4<A, B, C, D, T> {
        invoke(a, b, c, d)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B, c: C, d: D): T = getGlobal(hashcode, Params4(a, b, c, d)) { i(a, b, c, d) }
}

class SingletonProvider5<A, B, C, D, E, T> : Provider<T, (A, B, C, D, E) -> T> {
    @JvmField val hashcode = super.hashCode()
    inline override fun hashCode(): Int = hashcode
    fun eager(a: A, b: B, c: C, d: D, e: E): SingletonProvider5<A, B, C, D, E, T> {
        invoke(a, b, c, d, e)
        return this
    }

    @Synchronized
    operator fun invoke(a: A, b: B, c: C, d: D, e: E): T = getGlobal(hashcode, Params5(a, b, c, d, e)) { i(a, b, c, d, e) }
}
