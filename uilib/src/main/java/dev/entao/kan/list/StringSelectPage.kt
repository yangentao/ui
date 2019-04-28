@file:Suppress("unused")

package dev.entao.kan.list

import android.view.View
import dev.entao.kan.list.itemviews.TextItemView

/**
 * 简单字符串选择
 */
open class StringSelectPage : TextSelectPage() {

	override fun onBindView(itemView: View, position: Int) {
		(itemView as TextItemView).text = getItem(position) as String
	}
}
