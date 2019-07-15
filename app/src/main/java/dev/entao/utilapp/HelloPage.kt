package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.creator.edit
import dev.entao.kan.ext.HeightEdit
import dev.entao.kan.ext.LParam
import dev.entao.kan.ext.WidthFill
import dev.entao.kan.ext.imeDone
import dev.entao.kan.page.TitlePage
import dev.entao.kan.res.Res

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightImage(Res.addWhite)
        }

        contentView.edit (LParam.WidthFill.HeightEdit){
            this.imeDone()
        }
    }

    fun test() {
    }
}