package life.shank.android

import android.view.View
import android.view.View.OnAttachStateChangeListener
import life.shank.Scope
import java.util.UUID

internal class OnAttachListenerForScope(view: View) : OnAttachStateChangeListener {
    private val blocks = mutableSetOf<(Scope) -> Unit>()
    private var scope: Scope? = null

    init {
        if (view.isAttachedToWindow) onViewAttachedToWindow(view)
    }

    fun onScopeReady(block: (Scope) -> Unit) {
        blocks.add(block)

        val scope = scope
        if (scope != null) {
            block(scope)
        }
    }

    fun reset() {
        scope?.clear()
        scope = null
        blocks.clear()
    }

    override fun onViewAttachedToWindow(view: View) {
        onViewDetachedFromWindow(view)
        val scope = Scope(UUID.randomUUID())
        this.scope = scope
        blocks.forEach { it(scope) }
    }

    override fun onViewDetachedFromWindow(view: View) {
        scope?.clear()
        scope = null
    }
}