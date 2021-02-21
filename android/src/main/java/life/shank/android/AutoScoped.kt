package life.shank.android

import android.view.View
import androidx.lifecycle.LifecycleOwner
import life.shank.Scope
import life.shank.Scoped
import life.shank.ScopedProvider0
import life.shank.ScopedProvider1
import life.shank.ScopedProvider2
import life.shank.ScopedProvider3
import life.shank.ScopedProvider4
import life.shank.ScopedProvider5

interface AutoScoped {
    val scope: Scope?
        get() = when (this) {
            is Scoped -> scope
            is LifecycleOwner -> LifecycleOwnerScopes.getScope(this)
            is View -> getScope()
            else -> throw IllegalArgumentException()
        }

    fun onScopeReady(block: (Scope) -> Unit) {
        when (this) {
            is Scoped -> block(scope)
            is LifecycleOwner -> LifecycleOwnerScopes.doOnScopeReady(this, block)
            is View -> onScopeReadyFor(this, block)
            else -> throw IllegalArgumentException()
        }
    }
}

internal fun onScopeReadyFor(view: View, block: (Scope) -> Unit) {
    val currentOnAttachListenerForScope = view.getTag(R.id.shank_view_tag) as? OnAttachListenerForScope
    val onAttachListenerForScope = currentOnAttachListenerForScope ?: OnAttachListenerForScope(view)

    onAttachListenerForScope.onScopeReady(block)

    view.setTag(R.id.shank_view_tag, onAttachListenerForScope)
    if (currentOnAttachListenerForScope == null) {
        view.addOnAttachStateChangeListener(onAttachListenerForScope)
    }
}

internal fun View.getScope(): Scope? {
    val currentOnAttachListenerForScope = getTag(R.id.shank_view_tag) as? OnAttachListenerForScope ?: return null
    return currentOnAttachListenerForScope.scope
}

fun <T> ScopedProvider0<T>.onReadyFor(autoScoped: AutoScoped, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it)) }

fun <T> ScopedProvider0<T>.doOnReadyFor(autoScoped: AutoScoped, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it)) }

fun <T> ScopedProvider0<T>.doNowIfReadyFor(autoScoped: AutoScoped, block: (T) -> Unit) =
    autoScoped.scope?.run { block(invoke(this)) }

fun <P1, T> ScopedProvider1<P1, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1)) }

fun <P1, T> ScopedProvider1<P1, T>.doOnReadyFor(autoScoped: AutoScoped, p1: P1, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1)) }

fun <P1, T> ScopedProvider1<P1, T>.doNowIfReadyFor(autoScoped: AutoScoped, p1: P1, block: (T) -> Unit) =
    autoScoped.scope?.run { block(invoke(this, p1)) }

fun <P1, P2, T> ScopedProvider2<P1, P2, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2)) }

fun <P1, P2, T> ScopedProvider2<P1, P2, T>.doOnReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2)) }

fun <P1, P2, T> ScopedProvider2<P1, P2, T>.doNowIfReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, block: (T) -> Unit) =
    autoScoped.scope?.run { block(invoke(this, p1, p2)) }

fun <P1, P2, P3, T> ScopedProvider3<P1, P2, P3, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2, p3)) }

fun <P1, P2, P3, T> ScopedProvider3<P1, P2, P3, T>.doOnReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2, p3)) }

fun <P1, P2, P3, T> ScopedProvider3<P1, P2, P3, T>.doNowIfReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, block: (T) -> Unit) =
    autoScoped.scope?.run { block(invoke(this, p1, p2, p3)) }

fun <P1, P2, P3, P4, T> ScopedProvider4<P1, P2, P3, P4, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, p4: P4, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2, p3, p4)) }

fun <P1, P2, P3, P4, T> ScopedProvider4<P1, P2, P3, P4, T>.doOnReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, p4: P4, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2, p3, p4)) }

fun <P1, P2, P3, P4, T> ScopedProvider4<P1, P2, P3, P4, T>.doNowIfReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, p4: P4, block: (T) -> Unit) =
    autoScoped.scope?.run { block(invoke(this, p1, p2, p3, p4)) }

fun <P1, P2, P3, P4, P5, T> ScopedProvider5<P1, P2, P3, P4, P5, T>.onReadyFor(
    autoScoped: AutoScoped,
    p1: P1,
    p2: P2,
    p3: P3,
    p4: P4,
    p5: P5,
    block: (T) -> Unit
) = autoScoped.onScopeReady { block(invoke(it, p1, p2, p3, p4, p5)) }

fun <P1, P2, P3, P4, P5, T> ScopedProvider5<P1, P2, P3, P4, P5, T>.doOnReadyFor(
    autoScoped: AutoScoped,
    p1: P1,
    p2: P2,
    p3: P3,
    p4: P4,
    p5: P5,
    block: (T) -> Unit
) = autoScoped.onScopeReady { block(invoke(it, p1, p2, p3, p4, p5)) }

fun <P1, P2, P3, P4, P5, T> ScopedProvider5<P1, P2, P3, P4, P5, T>.doNowIfReadyFor(
    autoScoped: AutoScoped,
    p1: P1,
    p2: P2,
    p3: P3,
    p4: P4,
    p5: P5,
    block: (T) -> Unit
) = autoScoped.scope?.run { block(invoke(this, p1, p2, p3, p4, p5)) }
