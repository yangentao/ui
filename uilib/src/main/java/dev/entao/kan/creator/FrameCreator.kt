@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId
/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//FrameLayout
fun  ViewGroup.frame(param:ViewGroup.LayoutParams, block: FrameLayout.() -> Unit): FrameLayout {
	val v = this.createFrame()
	this.addView(v, param)
	v.block()
	return v
}

fun  ViewGroup.frame(index: Int, param:ViewGroup.LayoutParams, block: FrameLayout.() -> Unit): FrameLayout {
	val v = this.createFrame()
	this.addView(v, index, param)
	v.block()
	return v
}

fun  ViewGroup.frameBefore(ankor: View, param:ViewGroup.LayoutParams, block: FrameLayout.() -> Unit): FrameLayout {
	return this.frame(this.indexOfChild(ankor), param, block)
}

fun View.createFrame(): FrameLayout {
	return this.context.createFrame()
}

fun Fragment.createFrame(): FrameLayout {
	return this.act.createFrame()
}

fun Context.createFrame(): FrameLayout {
	return FrameLayout(this).genId()
}