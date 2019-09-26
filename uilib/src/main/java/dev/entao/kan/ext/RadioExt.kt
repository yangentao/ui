@file:Suppress("unused")

package dev.entao.kan.ext

import android.graphics.drawable.Drawable
import android.widget.RadioButton
import android.widget.RadioGroup
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.VState
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.appbase.ex.sized
import dev.entao.kan.creator.createRadioButton
import dev.entao.kan.res.D
import dev.entao.kan.ui.R

/**
 * Created by entaoyang@163.com on 2016-11-07.
 */

// IMAGE--Title------CHECK
fun <T : RadioButton> T.styleImageTextCheckRes(leftRes: Int): T {
    return this.styleImageTextCheckRes(leftRes, R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
}

fun <T : RadioButton> T.styleImageTextCheckRes(leftRes: Int, rightNormal: Int, rightChecked: Int): T {
    val rightImg = StateList.drawable(D.res(rightNormal), VState.Checked to D.res(rightChecked)).sized(15)
    this.buttonDrawable = null
    this.setCompoundDrawables(D.res(leftRes).sized(27), null, rightImg.sized(25), null)
    this.compoundDrawablePadding = dp(15)
    return this
}

fun <T : RadioButton> T.styleImageTextCheck(leftDraw: Drawable?): T {
    return styleImageTextCheck(leftDraw, D.res(R.drawable.yet_checkbox), D.res(R.drawable.yet_checkbox_checked))
}

fun <T : RadioButton> T.styleImageTextCheck(leftDraw: Drawable?, rightNormal: Drawable, rightChecked: Drawable): T {
    val rightImg = StateList.drawable(rightNormal, VState.Checked to rightChecked).sized(15)
    this.buttonDrawable = null
    this.setCompoundDrawables(leftDraw?.sized(27), null, rightImg.sized(25), null)
    this.compoundDrawablePadding = dp(15)
    return this
}


fun RadioGroup.addRadioButton(title: String, block: RadioGroup.LayoutParams.() -> RadioGroup.LayoutParams): RadioButton {
    val view = this.context.createRadioButton()
    view.text = title
    val lp = RadioParam.HeightButton.WidthFill
    lp.block()
    this.addView(view, lp)
    return view
}


val RadioParam: RadioGroup.LayoutParams
    get() {
        return RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
    }
