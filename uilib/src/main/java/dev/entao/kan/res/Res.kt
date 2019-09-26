@file:Suppress("unused")

package dev.entao.kan.res

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.VState
import dev.entao.kan.ui.R

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */
object Res {
    val menu = R.drawable.yet_menu
    val me = R.drawable.yet_me
    val addWhite = R.drawable.yet_add_white
    val arrowRight = R.drawable.yet_arrow_right
    val more = R.drawable.yet_arrow_right
    val back = R.drawable.yet_back
    val checkbox = R.drawable.yet_checkbox
    val checkboxChecked = R.drawable.yet_checkbox_checked
    val dropdown = R.drawable.yet_dropdown
    val editClear = R.drawable.yet_edit_clear
    val imageMiss = R.drawable.yet_image_miss
    val picAdd = R.drawable.yet_pic_add
    val portrait = R.drawable.yet_portrait
    val selAll = R.drawable.yet_sel_all
    val selAllLight = R.drawable.yet_sel_all2
    val del = R.drawable.yet_del

    fun str(resId: Int): String {
        return App.resource.getString(resId)
    }

    fun strArgs(resId: Int, vararg args: Any): String {
        return App.resource.getString(resId, *args)
    }

    @Suppress("DEPRECATION")
    fun color(resId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            App.resource.getColor(resId, App.inst.theme)
        } else {
            App.resource.getColor(resId)
        }
    }


    @Suppress("DEPRECATION")
    fun drawable(resId: Int): Drawable {
        return AppCompatResources.getDrawable(App.inst, resId)!!
//		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			App.resource.getDrawable(resId, App.inst.theme)
//		} else {
//			App.resource.getDrawable(resId)
//		}
    }

}


fun StateList.drawable(@DrawableRes normal: Int, vararg ls: Pair<VState, Int>): StateListDrawable {
    val ld = StateListDrawable()
    for (p in ls) {
        ld.addState(intArrayOf(p.first.value), Res.drawable(p.second))
    }
    ld.addState(IntArray(0), Res.drawable(normal))
    return ld
}

fun StateList.lightDrawable(@DrawableRes normal: Int, @DrawableRes light: Int): StateListDrawable {
    return this.lightDrawable(Res.drawable(normal), Res.drawable(light))
}