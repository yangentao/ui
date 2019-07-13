@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun TableLayout.tableRow(param: ViewGroup.LayoutParams, block: TableRow.() -> Unit): TableRow {
	val v = TableRow(this.context).genId()
	this.addView(v, param)
	v.block()
	return v
}

fun TableLayout.tableRow(index: Int, param: ViewGroup.LayoutParams, block: TableRow.() -> Unit): TableRow {
	val v = TableRow(this.context).genId()
	this.addView(v, index,  param)
	v.block()
	return v
}

//TableLayout
fun ViewGroup.table(param: ViewGroup.LayoutParams, block: TableLayout.() -> Unit): TableLayout {
	val v = this.createTable()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.table(index: Int, param: ViewGroup.LayoutParams, block: TableLayout.() -> Unit): TableLayout {
	val v = this.createTable()
	this.addView(v, index, param)
	v.block()
	return v
}

fun View.createTable(): TableLayout {
	return this.context.createTable()
}

fun Fragment.createTable(): TableLayout {
	return this.act.createTable()
}

fun Context.createTable(): TableLayout {
	return TableLayout(this).genId()
}


