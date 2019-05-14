package life.shank

import java.io.Serializable

data class Scope(val value: Serializable, val parent: Scope? = null) : Serializable {
    @Transient
    private var children: ArrayList<Scope>? = null
    @Transient
    private var clearActions: HashSet<() -> Unit>? = null
    @Transient
    @JvmField
    val hashcode = value.hashCode() * 31 + parent.hashCode()

    fun clear() {
        ShankScopedCache.scopedCache.get(this.hashcode)?.values?.forEach { item ->
            ShankScopedCache.globalOnClearActions.forEach { action ->
                action(item)
            }

        }
        ShankScopedCache.scopedCache.remove(this.hashcode)
        ShankGlobalCache.globalCache.clear()
        children?.forEach { it.clear() }
        clearActions?.forEach { it() }
        clearActions = null
    }

    fun nest() = copy(value = Any().hashCode(), parent = this)
        .also { (children ?: apply { children = ArrayList() }.children!!).add(it) }

    fun addOnClearAction(action: () -> Unit): Scope =
        also { (clearActions ?: apply { clearActions = HashSet() }.clearActions!!).add(action) }

    fun removeOnClearAction(action: () -> Unit) = also {
        clearActions?.remove(action)
    }

    override fun hashCode(): Int = hashcode
    override fun equals(other: Any?): Boolean = this.hashcode == other.hashCode()
}

fun Scope.clearWithAction(action: (Any?) -> Unit) {
    ShankScopedCache.scopedCache.also { it[this]?.values?.forEach { action(it) } }.remove(this.hashcode)
}

fun addGlobalOnClearAction(action: (Any?) -> Unit) {
    ShankScopedCache.globalOnClearActions.add(action)
}

interface ScopedFactory : Scoped
internal data class SSF(override val scope: Scope) : ScopedFactory

interface Scoped {
    val scope: Scope

    operator fun <T> ScopedProvider0<T>.invoke() = this(scope)
    operator fun <A, T> ScopedProvider1<A, T>.invoke(a: A) = this(scope, a)
    operator fun <A, B, T> ScopedProvider2<A, B, T>.invoke(a: A, b: B) = this(scope, a, b)
    operator fun <A, B, C, T> ScopedProvider3<A, B, C, T>.invoke(a: A, b: B, c: C) = this(scope, a, b, c)
}
