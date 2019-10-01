@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.kan.ext

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.view.View.OnLayoutChangeListener
import android.widget.CheckBox
import dev.entao.kan.appbase.ex.*
import dev.entao.kan.base.ColorX

/**
 * Created by entaoyang@163.com on 16/6/4.
 */

//width:60, height:30
open class SwitchButton(context: Context) : CheckBox(context) {
    private val onLayoutChange = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        this.post {
            resetImage()
        }
    }

    init {
        this.compoundDrawablePadding = 0
        this.addOnLayoutChangeListener(this.onLayoutChange)
    }

    override fun setChecked(checked: Boolean) {
        val old = this.isChecked
        super.setChecked(checked)
        if (old != checked) {
            resetImage()
        }
    }

    fun makeDrawDp(w: Int, h: Int): LayerDrawable {
        val dd1 = ShapeRect(Color.WHITE, h / 2).stroke(1, ColorX.borderGray).size(w, h).value
        val dd2 = ShapeRect(ColorX.green, h / 2).size(w, h).value
        val dd3 = ShapeRect(ColorX.backDisabled, h / 2).stroke(1, Color.WHITE).size(w, h).value
        val draw = StateList.drawable(dd1, VState.Checked to dd2, VState.Disabled to dd3)
        val h2: Int = if (h <= 2) {
            1
        } else {
            h - 2
        }
        val a = ShapeOval().fill(Color.WHITE)
        if (isChecked) {
            a.stroke(1, ColorX.borderGray)
        }
        val round = a.size(h2).value

        val ld = LayerDrawable(arrayOf(draw, round))
        val offset = dp(w - h)
        if (this.isChecked) {
            ld.setLayerInset(1, offset, 1, 1, 1)
        } else {
            ld.setLayerInset(1, 1, 1, offset, 1)
        }


        return ld
    }

    fun resetImage() {
        this.buttonDrawable = makeDrawDp(px2dp(this.width), px2dp(this.height))
    }

    companion object {
        const val WIDTH = 60
        const val HEIGHT = 30
    }
}
