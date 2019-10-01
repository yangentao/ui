package dev.entao.kan.widget

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.kan.appbase.ex.gray
import dev.entao.kan.base.ColorX
import dev.entao.kan.creator.createTextView
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.*
import dev.entao.kan.list.itemviews.TextDetailView

/**
 * Created by entaoyang@163.com on 2017-05-20.
 */


class TitleContentView(context: Context) : LinearLayout(context) {
	val titleView: TextView

	init {
		vertical()
		padding(10)
		backFill(defaultBackColor, defaultBackCorner)


		titleView = textView(lParam().widthFill().heightWrap().margins(0, 0, 0, 10)) {
			textColor(defaultTitleTextColor)
			textSizeA()
			gravityCenter()
		}
		if (defaultHasLine) {
			addGrayLine {
				color(defaultLineColor)
				bottom(10)
			}
		}
	}

	fun setTitle(title: String) {
		titleView.text = title
	}

	fun styleColored(color: Int) {
		titleView.textColorWhite()
		addGrayLine {
			color(Color.WHITE)
			bottom(10)
		}
		backFill(color, 4)
	}

	companion object {
		var defaultTitleTextColor: Int = ColorX.textPrimary
		var defaultHasLine = true
		var defaultLineColor = Color.WHITE
		var defaultBackColor = 0xee.gray
		var defaultBackCorner = 4
		var defaultTextColor = ColorX.textPrimary
	}
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTitleContentView(param: P, block: TitleContentView.() -> Unit): TitleContentView {
	val v = TitleContentView(this.context)
	this.addView(v, param)
	v.block()
	return v
}

inline fun TitleContentView.addTextViewStyled(param: LinearLayout.LayoutParams, block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v, param)
	v.textColor(TitleContentView.defaultTextColor)
	v.block()
	return v
}

inline fun  TitleContentView.addTextDetailViewStyled(param: LinearLayout.LayoutParams, block: TextDetailView.() -> Unit): TextDetailView {
	val v = TextDetailView(this.context)
	this.addView(v, param)
	v.backColorTrans()
	v.textView.textColor(TitleContentView.defaultTextColor)
	v.detailView.textColor(TitleContentView.defaultTextColor)
	v.block()
	return v
}