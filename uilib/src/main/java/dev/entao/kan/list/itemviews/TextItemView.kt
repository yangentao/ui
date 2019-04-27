package dev.entao.kan.list.itemviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.TextView
import dev.entao.kan.appbase.ex.sized
import dev.entao.kan.theme.IconSize
import dev.entao.kan.theme.Space
import dev.entao.kan.ext.gravityLeftCenter
import dev.entao.kan.ext.padding
import dev.entao.kan.ext.textColorMajor
import dev.entao.kan.ext.textSizeA
import dev.entao.kan.res.D

/**
 * Created by entaoyang@163.com on 16/3/13.
 */
class TextItemView(context: Context) : TextView(context) {
	init {
		padding(Space.Normal, Space.Small, Space.Normal, Space.Small).gravityLeftCenter().textSizeA().textColorMajor()
	}

	fun icon(d: Drawable?) {
		d?.sized(IconSize.Normal)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(d: Drawable?, size: Int) {
		d?.sized(size)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(@DrawableRes resId: Int, size: Int) {
		val d = D.sized(resId, size)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(@DrawableRes resId: Int) {
		val d = D.res(resId)
		setCompoundDrawables(d, null, null, null)
	}
}
