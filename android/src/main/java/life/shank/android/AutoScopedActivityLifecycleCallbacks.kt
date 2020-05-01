package life.shank.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import life.shank.Scope
import java.util.*

internal object AutoScopedActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private const val activityScopeKey = "shank_activity_scope_key"

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is AutoScoped && activity is LifecycleOwner) {
            val scope = savedInstanceState?.getSerializable(activityScopeKey) as? Scope ?: Scope(UUID.randomUUID())
            LifecycleOwnerScopes.putScope(activity, scope)
        }

        (activity as? FragmentActivity)
            ?.supportFragmentManager
            ?.registerFragmentLifecycleCallbacks(AutoScopedFragmentLifecycleCallbacks, true)
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity !is AutoScoped || activity !is LifecycleOwner) return

        if (activity.isFinishing) {
            LifecycleOwnerScopes.doOnScopeReady(activity) { it.clear() }
        }
        LifecycleOwnerScopes.removeScope(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        if (activity !is AutoScoped || activity !is LifecycleOwner) return

        LifecycleOwnerScopes.doOnScopeReady(activity) {
            outState.putSerializable(activityScopeKey, it)
        }
    }

    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivityStarted(activity: Activity?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
}