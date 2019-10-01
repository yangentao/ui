@file:Suppress("MemberVisibilityCanBePrivate")

package dev.entao.utilapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.google.android.material.button.MaterialButton
import dev.entao.kan.appbase.ex.*
import dev.entao.kan.base.pushPage
import dev.entao.kan.creator.addViewX
import dev.entao.kan.creator.button
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

        val a = com.google.android.material.R.style.Widget_MaterialComponents_Button
        val c = com.google.android.material.R.style.Theme_MaterialComponents
        val mb = MaterialButton(ContextThemeWrapper(context, c), null, a)
        contentView.addViewX(mb, LParam.size(150, 60).margins(10)) {
            text = "Hello"
            gravityCenter()
        }


        contentView.button(LParam.size(150, 48).margins(10)) {
            styleSetup { fillBlue() }
            text = "Hello"
        }
        contentView.button(LParam.size(150, 48).margins(10)) {
            styleSetup { outlineGreen() }
            text = "Hello"
        }
        contentView.button(LParam.size(150, 48).margins(10)) {
            styleSetup { textRed() }
            text = "Hello"
        }


        contentView.button(LParam.size(150, 48).margins(10)) {
            styleSetup { fillBlue() }
            text = "Hello"
            this.isEnabled = false
        }
        contentView.button(LParam.size(150, 48).margins(10)) {
            styleSetup { outlineGreen() }
            text = "Hello"
            this.isEnabled = false
        }
        contentView.button(LParam.size(150, 48).margins(10)) {
            styleSetup { textRed() }
            text = "Hello"
            this.isEnabled = false
        }
    }

    fun onOK() {
        pushPage(APage())
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


val Button.styleX: ButtonStyle get() = ButtonStyle(this)

fun Button.styleSetup(block: ButtonStyle.() -> Unit) {
    val a = ButtonStyle(this)
    a.block()
    a.setup()
}

class ButtonStyle(val button: Button) {
    var style: Int = S_FILL
        private set

    var fadeColor: Int = Colors.Fade
    var textDisabledColor: Int = TX_DISABLED
    var backDisabledColor: Int = BG_DISABLED

    var corner: Int = 4

    var fillColor: Int = Color.TRANSPARENT
    var strokeColor: Int = Color.LTGRAY
    var textColor: Int = Colors.TextColorMajor

    fun fade(color: Int): ButtonStyle {
        fadeColor = color
        return this
    }

    fun fill(color: Int): ButtonStyle {
        style = S_FILL
        fillColor = color
        return this
    }

    fun outline(color: Int): ButtonStyle {
        style = S_OUTLINE
        textColor = color
        return this
    }

    fun text(color: Int): ButtonStyle {
        style = S_TEXT
        textColor = color
        return this
    }

    fun setup() {
        button.elevation = 4.dpf

        val normalShape = ShapeRect(fillColor, corner)
        val fadeShape = ShapeRect(fadeColor, corner)

        when (style) {
            S_FILL -> {
                button.background = StateList.drawables(normalShape.value) {
                    pressed(fadeShape.value)
                    checked(fadeShape.value)
                    disabled(ColorDrawable(backDisabledColor))
                }
                button.textColor(textColor)
            }
            S_OUTLINE -> {
                val aa = normalShape.stroke(1, strokeColor).value
                val bb = fadeShape.stroke(1, fadeColor).value
                button.background = StateList.drawables(aa) {
                    pressed(bb)
                    checked(bb)
                }
                button.setTextColor(StateList.colors(textColor) {
                    disabled(textDisabledColor)
                })

            }
            S_TEXT -> {
                button.background = StateList.drawables(normalShape.value) {
                    pressed(fadeShape.value)
                    checked(fadeShape.value)
                }
                button.setTextColor(StateList.colors(textColor) {
                    disabled(textDisabledColor)
                })
            }
            else -> {
                return
            }
        }


    }

    fun outlineBlue(): ButtonStyle {
        outline(C_BLUE)
        return this
    }

    fun outlineRed(): ButtonStyle {
        outline(C_RED)
        return this
    }

    fun outlineGreen(): ButtonStyle {
        outline(C_GREEN)
        return this
    }

    fun outlineTheme(): ButtonStyle {
        outline(Colors.Theme)
        return this
    }

    fun textBlue(): ButtonStyle {
        text(C_BLUE)
        return this
    }

    fun textRed(): ButtonStyle {
        text(C_RED)
        return this
    }

    fun textGreen(): ButtonStyle {
        text(C_GREEN)
        return this
    }

    fun textTheme(): ButtonStyle {
        text(Colors.Theme)
        return this
    }

    fun fillBlue(): ButtonStyle {
        this.fill(C_BLUE)
        textColor = Color.WHITE
        return this
    }

    fun fillRed(): ButtonStyle {
        this.fill(C_RED)
        textColor = Color.WHITE
        return this
    }

    fun fillGreen(): ButtonStyle {
        this.fill(C_GREEN)
        textColor = Color.WHITE
        return this
    }

    fun fillTheme(): ButtonStyle {
        this.fill(Colors.Theme)
        textColor = Color.WHITE
        return this
    }

    companion object {
        const val S_FILL = 0
        const val S_OUTLINE = 1
        const val S_TEXT = 2

        var C_BLUE: Int = 0x3F52B5L.rgb
        var C_RED: Int = 0xD81B60L.rgb
        var C_GREEN: Int = 0x45A859L.rgb
        var TX_DISABLED: Int = 0x61000000L.argb
        var BG_DISABLED: Int = 0x1F000000L.argb
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


