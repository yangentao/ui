@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package dev.entao.kan.list.itemviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.creator.createImageView
import dev.entao.kan.creator.createTextViewA
import dev.entao.kan.ext.*
import dev.entao.kan.theme.IconSize

/**
 * Created by entaoyang@163.com on 2016-12-27.
 */

@SuppressLint("ViewConstructor")
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
