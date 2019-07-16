package dev.entao.kan.list.itemviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes
import dev.entao.kan.appbase.ex.sized
import dev.entao.kan.ext.*
import dev.entao.kan.res.D
import dev.entao.kan.theme.IconSize
import dev.entao.kan.theme.Space

/**
 * Created by entaoyang@163.com on 16/3/13.
 */
class TextItemView(context: Context) : TextView(context) {
    init {
        genId()
        padding(Space.Normal, 12, Space.Normal, 12).gravityLeftCenter().textSizeA().textColorMajor()
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
