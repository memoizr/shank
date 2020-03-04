package life.shank.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import life.shank.Scope
import java.util.UUID

internal object AutoScopedFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
    private const val fragmentScopeKey = "shank_fragment_scope_key"

    override fun onFragmentPreCreated(fm: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
        if (fragment !is AutoScoped) return

        val scope = savedInstanceState?.getSerializable(fragmentScopeKey) as? Scope ?: Scope(UUID.randomUUID())
        LifecycleOwnerScopes.putScope(fragment, scope)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, fragment: Fragment) {
        if (fragment !is AutoScoped) return

        if (fragment.activity?.isFinishing == true || fragment.isRemoving) {
            LifecycleOwnerScopes.doOnScopeReady(fragment) { it.clear() }
        }
        LifecycleOwnerScopes.removeScope(fragment)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        if (f !is AutoScoped) return

        LifecycleOwnerScopes.doOnScopeReady(f) {
            outState.putSerializable(fragmentScopeKey, it)
        }
    }
}