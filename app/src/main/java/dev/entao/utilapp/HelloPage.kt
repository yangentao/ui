package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.ui.base.pushPage
import dev.entao.ui.page.TitlePage

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightText("Yang").onClick = {
                pushPage(YangPage(), true, false)
            }
        }
    }
}