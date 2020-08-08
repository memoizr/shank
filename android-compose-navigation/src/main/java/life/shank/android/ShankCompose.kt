package life.shank.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.onDispose
import androidx.compose.runtime.remember
import com.koduok.compose.navigation.BackStackAmbient
import com.koduok.compose.navigation.core.BackStack.Listener
import com.koduok.compose.navigation.core.Route
import life.shank.Scope
import life.shank.Scoped
import java.util.UUID

val ScopeAmbient = ambientOf<Scope> { throw IllegalStateException("Scope not defined") }
private val scopesCache = mutableMapOf<ScopeKey, Scope>()

private data class ScopeKey(val route: Route<*>)
private class InternalScoped(override val scope: Scope) : Scoped

@Composable
fun RouteScope(createScope: () -> Scope = { Scope(UUID.randomUUID()) }, children: @Composable() Scoped.() -> Unit) {
    val backStack = BackStackAmbient.current
    var currentRoute = remember { backStack.current }

    val scope: Scope = remember {
        val scopeKey = ScopeKey(currentRoute)
        val existingScope = scopesCache[scopeKey]
        if (existingScope != null) return@remember existingScope

        val scope = createScope()
        scopesCache[scopeKey] = scope

        backStack.addListener(object : Listener<Any> {
            override fun onRemoved(route: Route<Any>) {
                if (route == currentRoute) {
                    scopesCache.remove(ScopeKey(route))?.clear()
                    backStack.removeListener(this)
                }
            }

            override fun onIndexChanged(route: Route<Any>, previousIndex: Int) {
                val previousRoute = route.copy(index = previousIndex)
                if (previousRoute == currentRoute) {
                    currentRoute = route
                    val cachedScope = scopesCache.remove(ScopeKey(previousRoute))
                    if (cachedScope != null) {
                        scopesCache[ScopeKey(route)] = cachedScope
                    }
                }
            }
        })

        scope
    }

    Providers(ScopeAmbient.provides(scope)) {
        children(InternalScoped(scope))
    }
}

@Composable
fun Scope(createScope: () -> Scope = { Scope(UUID.randomUUID()) }, children: @Composable() Scoped.() -> Unit) {
    val scope = remember { createScope() }
    onDispose {
        scope.clear()
    }

    Providers(ScopeAmbient.provides(scope)) {
        children(InternalScoped(scope))
    }
}

@Composable
fun ParentScope(children: @Composable Scoped.() -> Unit) {
    val scope = ScopeAmbient.current
    val scoped = remember { InternalScoped(scope) }
    children(scoped)
}
