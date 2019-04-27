package dev.entao.kan.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import dev.entao.kan.appbase.App
import java.util.*

/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
class WaveView(context: Context) : View(context) {
    private var maxVal = 0
    private var data: MutableList<Int> = ArrayList()
    private val paint = Paint()

    init {
        paint.color = Color.GRAY
        paint.strokeWidth = App.dp2px(2).toFloat()
    }

    fun clearData() {
        data.clear()
        postInvalidate()
    }

    fun setColor(color: Int): WaveView {
        paint.color = color
        return this
    }

    fun setStrokeWidth(dp: Int): WaveView {
        paint.strokeWidth = App.dp2px(dp).toFloat()
        return this
    }

    override fun onDraw(canvas: Canvas) {
        val ls: ArrayList<Int>
        synchronized(this) {
            ls = ArrayList(data)
        }
        val size = ls.size
        if (size == 0) {
            return
        }
        val w = width
        val h = height
        val scaleH = h * 1.0 / maxVal
        for (i in ls.indices) {
            ls[i] = (ls[i] * scaleH).toInt()
        }

        val f = w * 1.0 / size//0.5
        var left = 0
        for (i in 0 until size) {
            val `val` = ls[i]
            left = (i * f).toInt()
            canvas.drawLine(left.toFloat(), (h - `val`).toFloat(), left.toFloat(), h.toFloat(), paint)
        }

    }

    fun setMaxValue(max: Int): WaveView {
        this.maxVal = max
        return this
    }

    fun setValue(ls: MutableList<Int>): WaveView {
        synchronized(this) {
            data = ls
        }
        this.postInvalidate()
        return this
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.GONE
    }
}
