package dev.entao.ui.list.itemviews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.appbase.ex.dp
import dev.entao.ui.theme.IconSize
import dev.entao.ui.ext.*
import dev.entao.ui.creator.createImageView
import dev.entao.ui.creator.createTextViewA

/**
 * Created by entaoyang@163.com on 2016-12-27.
 */

class IconTextView(context: Context, iconSize: Int = IconSize.Big) : LinearLayout(context) {
	val iconView: ImageView
	val textView: TextView

	init {
		genId().horizontal().padding(15, 7, 15, 7).backColor(Color.WHITE)
		iconView = context.createImageView().scaleCenterInside()
		addView(iconView){
			size(iconSize).gravityCenter()
		}
		textView = context.createTextViewA()
		addView(textView){
			wrap().gravityCenter().margins(10, 7, 15, 7)
		}
	}

	fun setValues(@DrawableRes icon: Int, text: String): IconTextView {
		iconView.setImageResource(icon)
		textView.text = text
		return this
	}

	fun setValues(d: Drawable, text: String): IconTextView {
		iconView.setImageDrawable(d)
		textView.text = text
		return this
	}

	fun setTextSize(sp: Int): IconTextView {
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.toFloat())
		return this
	}

	fun setIconSize(n: Int): IconTextView {
		val p = iconView.layoutParams as LinearLayout.LayoutParams
		p.width = dp(n)
		p.height = dp(n)
		iconView.layoutParams = p
		return this
	}
}
