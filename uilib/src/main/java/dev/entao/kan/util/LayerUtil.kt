package dev.entao.kan.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import dev.entao.kan.appbase.App
import java.util.*

class LayerUtil {

     var ls = ArrayList<TempDrawable>()

     class TempDrawable(var drawable: Drawable, n: Int) {
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        init {
            bottom = n
            right = bottom
            top = right
            left = top
        }
    }

    //inset dp
    fun add(d: Drawable, insetLeftDp: Int, insetTopDp: Int, insetRightDp: Int, insetBottomDp: Int) {
        val td = TempDrawable(d, 0)
        td.left = insetLeftDp
        td.top = insetTopDp
        td.right = insetRightDp
        td.bottom = insetBottomDp
        ls.add(td)
    }

    fun add(d: Drawable, insetDp: Int) {
        val t = TempDrawable(d, insetDp)
        ls.add(t)
    }

    fun add(d: Drawable) {
        val t = TempDrawable(d, 0)
        ls.add(t)
    }

    fun get(): LayerDrawable {
        val layers = arrayOfNulls<Drawable>(ls.size)
        for (i in ls.indices) {
            layers[i] = ls[i].drawable
        }
        val ld = LayerDrawable(layers)
        for (i in ls.indices) {
            val t = ls[i]
            ld.setLayerInset(i, App.dp2px(t.left), App.dp2px(t.top), App.dp2px(t.right), App.dp2px(t.bottom))
        }
        return ld
    }

    companion object {

        fun build(d: Drawable, inset: Int): LayerDrawable {
            val ld = LayerDrawable(arrayOf(d))
            ld.setLayerInset(0, App.dp2px(inset), App.dp2px(inset), App.dp2px(inset), App.dp2px(inset))
            return ld
        }
    }
}
