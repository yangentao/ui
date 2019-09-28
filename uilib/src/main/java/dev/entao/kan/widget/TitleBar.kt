package dev.entao.kan.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.*
import dev.entao.kan.base.StackActivity
import dev.entao.kan.creator.*
import dev.entao.kan.ext.*
import dev.entao.kan.res.D
import dev.entao.kan.res.Res
import dev.entao.kan.res.drawableRes

@SuppressLint("ViewConstructor")
class TitleBar(val context: Activity) : RelativeLayout(context) {
    private val leftCmds = ArrayList<BarItem>()
    private val rightCmds = ArrayList<BarItem>()
    var titleView: View? = null
    var titleCenter = TitleBar.TitleCenter

    var leftLinear: LinearLayout? = null
    var rightLinear: LinearLayout? = null

    var pushModel: Boolean = false
        private set
    private var leftBack = ArrayList<BarItem>()
    private var rightBack = ArrayList<BarItem>()
    private var titleBack: View? = null

    var popWindow: PopupWindow? = null

    private var titleClickCallback: () -> Unit = {}

    init {
        backColor(Colors.Theme)
        this.elevation = 10.dpf
    }

    operator fun invoke(block: TitleBar.() -> Unit) {
        block(this)
    }

    fun push(block: TitleBar.() -> Unit) {
        if (!pushModel) {
            pushModel = true

            moveTo(leftCmds, leftBack)
            moveTo(rightCmds, rightBack)

            titleBack = titleView
            titleView = null
            for (a in leftBack) {
                a.view?.removeFromParent()
            }
            for (a in rightBack) {
                a.view?.removeFromParent()
            }

            this.block()
            commit()
        }
    }

    fun pop() {
        if (pushModel) {
            pushModel = false
            moveTo(leftBack, leftCmds)
            moveTo(rightBack, rightCmds)
            titleView = titleBack
            titleBack = null

            commit()
        }
    }

    private fun moveTo(from: ArrayList<BarItem>, dest: ArrayList<BarItem>) {
        dest.clear()
        dest.addAll(from)
        from.clear()
    }

    fun findMenuItem(cmd: String): BarItem? {
        return find {
            it.cmd == cmd
        }
    }

    fun find(block: (BarItem) -> Boolean): BarItem? {
        for (a in leftCmds) {
            if (block(a)) {
                return a
            }
            for (b in a.children) {
                if (block(b)) {
                    return b
                }
            }
        }
        for (a in rightCmds) {
            if (block(a)) {
                return a
            }
            for (b in a.children) {
                if (block(b)) {
                    return b
                }
            }
        }
        return null
    }

    fun find(cmd: String): BarItem? {
        return find {
            it.cmd == cmd
        }
    }


    private fun makeSureView(item: BarItem) {
        if (item.view != null) {
            item.view?.removeFromParent()
            item.view = null
//            return
        }
        var d: Drawable? = null
        if (item.drawable != null) {
            d = item.drawable?.mutate()
        } else if (item.resIcon != 0) {
            d = item.resIcon.drawableRes.mutate()
        }
        if (d != null) {
            if (item.tintTheme) {
                d = d.tintedWhite
            }
            d = d.sized(TitleBar.ImgSize)
            val iv = ImageView(context)
            iv.scaleCenterInside()
            iv.backColorTransFade()
            iv.padding(0, 10, 0, 10)
            iv.setImageDrawable(d)
            item.view = iv
            item.param = LParam.width(HEIGHT).heightFill()
            return
        }
        val tv = createTextViewB()
        tv.backColorTransFade()
        tv.textColorWhite()
        tv.gravityCenter()
        tv.minimumWidth = dp(HEIGHT)
        tv.padding(5, 0, 5, 0)
        tv.text = item.text
        item.view = tv
        item.param = LParam.WidthWrap.HeightFill.gravityCenter()
    }

