@file:Suppress("NOTHING_TO_INLINE")

package life.shank.android

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.children
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import life.shank.Scope
import life.shank.Scoped
import java.util.UUID


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
    doOnAttach {
        val activity = activity ?: throw IllegalArgumentException("$this does not have an Activity")

        var scoped: Scoped? = null
        var autoScoped: AutoScoped? = null

        var parentView = parent as? View
        while (parentView != null && scoped == null && autoScoped == null) {
            when (parentView) {
                is Scoped -> scoped = parentView
                is AutoScoped -> autoScoped = parentView
            }
            parentView = parentView.parent as? View
        }

        if (scoped == null && autoScoped == null && activity is FragmentActivity) {
            val tag = getTag(R.id.shank_view_tag_to_find_in_parent) ?: UUID.randomUUID()
            setTag(R.id.shank_view_tag_to_find_in_parent, tag)

            val fragment = activity.supportFragmentManager.findFragmentThatContains(this)
            val scopedOrAutoScopedFragment = fragment?.findClosestScopedOrAutoScopedFragment()

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
        it.contains(view)
    }
}

private inline fun Fragment.contains(view: View) = view == this.view
    ?.findViewWithTag(R.id.shank_view_tag_to_find_in_parent, view.getTag(R.id.shank_view_tag_to_find_in_parent))

fun View.findViewWithTag(@IdRes idRes: Int, tag: Any): View? {
    if (this.getTag(idRes) == tag) return this
    if (this !is ViewGroup) return null
    return children.firstOrNull { it.findViewWithTag(idRes, tag) != null }
}

private val View.activity: Activity? get() = context.activity
private val Context.activity: Activity?
    get() = when (this) {
        is Activity -> this
        is ContextThemeWrapper -> this.baseContext.activity
        is android.view.ContextThemeWrapper -> this.baseContext.activity
        is ContextWrapper -> this.baseContext.activity
        else -> null
    }