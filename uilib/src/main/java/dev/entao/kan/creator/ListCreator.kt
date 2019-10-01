@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import dev.entao.kan.base.ColorX
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId
import dev.entao.kan.list.SimpleListView
import dev.entao.kan.res.D

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun  ViewGroup.simpleListView(param: ViewGroup.LayoutParams, block: SimpleListView.() -> Unit): SimpleListView {
	val lv = SimpleListView(context)
	this.addView(lv, param)
	lv.block()
	return lv
}

//List View
fun ViewGroup.listView(param: ViewGroup.LayoutParams, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.listView(index: Int, param: ViewGroup.LayoutParams, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.listViewBefore(ankor: View, param: ViewGroup.LayoutParams, block: ListView.() -> Unit): ListView {
	return this.listView(this.indexOfChild(ankor), param, block)
}


fun View.createListView(): ListView {
	return this.context.createListView()
}

fun Fragment.createListView(): ListView {
	return this.act.createListView()
}

fun Context.createListView(): ListView {
	val lv = ListView(this).genId()
	lv.cacheColorHint = 0
	lv.selector = D.lightColor(Color.TRANSPARENT, ColorX.fade)
	return lv
}
