package dev.entao.ui.list

import android.os.Handler
import android.os.Message
import android.view.View

/**
 * Created by yet on 2015/10/26.
 */
class MoveHandler(internal var view: View, to: Int) : Handler() {
    var stepX = 0
    var fromX = 0
    var toX = 0

    init {
        this.fromX = view.scrollX
        this.toX = to
        stepX = ((toX - fromX).toDouble() * DURATION_STEP.toDouble() * 1.0 / DURATION).toInt()
    }

    override fun handleMessage(msg: Message) {
        if (Math.abs(toX - fromX) <= Math.max(Math.abs(stepX), 10)) {
            view.scrollTo(toX, 0)
        } else {
            fromX += stepX
            view.scrollTo(fromX, 0)
            this.sendEmptyMessageDelayed(0, DURATION_STEP.toLong())
        }
    }

    companion object {
        private const val DURATION = 100
        private const val DURATION_STEP = 10
    }
}