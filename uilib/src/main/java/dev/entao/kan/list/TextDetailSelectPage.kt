@file:Suppress("unused")

package dev.entao.kan.list

import android.content.Context
import android.view.View
import dev.entao.kan.list.itemviews.TextDetailView

abstract class TextDetailSelectPage : SelectPage() {

	override fun onNewView(context: Context, position: Int): View {
		return TextDetailView(context)
	}

}
