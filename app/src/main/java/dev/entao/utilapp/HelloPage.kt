package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.page.TitlePage
import dev.entao.kan.res.Res
import dev.entao.kan.util.app.Notify

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightImage(Res.addWhite).onClick = {
                test()
            }
            rightText("Hide").onClick = {
                hideNotify()
            }
        }

    }

    fun hideNotify() {
        Notify.cancel(111)
    }

    fun test() {
        Notify(111).title("Hello").text("Yang Entao").show()
    }
}