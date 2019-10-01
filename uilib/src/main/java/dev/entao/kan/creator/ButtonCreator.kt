@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import dev.entao.kan.appbase.ex.dpf
import dev.entao.kan.base.act
import dev.entao.kan.ext.*

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun LinearLayout.buttonGreenRound(block: Button.() -> Unit): Button {
    return this.button(LParam.WidthFill.HeightButton.margins(15)) {
        styleGreenRound()
        this.block()
    }
}

fun LinearLayout.buttonRedRound(block: Button.() -> Unit): Button {
    return this.button(LParam.WidthFill.HeightButton.margins(15)) {
        styleRedRound()
        this.block()
    }
}

//Button
fun ViewGroup.button(param: ViewGroup.LayoutParams, block: Button.() -> Unit): Button {
    val v = this.createButton()
    this.addView(v, param)
    v.block()
    return v
}

fun ViewGroup.button(index: Int, param: ViewGroup.LayoutParams, block: Button.() -> Unit): Button {
    val v = this.createButton()
    this.addView(v, index, param)
    v.block()
    return v
}

fun ViewGroup.buttonBefore(ankor: View, param: ViewGroup.LayoutParams, block: Button.() -> Unit): Button {
    return this.button(this.indexOfChild(ankor), param, block)
}

fun View.createButton(text: String = ""): Button {
    return this.context.createButton(text)
}

fun Fragment.createButton(text: String = ""): Button {
    return this.act.createButton(text)
}

fun Context.createButton(text: String = ""): Button {
    val b = Button(this).genId().text(text).textSizeB().padding(3)
    b.stateListAnimator = null
    b.elevation = 6.dpf
    return b
}
