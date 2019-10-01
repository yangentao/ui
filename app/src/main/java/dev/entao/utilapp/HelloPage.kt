package dev.entao.utilapp

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.creator.addViewX
import dev.entao.kan.creator.createImageView
import dev.entao.kan.creator.createTextView
import dev.entao.kan.ext.*
import dev.entao.kan.page.TitlePage


class HelloPage : TitlePage() {

    lateinit var imageView: ImageView

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightItems {
                "OK" ON ::onOK
            }


        }

        imageView = contentView.addViewX(createImageView(), LParam.size(300).margins(30))
        imageView.setBackgroundColor(Color.GRAY)
//        val d = VectorDrawableCompat.create(act.resources, R.drawable.yet_account, act.theme)!!


    }

    fun onOK() {
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


