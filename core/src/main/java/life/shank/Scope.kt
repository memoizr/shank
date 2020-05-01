package life.shank

import life.shank.Shank.internalInstancesInScopesCache
import life.shank.Shank.scopedInstancesClearActions
import java.io.Serializable

data class Scope(val value: Serializable) : Serializable {
    @Transient
    private var clearActions: HashSet<(Any?) -> Unit>? = null
    @Transient
    @JvmField
    val hashcode = value.hashCode()

    fun clear() {
        internalInstancesInScopesCache.get(hashcode)?.values?.filterNotNull()?.forEach { item ->
            clearActions?.forEach { action -> action(item) }
            scopedInstancesClearActions.forEach { action -> action(item) }
        }
        internalInstancesInScopesCache.remove(hashcode)
    }

    fun addOnClearAction(action: (Any?) -> Unit): Scope {
        var clearActions = this.clearActions
        if (clearActions == null) {
            clearActions = HashSet()
            this.clearActions = clearActions
        }
        clearActions.add(action)
        return this
    }

    fun removeOnClearAction(action: (Any?) -> Unit) = apply {
        clearActions?.remove(action)
    }

    override fun hashCode(): Int = hashcode
    override fun equals(other: Any?): Boolean = this.hashcode == other.hashCode()
}

interface Scoped {
    val scope: Scope

    operator fun <T> ScopedProvider0<T>.invoke() = this(scope)
    operator fun <P1, T> ScopedProvider1<P1, T>.invoke(p1: P1) = this(scope, p1)
    operator fun <P1, P2, T> ScopedProvider2<P1, P2, T>.invoke(p1: P1, p2: P2) = this(scope, p1, p2)
    operator fun <P1, P2, P3, T> ScopedProvider3<P1, P2, P3, T>.invoke(p1: P1, p2: P2, p3: P3) = this(scope, p1, p2, p3)
    operator fun <P1, P2, P3, P4, T> ScopedProvider4<P1, P2, P3, P4, T>.invoke(p1: P1, p2: P2, p3: P3, p4: P4) = this(scope, p1, p2, p3, p4)
    operator fun <P1, P2, P3, P4, P5, T> ScopedProvider5<P1, P2, P3, P4, P5, T>.invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5) = this(scope, p1, p2, p3, p4, p5)
}

data class InternalScoped(override val scope: Scope) : Scoped
