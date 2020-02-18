package life.shank

interface SingleProvider0<T> : Provider0<T>
interface SingleProvider1<A, T> : Provider1<A, T>
interface SingleProvider2<A, B, T> : Provider2<A, B, T>
interface SingleProvider3<A, B, C, T> : Provider3<A, B, C, T>
interface SingleProvider4<A, B, C, D, T> : Provider4<A, B, C, D, T>
interface SingleProvider5<A, B, C, D, E, T> : Provider5<A, B, C, D, E, T>

inline fun <T> ShankModule.single(crossinline factory: () -> T) = object : SingleProvider0<T> {
    private val cache by lazy { HashcodeHashMap<T>() }

    @Synchronized
    override fun invoke(): T = cache.get(this) ?: factory().also { cache.put(this, it) }
}

inline fun <A, T> ShankModule.single(crossinline factory: (A) -> T) = object : SingleProvider1<A, T> {
    private val cache by lazy { HashcodeHashMap<T>() }

    @Synchronized
    override fun invoke(a: A): T = cache.get(this or a) ?: factory(a).also { cache.put(this or a, it) }
}

inline fun <A, B, T> ShankModule.single(crossinline factory: (A, B) -> T) = object : SingleProvider2<A, B, T> {
    private val cache by lazy { HashcodeHashMap<T>() }

    @Synchronized
    override fun invoke(a: A, b: B): T = cache.get(this or a or b) ?: factory(a, b).also { cache.put(this or a or b, it) }
}

inline fun <A, B, C, T> ShankModule.single(crossinline factory: (A, B, C) -> T) = object : SingleProvider3<A, B, C, T> {
    private val cache by lazy { HashcodeHashMap<T>() }

    @Synchronized
    override fun invoke(a: A, b: B, c: C): T = cache.get(this or a or b or c) ?: factory(a, b, c).also { cache.put(this or a or b or c, it) }
}

inline fun <A, B, C, D, T> ShankModule.single(crossinline factory: (A, B, C, D) -> T) = object : SingleProvider4<A, B, C, D, T> {
    private val cache by lazy { HashcodeHashMap<T>() }

    @Synchronized
    override fun invoke(a: A, b: B, c: C, d: D): T = cache.get(this or a or b or c or d) ?: factory(a, b, c, d).also { cache.put(this or a or b or c or d, it) }
}

inline fun <A, B, C, D, E, T> ShankModule.single(crossinline factory: (A, B, C, D, E) -> T) = object : SingleProvider5<A, B, C, D, E, T> {
    private val cache by lazy { HashcodeHashMap<T>() }

    @Synchronized
    override fun invoke(a: A, b: B, c: C, d: D, e: E): T =
        cache.get(this or a or b or c or d or e) ?: factory(a, b, c, d, e).also { cache.put(this or a or b or c or d or e, it) }
}
