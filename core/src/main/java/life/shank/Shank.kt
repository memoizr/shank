package life.shank

interface ShankModule

object Shank {
    internal val scopedInstancesClearActions = HashSet<(Any?) -> Unit>()
    val internalSingletonInstanceCache by lazy { HashcodeHashMap<Any?>() }
    val internalInstancesInScopesCache = HashcodeHashMap<HashcodeHashMap<Any?>>()

    fun addScopedInstanceClearAction(action: (Any?) -> Unit) {
        scopedInstancesClearActions.add(action)
    }

    fun removeScopedInstanceClearAction(action: (Any?) -> Unit) {
        scopedInstancesClearActions.remove(action)
    }
}
