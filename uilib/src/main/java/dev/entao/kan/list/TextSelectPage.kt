package dev.entao.kan.list

import android.content.Context
import android.view.View
import dev.entao.kan.list.itemviews.TextItemView

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class TextSelectPage : SelectPage() {
	override fun onNewView(context: Context, position: Int): View {
		return TextItemView(context)
	}

}
