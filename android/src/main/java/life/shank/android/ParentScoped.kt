@file:Suppress("NOTHING_TO_INLINE")

package life.shank.android

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import life.shank.Scope
import life.shank.Scoped

interface ParentScoped : AutoScoped {
    override fun onScopeReady(block: (Scope) -> Unit) {
        when (this) {
            is Scoped -> throw IllegalStateException("You cannot have Scoped and ParentScoped at the same time")
            is Fragment -> onParentScopeReadyForFragment(block)
            is View -> onParentScopeReadyForView(block)
            else -> throw IllegalArgumentException("$this is not supported")
        }
    }
}

private inline fun Fragment.onParentScopeReadyForFragment(noinline block: (Scope) -> Unit) {
    val scoped: Scoped?
    val autoScoped: AutoScoped?

    val scopedOrAutoScopedFragment = parentFragment?.findClosestScopedOrAutoScopedFragment()

    if (scopedOrAutoScopedFragment != null) {
        scoped = scopedOrAutoScopedFragment as? Scoped
        autoScoped = scopedOrAutoScopedFragment as? AutoScoped
    } else {
        scoped = activity as? Scoped
        autoScoped = activity as? AutoScoped
    }

    when {
        scoped != null -> block(scoped.scope)
        autoScoped != null -> autoScoped.onScopeReady(block)
        else -> throw IllegalStateException("$this does not have any parent with Scope")
    }
}

private inline fun View.onParentScopeReadyForView(noinline block: (Scope) -> Unit) {
    if (id == View.NO_ID) throw IllegalArgumentException("$this must have an id")
    val activity = activity ?: throw IllegalArgumentException("$this does not have an Activity")

    var scoped: Scoped? = null
    var autoScoped: AutoScoped? = null

    Log.d("SHANK", "ParentScoped - Looking for scope for $this")

    var parentView = parent as? View
    while (parentView != null && scoped == null && autoScoped == null) {
        Log.d("SHANK", "ParentScoped - Checking $parentView")
        when (parentView) {
            is Scoped -> scoped = parentView
            is AutoScoped -> autoScoped = parentView
        }
        parentView = parentView.parent as? View
    }

    if (scoped == null && autoScoped == null && activity is FragmentActivity) {
        val fragment = activity.supportFragmentManager.findFragmentThatContains(this)
        Log.d("SHANK", "ParentScoped - Contains this view $fragment")
        val scopedOrAutoScopedFragment = fragment?.findClosestScopedOrAutoScopedFragment()
        Log.d("SHANK", "ParentScoped - Closest scoped $scopedOrAutoScopedFragment")
        scoped = scopedOrAutoScopedFragment as? Scoped
        autoScoped = scopedOrAutoScopedFragment as? AutoScoped
    }

    if (scoped == null && autoScoped == null) {
        scoped = activity as? Scoped
        autoScoped = activity as? AutoScoped
    }

    when {
        scoped != null -> block(scoped.scope)
        autoScoped != null -> autoScoped.onScopeReady(block)
        else -> throw IllegalStateException("$this does not have any parent with Scope")
    }
}

private inline fun Fragment.findClosestScopedOrAutoScopedFragment(): Fragment? {
    var fragment: Fragment? = this
    while (fragment != null && !(fragment is Scoped || fragment is AutoScoped)) {
        fragment = fragment.parentFragment
    }
    return fragment
}

private fun FragmentManager.findFragmentThatContains(view: View): Fragment? {
    return fragments.firstOrNull {
        val fragment = it.childFragmentManager.findFragmentThatContains(view)
        if (fragment != null) return fragment

        Log.d("SHANK", "ParentScoped - Checking $it")
        val contains = it.contains(view)
        if (contains)
            Log.d("SHANK", "ParentScoped - $it contains $view")
        else
            Log.d("SHANK", "ParentScoped - $it does not contain $view")
        contains
    }
}

private inline fun Fragment.contains(view: View) = view == this.view?.findViewById(view.id)

private val View.activity: Activity? get() = context.activity

private val Context.activity: Activity?
    get() = when (this) {
        is Activity -> this
        is ContextThemeWrapper -> this.baseContext.activity
        is android.view.ContextThemeWrapper -> this.baseContext.activity
        is ContextWrapper -> this.baseContext.activity
        else -> null
    }