package dev.entao.utilapp

import android.content.Context
import android.graphics.Color
import android.view.Menu
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.colors
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.creator.createTextView
import dev.entao.kan.ext.*
import dev.entao.kan.log.logd
import dev.entao.kan.page.TitlePage
import dev.entao.kan.util.IdGen


class HelloPage : TitlePage() {
    lateinit var navMenu: Menu
    lateinit var bb: BottomNavigationView

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightItems {
                "OK" on ::onOK
            }

        }

        val b = BottomNavigationView(context)
        bb = b
        this.contentView.addView(b, LParam.WidthFill.HeightWrap)
        b.menu.buildItems {
            "Home" TO R.drawable.yet_del
//            "Discover" TO R.drawable.yet_add_white
            "Me" TO R.drawable.yet_me
        }
//        b.menu.apply {
//            this.add("Home").setIcon(R.drawable.yet_del)
//            this.add("Discover").setIcon(R.drawable.yet_add_white)
//            this.add("Me").setIcon(R.drawable.yet_me)
//        }
        val a = com.google.android.material.R.color.secondary_text_default_material_light
        val cs = StateList.colors(a) {
            selected(Colors.Theme)
            checked(Colors.Theme)
        }
        b.itemTextColor = cs
        b.itemIconTintList = cs

        b.setOnNavigationItemSelectedListener {
            logd("Select: ", it.title, it.itemId)
            true
        }

        navMenu = b.menu


    }

    fun onOK() {
        navMenu.getItem(0)?.setChecked(true)
        logd(bb.selectedItemId)

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

fun Menu.buildItems(block: MenuItemBuilder.() -> Unit) {
    val a = MenuItemBuilder(this)
    a.block()
}

class MenuItemBuilder(val menu: Menu) {

    infix fun String.TO(@DrawableRes iconId: Int) {
        menu.add(0, IdGen.gen(), 0, this).setIcon(iconId)
    }

    fun add(title: String) {
        menu.add(title)
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


