package life.shank.android

import androidx.lifecycle.LifecycleOwner
import life.shank.Scope

internal object LifecycleOwnerScopes {
    private val onScopeReadyActions = mutableListOf<Pair<LifecycleOwner, (Scope) -> Unit>>()
    private val scopes = mutableMapOf<LifecycleOwner, Scope>()

    fun doOnScopeReady(lifecycleOwner: LifecycleOwner, onScopeReady: (Scope) -> Unit) {
        val scope = scopes[lifecycleOwner]
        if (scope != null) {
            onScopeReady(scope)
        } else {
            onScopeReadyActions.add(lifecycleOwner to onScopeReady)
        }
    }

    fun putScope(lifecycleOwner: LifecycleOwner, scope: Scope) {
        scopes[lifecycleOwner] = scope
        onScopeReadyActions.filter { it.first == lifecycleOwner }.forEach { it.second(scope) }
        onScopeReadyActions.removeAll { it.first == lifecycleOwner }
    }

    fun removeScope(lifecycleOwner: LifecycleOwner) {
        scopes.remove(lifecycleOwner)
        onScopeReadyActions.removeAll { it.first == lifecycleOwner }
    }
}