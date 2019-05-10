package life.shank

interface SingleProvider0<T> : Provider0<T> {
    infix fun override(f: (() -> T)?): SingleProvider0<T>
}

interface SingleProvider1<A, T> : Provider1<A, T> {
    infix fun override(f: ((A) -> T)?): SingleProvider1<A, T>
}

interface SingleProvider2<A, B, T> : Provider2<A, B, T> {
    infix fun override(f: ((A, B) -> T)?): SingleProvider2<A, B, T>
}

interface SingleProvider3<A, B, C, T> : Provider3<A, B, C, T> {
    infix fun override(f: ((A, B, C) -> T)?): SingleProvider3<A, B, C, T>
}

inline fun <T> ShankModule.single(crossinline factory: () -> T) = object : SingleProvider0<T> {
    private var cache: T? = null
    private var o: (() -> T)? = null
    override fun override(f: (() -> T)?) = also { o = f }.also { cache = null }

    @Synchronized
    override fun invoke(): T = cache ?: (o?.invoke() ?: factory()).also { cache = it }
}

inline fun <A, T> ShankModule.single(crossinline factory: (A) -> T) = object : SingleProvider1<A, T> {
    private var cache: HashcodeHashMap<T>? = null
    private var o: ((A) -> T)? = null
    override fun override(f: ((A) -> T)?) = also { o = f }.also { cache = null }

    @Synchronized
    override fun invoke(a: A): T = cache?.get(this or a)
        ?: (o?.invoke(a) ?: factory(a)).also {
            (cache ?: apply { cache = HashcodeHashMap() }.cache)
                ?.put(this or a, it)
        }
}

inline fun <A, B, T> ShankModule.single(crossinline factory: (A, B) -> T) = object : SingleProvider2<A, B, T> {
    private var cache: HashcodeHashMap<T>? = null
    private var o: ((A, B) -> T)? = null
    override fun override(f: ((A, B) -> T)?) = also { o = f }.also { cache = null }

    @Synchronized
    override fun invoke(a: A, b: B): T = cache?.get(this or a or b)
        ?: (o?.invoke(a, b) ?: factory(a, b)).also {
            (cache ?: apply { cache = HashcodeHashMap() }.cache)
                ?.put(this or a or b, it)
        }
}

inline fun <A, B, C, T> ShankModule.single(crossinline factory: (A, B, C) -> T) = object : SingleProvider3<A, B, C, T> {
    private var cache: HashcodeHashMap<T>? = null
    private var o: ((A, B, C) -> T)? = null
    override fun override(f: ((A, B, C) -> T)?) = also { o = f }.also { cache = null }

    @Synchronized
    override fun invoke(a: A, b: B, c: C): T = cache?.get(this or a or b or c)
        ?: (o?.invoke(a, b, c) ?: factory(a, b, c)).also {
            (cache ?: apply { cache = HashcodeHashMap() }.cache)
                ?.put(this or a or b or c, it)
        }
}

