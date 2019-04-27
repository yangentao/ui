package dev.entao.ui.util

import android.view.MotionEvent
import android.view.View

/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
abstract class TouchMonitor {

    private var view: View? = null
    private val listener = View.OnTouchListener { v, event ->
        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> onDown(v)
            MotionEvent.ACTION_UP -> if (isInside(v, event)) {
                onUp(v)
            } else {
                onCancel(v)
            }
            MotionEvent.ACTION_CANCEL -> onCancel(v)
            MotionEvent.ACTION_OUTSIDE -> onCancel(v)
            MotionEvent.ACTION_MOVE -> onMove(v, isInside(v, event))
        }
        false
    }

    abstract fun onDown(view: View)

    abstract fun onUp(view: View)

    abstract fun onCancel(view: View)

    fun onMove(view: View, inside: Boolean) {}

    fun monitor(view: View) {
        if (view === this.view) {
            return
        }

        if (this.view != null) {
            this.view!!.setOnTouchListener(null)
        }
        this.view = view
        if (this.view != null) {
            this.view!!.setOnTouchListener(listener)
        }
    }

    private fun isInside(v: View, event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        return x >= 0 && y >= 0 && x < v.width && y < v.height
    }
}
