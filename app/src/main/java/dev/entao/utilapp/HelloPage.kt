package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.page.TitlePage
import dev.entao.kan.res.Res

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightImage(Res.addWhite)
        }
    }

    fun test() {
    }
}