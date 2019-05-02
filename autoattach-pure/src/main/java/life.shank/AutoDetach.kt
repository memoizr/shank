package life.shank

interface AutoDetachable

interface DetachAware<V : AutoDetachable> {
    fun attach(v: V)
    fun detach(v: V)
}