    fun commit() {
        leftLinear?.removeAllViews()
        rightLinear?.removeAllViews()
        removeAllViews()

        if (leftCmds.isNotEmpty()) {
            val ll = this.createLinearHorizontal()
            for (c in leftCmds.filter { !it.hidden }) {
                makeSureView(c)
                ll.addView(c.view, c.param)
                if (c.children.isEmpty()) {
                    c.view!!.setOnClickListener {
                        c.onClick(c.cmd)
                    }
                } else {
                    c.view!!.setOnClickListener {
                        popMenu(c)
                    }
                }
            }
            addView(ll, RParam.ParentLeft.HeightFill.WidthWrap)
            leftLinear = ll
        }

        if (rightCmds.isNotEmpty()) {
            val ll = this.createLinearHorizontal()
            for (c in rightCmds.filter { !it.hidden }) {
                makeSureView(c)
                ll.addView(c.view, c.param)
                if (c.children.isEmpty()) {
                    c.view!!.setOnClickListener {
                        c.onClick(c.cmd)
                    }
                } else {
                    c.view!!.setOnClickListener {
                        popMenu(c)
                    }
                }
            }
            addView(ll, RParam.ParentRight.HeightFill.WidthWrap)
            rightLinear = ll
        }
        val v = titleView
        if (v != null) {
            if (titleCenter) {
                addView(v, RParam.CenterInParent.HeightFill.WidthWrap)
            } else {
                if (leftLinear == null) {
                    addView(v, RParam.CenterVertical.ParentLeft.HeightFill.WidthWrap.margins(15, 0, 0, 0))
                } else {
                    addView(v, RParam.CenterVertical.toRightOf(leftLinear!!).HeightFill.WidthWrap.margins(15, 0, 0, 0))
                }
            }
        }

    }

    fun title(text: String): TextView {
        val tv = createTextViewA()
        tv.textColorWhite()
        tv.text = text
        titleView = tv
        titleView?.onClick {
            titleClickCallback()
        }
        return tv
    }

    fun titleImage(@DrawableRes resId: Int): ImageView {
        val iv = createImageView()
        iv.setImageResource(resId)
        iv.scaleCenterCrop()
        titleView = iv
        titleView?.onClick {
            titleClickCallback()
        }
        return iv
    }

    fun onTitleClick(block: () -> Unit) {
        titleClickCallback = block

    }

    fun removeBack() {
        removeCmd(BACK)
    }

    fun removeCmd(cmd: String) {
        val c = find(cmd)
        c?.view?.removeFromParent()

        leftCmds.removeAll { it.cmd == cmd }
        for (a in leftCmds) {
            a.children.removeAll { it.cmd == cmd }
        }
        rightCmds.removeAll { it.cmd == cmd }
        for (a in rightCmds) {
            a.children.removeAll { it.cmd == cmd }
        }
    }


    fun showBack(resId: Int = Res.back): BarItem {
        val c = find(BACK)
        if (c != null) {
            return c
        }
        val a = leftImage(resId, BACK)
        a.onClick = {

            if (context is StackActivity) {
                context.pop()
            } else {
                context.onBackPressed()
            }
        }
        return a
    }

    fun rightImage(resId: Int, BarItem: String = "$resId"): BarItem {
        return rightImage(resId.drawableRes, BarItem)
    }

    fun rightImage(d: Drawable, cmd: String = BarItem.autoIdent()): BarItem {
        val c = BarItem(cmd)
        c.drawable = d
        rightCmds.add(c)
        return c
    }

    fun rightText(text: String, cmd: String = text): BarItem {
        val c = BarItem(cmd)
        c.text = text
        rightCmds.add(c)
        return c
    }

    fun leftImage(resId: Int, BarItem: String = "$resId"): BarItem {
        return leftImage(resId.drawableRes, BarItem)
    }

    fun leftImage(d: Drawable, cmd: String = BarItem.autoIdent()): BarItem {
        val c = BarItem(cmd)
        c.drawable = d
        leftCmds.add(c)
        return c
    }

    fun leftText(text: String, cmd: String = text): BarItem {
        val c = BarItem(cmd)
        c.text = text
        leftCmds.add(c)
        return c
    }

