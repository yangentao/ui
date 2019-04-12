package dev.entao.ui.list

import android.content.Context
import android.view.View
import dev.entao.ui.list.itemviews.TextDetailView

abstract class TextDetailSelectPage : SelectPage() {

	override fun onNewView(context: Context, position: Int): View {
		return TextDetailView(context)
	}

}
