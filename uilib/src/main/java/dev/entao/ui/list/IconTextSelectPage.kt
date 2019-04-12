package dev.entao.ui.list

import android.content.Context
import android.view.View
import dev.entao.ui.theme.IconSize
import dev.entao.ui.theme.TextSize
import dev.entao.ui.list.itemviews.IconTextView

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