    private fun popMenu(item: BarItem) {
        val p = PopupWindow(context)
        p.width = ViewGroup.LayoutParams.WRAP_CONTENT
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT
        p.isFocusable = true
        p.isOutsideTouchable = true
        p.setBackgroundDrawable(ColorDrawable(0))
        val gd = ShapeRect(Colors.Theme).corners(0, 0, 2, 2).value
        val popRootView = context.createLinearVertical()
        popRootView.minimumWidth = dp(150)
        popRootView.backDrawable(gd).padding(5)
        popRootView.divider()
        val itemList = ArrayList<BarItem>(item.children.filter { !it.hidden })
        for (c in itemList) {
            val v = makeSureMenuItemView(c)
            popRootView.addView(v, LParam.WidthFill.height(45))
            v.setOnClickListener {
                popWindow?.dismiss()
                Task.fore {
                    c.onClick(c.cmd)
                }
            }
        }
        p.contentView = popRootView
        popWindow = p

        p.setOnDismissListener {
            (popWindow?.contentView as? ViewGroup)?.removeAllViews()
            popWindow = null
        }
        p.showAsDropDown(item.view, 0, 1)
    }

    fun menu(block: BarItem.() -> Unit) {
        val m = find(MENU) ?: rightImage(Res.menu, MENU)
        m.block()
    }

    fun menu(resId: Int, block: BarItem.() -> Unit) {
        val m = find(MENU) ?: rightImage(resId, MENU)
        m.block()
    }

    private fun makeSureMenuItemView(item: BarItem): View {
        if (item.view != null) {
            item.view?.removeFromParent()
            item.view = null
//            return item.view!!
        }
        val tv = context.createTextViewB()
        tv.singleLine()
        tv.backColorTransFade()
        tv.gravityLeftCenter().padding(5, 5, 20, 5)
        tv.text = item.text

        var d: Drawable = if (item.drawable != null) {
            item.drawable!!.mutate()
        } else if (item.resIcon != 0) {
            item.resIcon.drawableRes.mutate()
        } else {
            D.color(Color.TRANSPARENT)
        }
        d = if (item.tintTheme) {
            d.tintedWhite.sized(TitleBar.ImgSize)
        } else {
            d.sized(TitleBar.ImgSize)
        }
        tv.compoundDrawablePadding = dp(10)
        tv.setCompoundDrawables(d, null, null, null)
        tv.textColorWhite()
        item.view = tv
        return tv
    }

    fun leftItems(block: TitleBarItemBuilder.() -> Unit) {
        val a = TitleBarItemBuilder(this, true)
        a.block()
    }

    fun rightItems(block: TitleBarItemBuilder.() -> Unit) {
        val a = TitleBarItemBuilder(this, false)
        a.block()
    }

    fun menuItems(block: TitleBarMenuItemBuilder.() -> Unit) {
        val m = find(MENU) ?: rightImage(Res.menu, MENU)
        val a = TitleBarMenuItemBuilder(m)
        a.block()
    }

    companion object {
        const val BACK = "back"
        const val MENU = "menu"
        const val ImgSize = 24
        const val HEIGHT = 50// dp, android Toolbar高度是56
        var TitleCenter = true
    }
}

class TitleBarItemBuilder(val bar: TitleBar, val isLeft: Boolean) {

    infix fun String.on(block: () -> Unit) {
        if (isLeft) {
            bar.leftText(this).onClick = {
                block()
            }
        } else {
            bar.rightText(this).onClick = {
                block()
            }
        }
    }

    infix fun Int.on(block: () -> Unit) {
        if (isLeft) {
            bar.leftImage(this).onClick = {
                block()
            }
        } else {
            bar.rightImage(this).onClick = {
                block()
            }
        }
    }
}

class TitleBarMenuItemBuilder(val barItem: BarItem) {

    infix fun Pair<String, Int>.on(block: () -> Unit) {
        barItem.add(this.second, this.first).onClick = {
            block()
        }
    }
}