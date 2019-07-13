@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//RelativeLayout
fun ViewGroup.relative(param: ViewGroup.LayoutParams, block: RelativeLayout.() -> Unit): RelativeLayout {
	val v = this.createRelative()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.relative(index: Int, param: ViewGroup.LayoutParams, block: RelativeLayout.() -> Unit): RelativeLayout {
	val v = this.createRelative()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.relativeBefore(ankor: View, param: ViewGroup.LayoutParams, block: RelativeLayout.() -> Unit): RelativeLayout {
	return this.relative(this.indexOfChild(ankor), param, block)
}

fun View.createRelative(): RelativeLayout {
	return this.context.createRelative()
}

fun Fragment.createRelative(): RelativeLayout {
	return this.act.createRelative()
}

fun Context.createRelative(): RelativeLayout {
	return RelativeLayout(this).genId()
}
