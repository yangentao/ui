package dev.entao.kan.res

import android.graphics.drawable.Drawable
import android.os.Build
import dev.entao.kan.appbase.App
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
	val del = R.drawable.del

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
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.inst.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

}