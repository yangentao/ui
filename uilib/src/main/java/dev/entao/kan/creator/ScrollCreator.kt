@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//ScrollView
fun ViewGroup.scroll(param: ViewGroup.LayoutParams, block: ScrollView.() -> Unit): ScrollView {
	val v = this.createScroll()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.scroll(index: Int, param: ViewGroup.LayoutParams, block: ScrollView.() -> Unit): ScrollView {
	val v = this.createScroll()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.scrollBefore(ankor: View, param: ViewGroup.LayoutParams, block: ScrollView.() -> Unit): ScrollView {
	return this.scroll(this.indexOfChild(ankor), param, block)
}

fun View.createScroll(): ScrollView {
	return this.context.createScroll()
}

fun Fragment.createScroll(): ScrollView {
	return this.act.createScroll()
}

fun Context.createScroll(): ScrollView {
	return ScrollView(this).genId()
}


