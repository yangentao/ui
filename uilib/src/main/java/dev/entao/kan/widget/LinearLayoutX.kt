package dev.entao.kan.widget

import android.content.Context
import android.support.annotation.Keep
import android.widget.LinearLayout
import dev.entao.kan.ext.genId
import dev.entao.kan.ext.parentGroup

@Keep
class LinearLayoutX(context: Context) : LinearLayout(context) {
	init {
		genId()
	}

	fun setPercentX(v: Float) {
		val w = parentGroup?.width?.toFloat() ?: 0.0f
		translationX = v * w
	}

	fun getPercentX(): Float {
		val w = parentGroup?.width?.toFloat() ?: 1.0f
		return this.translationX / w
	}


	fun setPercentY(v: Float) {
		val h = parentGroup?.height?.toFloat() ?: 0.0f
		translationY = v * h
	}

	fun getPercentY(): Float {
		val h = parentGroup?.height?.toFloat() ?: 1.0f
		return this.translationY / h
	}
}