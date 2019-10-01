package dev.entao.kan.widget

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.kan.base.ColorX
import dev.entao.kan.creator.createTextViewB
import dev.entao.kan.ext.*

/**
 * Created by entaoyang@163.com on 2016-09-04.
 */

class SwitchItemView(context: Context) : LinearLayout(context) {
	var textView: TextView

	init {
		this.genId()
		this.orientationVertical().backColor(Color.WHITE, ColorX.fade)
		textView = context.createTextViewB()
		textView.padding(20, 10, 20, 10).gravityCenter().backColor(Color.WHITE)
		this.addViewParam(textView) { widthFill().heightFill().margins(0, 0, 0, 3) }
	}

	var text: String
		get() {
			return textView.text.toString()
		}
		set(text) {
			textView.text(text)
		}

}