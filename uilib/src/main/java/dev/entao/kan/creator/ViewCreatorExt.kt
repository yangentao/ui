@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId

/**
 * Created by entaoyang@163.com on 2016-07-22.
 */

fun <T : View> ViewGroup.append(child: T, param: ViewGroup.LayoutParams): T {
    this.addView(child, param)
    return child
}

fun <T : View> ViewGroup.append(child: T, param: ViewGroup.LayoutParams, block: T.() -> Unit): T {
    this.addView(child, param)
    child.block()
    return child
}

fun ViewGroup.addViewBefore(child: View, ankor: View, param: ViewGroup.LayoutParams) {
    this.addView(child, this.indexOfChild(ankor), param)
}


//View
fun ViewGroup.view(param: ViewGroup.LayoutParams, block: View.() -> Unit): View {
    val v = this.createView()
    this.addView(v, param)
    v.block()
    return v
}

fun ViewGroup.view(index: Int, param: ViewGroup.LayoutParams, block: View.() -> Unit): View {
    val v = this.createView()
    this.addView(v, index, param)
    v.block()
    return v
}

fun ViewGroup.viewBefore(ankor: View, param: ViewGroup.LayoutParams, block: View.() -> Unit): View {
    return this.view(this.indexOfChild(ankor), param, block)
}

fun View.createView(): View {
    return this.context.createView()
}

fun Fragment.createView(): View {
    return this.act.createView()
}

fun Context.createView(): View {
    return View(this).genId()
}



























