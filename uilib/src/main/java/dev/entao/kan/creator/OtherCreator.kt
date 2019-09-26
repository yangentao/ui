package dev.entao.kan.creator

import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.entao.kan.ext.genId
/**
 * Created by entaoyang@163.com on 2018-04-18.
 */

fun ViewGroup.swipeRefresh(param: ViewGroup.LayoutParams, block: SwipeRefreshLayout.() -> Unit): SwipeRefreshLayout {
	val v = SwipeRefreshLayout(context).genId()
	addView(v, param)
	v.block()
	return v
}