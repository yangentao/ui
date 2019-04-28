@file:Suppress("unused")

package dev.entao.kan.list.itemviews

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.ext.*
/**
 * Created by entaoyang@163.com on 16/3/13.
 */

open class HorItemView(context: Context) : LinearLayout(context) {
	var positionBind = 0

	init {
		genId()
		horizontal()
		gravityCenterVertical()
		padding(20, 10, 20, 5)
		backColorWhiteFade()
	}

}