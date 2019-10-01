@file:Suppress("unused")

package dev.entao.kan.ext

import android.graphics.Color
import android.widget.Switch
import dev.entao.kan.appbase.ex.*
import dev.entao.kan.base.ColorX

/**
 * Created by entaoyang@163.com on 16/6/3.
 */


fun <T : Switch> T.themeSwitch(): T {
    this.thumbTextPadding = dp(10)

    val w1 = 30
    val h1 = 30

    val d1 = ShapeRect(0xFFCCCCCCL.argb, h1 / 2).size(w1, h1).value
    val d2 = ShapeRect(0xFF4A90E2L.argb, h1 / 2).size(w1, h1).value
    val d3 = ShapeRect(ColorX.backDisabled, h1 / 2).stroke(1, ColorX.borderGray).size(w1, h1).value
    this.thumbDrawable = StateList.drawable(d1, VState.Checked to d2, VState.Disabled to d3)

    val w = 50
    val h = 30
    val dd1 = ShapeRect(Color.WHITE, h / 2).stroke(1, ColorX.borderGray).size(w, h).value
    val dd2 = ShapeRect(ColorX.green, h / 2).size(w, h).value
    val dd3 = ShapeRect(ColorX.backDisabled, h / 2).stroke(1, Color.WHITE).size(w, h).value
    this.trackDrawable = StateList.drawable(dd1, VState.Checked to dd2, VState.Disabled to dd3)
    return this
}
