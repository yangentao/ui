package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.ui.page.TitlePage

class YangPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Yang")
        }
    }
}