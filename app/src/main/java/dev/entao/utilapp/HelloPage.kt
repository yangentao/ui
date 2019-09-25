package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.Shapes
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.creator.button
import dev.entao.kan.creator.buttonRedRound
import dev.entao.kan.creator.edit
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.*
import dev.entao.kan.page.TitlePage
import dev.entao.kan.res.Res

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightImage(Res.addWhite)
        }
        contentView.backColor(Colors.WHITE)
        contentView.edit(LParam.WidthFill.HeightEdit) {
            this.imeDone()
        }
        contentView.buttonRedRound {
            text = "Hello"
            this.elevation = 3.0f
            this.translationZ = 5f
        }

        contentView.button(LParam.width(80).HeightButton.gravityCenter().marginTop(30)) {
            text = "Yang"
            this.backColor(Colors.WHITE)
            this.elevation = 50.0f
        }
        contentView.textView(LParam.width(100).HeightButton.gravityCenter().marginTop(30)) {
            text = "Yang"
            this.background = Shapes.rect {
                cornerDp = 3
                fillColor = Colors.RedMajor
            }
            this.elevation = 30.0f
            this.translationZ = 5f
        }
        contentView.textView(LParam.width(100).HeightButton.gravityCenter().marginTop(30)) {
            text = "Yang"
            this.backColorWhite()
            this.elevation = 10.dp.toFloat()
            this.translationZ = 0f
        }
    }

    fun test() {
    }
}