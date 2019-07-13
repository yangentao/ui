@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId
import dev.entao.kan.grid.SimpleGridView

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun ViewGroup.simpleGridView(index: Int, param: ViewGroup.LayoutParams, block: SimpleGridView.() -> Unit): SimpleGridView {
	val lv = SimpleGridView(context)
	this.addView(lv, index, param)
	lv.block()
	return lv
}

fun ViewGroup.simpleGridView(param: ViewGroup.LayoutParams, block: SimpleGridView.() -> Unit): SimpleGridView {
	val lv = SimpleGridView(context)
	this.addView(lv, param)
	lv.block()
	return lv
}

fun ViewGroup.grid(param: ViewGroup.LayoutParams, block: GridView.() -> Unit): GridView {
	val v = this.createGrid()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.grid(index: Int, param: ViewGroup.LayoutParams, block: GridView.() -> Unit): GridView {
	val v = this.createGrid()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.gridBefore(ankor: View, param: ViewGroup.LayoutParams, block: GridView.() -> Unit): GridView {
	return this.grid(this.indexOfChild(ankor), param, block)
}

fun View.createGrid(): GridView {
	return this.context.createGrid()
}

fun Fragment.createGrid(): GridView {
	return this.act.createGrid()
}

fun Context.createGrid(): GridView {
	val g = GridView(this).genId()
	g.numColumns = 3
	return g
}
