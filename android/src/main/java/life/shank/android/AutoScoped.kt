package life.shank.android

import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.lifecycle.LifecycleOwner
import life.shank.Scope
import life.shank.Scoped
import life.shank.ScopedProvider0
import life.shank.ScopedProvider1
import life.shank.ScopedProvider2
import life.shank.ScopedProvider3
import life.shank.ScopedProvider4
import life.shank.ScopedProvider5
import java.util.UUID

interface AutoScoped {
    fun onScopeReady(block: (Scope) -> Unit) {
        when (this) {
            is Scoped -> block(scope)
            is LifecycleOwner -> LifecycleOwnerScopes.doOnScopeReady(this, block)
            is View -> onScopeReadyFor(this, "", block)
            else -> throw IllegalArgumentException()
        }
    }
}

internal fun onScopeReadyFor(view: View, id: Any, block: (Scope) -> Unit) {
    val scopeOnAttachStateChangeListener = view.getTag(R.id.shank_view_tag) as? OnAttachListenerForScope ?: OnAttachListenerForScope(view)
    scopeOnAttachStateChangeListener.put(id, block)
    view.setTag(R.id.shank_view_tag, scopeOnAttachStateChangeListener)
}

fun <VIEW> VIEW.onScopeReady(id: Any, block: (Scope) -> Unit) where VIEW : View, VIEW : AutoScoped {
    onScopeReadyFor(this, id, block)
}

private class OnAttachListenerForScope(view: View) : OnAttachStateChangeListener {
    private val blocks = hashMapOf<Any, (Scope) -> Unit>()
    private var scope: Scope? = null

    init {
        if (view.isAttachedToWindow) onViewAttachedToWindow(view)
    }

    fun put(uniqueBlockId: Any, block: (Scope) -> Unit) {
        blocks[uniqueBlockId] = block
        scope?.run { block(this) }
    }

    override fun onViewAttachedToWindow(view: View) {
        onViewDetachedFromWindow(view)
        val scope = Scope(UUID.randomUUID())
        this.scope = scope
        blocks.values.forEach { it(scope) }
    }

    override fun onViewDetachedFromWindow(view: View) {
        scope?.clear()
        scope = null
    }
}

fun <T, VIEW> ScopedProvider0<T>.onReadyFor(view: VIEW, id: Any = "", block: (T) -> Unit)
        where VIEW : View, VIEW : AutoScoped = view.onScopeReady(id) { block(invoke(it)) }

fun <T> ScopedProvider0<T>.onReadyFor(autoScoped: AutoScoped, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it)) }

fun <P1, T, VIEW> ScopedProvider1<P1, T>.onReadyFor(view: VIEW, p1: P1, id: Any = "", block: (T) -> Unit)
        where VIEW : View, VIEW : AutoScoped = view.onScopeReady(id) { block(invoke(it, p1)) }

fun <P1, T> ScopedProvider1<P1, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1)) }

fun <P1, P2, T, VIEW> ScopedProvider2<P1, P2, T>.onReadyFor(view: VIEW, p1: P1, p2: P2, id: Any = "", block: (T) -> Unit)
        where VIEW : View, VIEW : AutoScoped = view.onScopeReady(id) { block(invoke(it, p1, p2)) }

fun <P1, P2, T> ScopedProvider2<P1, P2, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2)) }

fun <P1, P2, P3, T, VIEW> ScopedProvider3<P1, P2, P3, T>.onReadyFor(view: VIEW, p1: P1, p2: P2, p3: P3, id: Any = "", block: (T) -> Unit)
        where VIEW : View, VIEW : AutoScoped = view.onScopeReady(id) { block(invoke(it, p1, p2, p3)) }

fun <P1, P2, P3, T> ScopedProvider3<P1, P2, P3, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2, p3)) }

fun <P1, P2, P3, P4, T, VIEW> ScopedProvider4<P1, P2, P3, P4, T>.onReadyFor(view: VIEW, p1: P1, p2: P2, p3: P3, p4: P4, id: Any = "", block: (T) -> Unit)
        where VIEW : View, VIEW : AutoScoped = view.onScopeReady(id) { block(invoke(it, p1, p2, p3, p4)) }

fun <P1, P2, P3, P4, T> ScopedProvider4<P1, P2, P3, P4, T>.onReadyFor(autoScoped: AutoScoped, p1: P1, p2: P2, p3: P3, p4: P4, block: (T) -> Unit) =
    autoScoped.onScopeReady { block(invoke(it, p1, p2, p3, p4)) }

fun <P1, P2, P3, P4, P5, T, VIEW> ScopedProvider5<P1, P2, P3, P4, P5, T>.onReadyFor(
    view: VIEW,
    p1: P1,
    p2: P2,
    p3: P3,
    p4: P4,
    p5: P5,
    id: Any = "",
    block: (T) -> Unit
) where VIEW : View, VIEW : AutoScoped = view.onScopeReady(id) { block(invoke(it, p1, p2, p3, p4, p5)) }

fun <P1, P2, P3, P4, P5, T> ScopedProvider5<P1, P2, P3, P4, P5, T>.onReadyFor(
    autoScoped: AutoScoped,
    p1: P1,
    p2: P2,
    p3: P3,
    p4: P4,
    p5: P5,
    block: (T) -> Unit
) = autoScoped.onScopeReady { block(invoke(it, p1, p2, p3, p4, p5)) }
