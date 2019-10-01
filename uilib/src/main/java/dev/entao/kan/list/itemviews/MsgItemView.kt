package dev.entao.kan.list.itemviews

import android.content.Context
import android.graphics.Color
import android.widget.RelativeLayout
import android.widget.TextView
import dev.entao.kan.appbase.ex.dpf
import dev.entao.kan.base.ColorX
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.*


class MsgItemView(context: Context) : RelativeLayout(context) {
    val textView: TextView

    val relParam: RelativeLayout.LayoutParams

    init {
        relParam = RParam.Wrap.margins(6).ParentTop.ParentLeft
        textView = this.textView(relParam) {
            padding(16)
            textSizeA()
            textColorMajor()
            backRect {
                corner(5)
                fill(Color.WHITE)
            }
            elevation = 5.dpf
        }
    }

    fun setMsgPos(isLeft: Boolean) {
        if (isLeft) {
            textView.textColor(ColorX.theme)
            textView.gravityLeftCenter()
            textView.layoutParams = RParam.Wrap.ParentTop.ParentLeft.margins(6, 6, 40, 6)
        } else {
            textView.textColorMajor()
            textView.gravityRightCenter()
            textView.layoutParams = RParam.Wrap.ParentTop.ParentRight.margins(40, 6, 6, 6)
        }
    }

    fun setText(text: String) {
        textView.text = text
    }
}