@file:Suppress("unused")

package dev.entao.kan.list

import android.content.Context
import android.view.View
import dev.entao.kan.list.itemviews.IconTextView
import dev.entao.kan.theme.IconSize
import dev.entao.kan.theme.TextSize

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class IconTextSelectPage : SelectPage() {


	override fun onNewView(context: Context, position: Int): View {
		val v = IconTextView(context, IconSize.Big)
		v.setTextSize(TextSize.Normal)

		return v
	}
}
