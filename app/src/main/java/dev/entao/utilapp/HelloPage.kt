package dev.entao.utilapp

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.appbase.ex.dpf
import dev.entao.kan.base.toast
import dev.entao.kan.creator.createButton
import dev.entao.kan.creator.createTextView
import dev.entao.kan.ext.*
import dev.entao.kan.page.TitlePage


class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightItems {
                "OK" on ::onOK
                "Down" on ::down
            }
            menuItems {
                "Hello" to R.drawable.yet_add_white on ::onOK
                "Yang" to R.drawable.yet_del on ::onOK

            }
        }

//        val b = Button(context, null, android.R.attr.actionButtonStyle)
//        val b = Button(context)
        val b = createButton("Hello")
        this.contentView.addView(b, LParam.width(200).height(80).margins(20))
        b.text = "Hello"
        b.setOnClickListener { v -> }
//        b.backTint(Colors.Safe)
//        b.styleGreen()
//        b.backColor(Colors.WHITE)
        b.backTintRed()
//        b.backTint(Colors.Safe)
//        b.stateListAnimator = null
//        b.elevation = 30.dpf

        val c = TextView(context)
        this.contentView.addView(c, LParam.width(200).height(80).margins(20))
        c.text = "Hello"
        c.backColor(Colors.WHITE)
//        c.backColor(Colors.Safe)
//        c.backTint(Colors.Safe)
        c.elevation = 30.dpf

    }

    fun onOK() {
        toast("OK")
    }

    fun sliding(context: Context) {

        val a = SlidingPaneLayout(context)
        contentView.addView(a, LParam.WidthFill.height(200))
        val b = createTextView()
        val c = createTextView()
//        val d = createTextView()
        a.addView(b, 100.dp, ViewGroup.LayoutParams.MATCH_PARENT)
        a.addView(c, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        a.addView(d, 150.dp, ViewGroup.LayoutParams.MATCH_PARENT)

        a.backColor(Color.DKGRAY)
        b.backColor(Colors.RedMajor)
        c.backColor(Colors.GreenMajor)
//        d.backColor(Colors.BlueMajor)
    }

    fun down() {


    }

}


fun Button.applyStyle() {
//    val ta = context.obtainStyledAttributes(android.R.style.Widget_Holo_Button, android.R.styleable.ButtonStyleHolder)
//    val resources = context.resources
//    val background = ta.getDrawable(ta.getIndex(android.R.styleable.ButtonStyleHolder_android_background))
//    val textColor = ta.getColorStateList(ta.getIndex(R.styleable.ButtonStyleHolder_android_textColor))
//    val textSize = ta.getDimensionPixelSize(
//        ta.getIndex(R.styleable.ButtonStyleHolder_android_textSize),
//        resources.getDimensionPixelSize(R.dimen.standard_text_size)
//    )
//    ta.recycle()
//    this.background = background
//    this.setTextColor(textColor)
//    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
}


