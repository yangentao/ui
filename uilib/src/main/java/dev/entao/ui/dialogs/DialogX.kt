@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.dp
import dev.entao.ui.creator.*
import dev.entao.ui.ext.*
import dev.entao.ui.grid.SimpleGridView
import dev.entao.ui.list.CheckListView
import dev.entao.ui.list.SimpleListView
import dev.entao.ui.theme.ViewSize
import dev.entao.ui.widget.TitleBar

class DialogX(val context: Context) {
    private var titleHeight = 45
    val dialog = Dialog(context, android.support.v7.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
    val windowParam: WindowManager.LayoutParams = dialog.window?.attributes!!
    val cardView: CardView = CardView(context)
    private val rootLayout = context.createLinearVertical()
    var bodyView: View = context.createTextViewB().textColorMajor()
    var bodyViewParam: LinearLayout.LayoutParams = LParam.WidthFill.HeightWrap
    var onDismiss: (DialogX) -> Unit = {}
    var beforeDismiss: (DialogX) -> Unit = {}


    private var buttons = ArrayList<DialogButton>()
    var title: String = ""
    val result = ArrayList<Any>()

    var argS: String = ""
    var argN = 0

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setDimAmount(0.5f)
        windowParam.Wrap


        cardView.setCardBackgroundColor(Color.WHITE)
        cardView.cardElevation = dp(5).toFloat()
        cardView.preventCornerOverlap = false
        cardView.radius = ViewSize.DialogCorner.dp.toFloat()


        cardView.addView(rootLayout, FParam.WidthFill.HeightWrap)
        rootLayout.divider()

        cardView.minimumHeight = dp(80)
        val minW = App.screenWidthPx * 2 / 4
        rootLayout.minimumWidth = minW
        rootLayout.minimumHeight = dp(80)
    }


    fun title(title: String?) {
        this.title = title ?: ""
    }

    fun button(text: String, color: Int, block: (String) -> Unit): DialogButton {
        val b = DialogButton(text, color, block)
        buttons.add(b)
        return b
    }

    fun risk(text: String, block: (String) -> Unit) {
        button(text, Colors.Risk, block)
    }

    fun safe(text: String, block: (String) -> Unit) {
        button(text, Colors.GreenMajor, block)
    }

    fun normal(text: String, block: (String) -> Unit) {
        button(text, Colors.TextColorMajor, block)
    }

    fun cancel(text: String = "取消") {
        normal(text) {}
    }

    fun ok(text: String = "确定", block: (String) -> Unit) {
        safe(text, block)
    }

    fun buttons(block: DialogX.() -> Unit) {
        this.block()
    }


    fun body(view: View) {
        this.bodyView = view
    }

    fun body(block: DialogX.() -> View) {
        this.bodyView = this.block()
    }

    fun bodyText(text: String, block: TextView.() -> Unit = {}) {
        val tv = context.createTextViewB()
        tv.padding(15, 10, 15, 10)
        tv.text = text
        tv.gravityCenter()
        tv.linkifyAll()
        tv.block()
        body(tv)
    }

    fun bodyInput(block: EditText.() -> Unit) {
        val rl = context.createRelative()
        val ed = rl.editX(RParam.CenterInParent.WidthFill.HeightWrap.margins(15)) {
            minimumWidth = dp(200)
            minimumHeight = dp(ViewSize.EditHeight)
            this.block()
        }
        beforeDismiss = {
            result.add(ed.textS)
        }
        body(rl)
    }

    fun bodyInputLines(block: EditText.() -> Unit) {
        val rl = context.createRelative()
        val ed = rl.edit(RParam.CenterInParent.WidthFill.HeightWrap.margins(15)) {
            minimumWidth = dp(200)
            minimumHeight = dp(ViewSize.EditHeight * 5)
            this.multiLine()
            minLines = 5
            gravityTopLeft()
            padding(5)
            this.block()
        }
        beforeDismiss = {
            result.add(ed.textS)
        }
        body(rl)
    }

    fun bodyList(block: SimpleListView.() -> Unit): SimpleListView {
        bodyViewParam = LParam.WidthFill.HeightFlex
        val lv = SimpleListView(context)
        body(lv)
        lv.block()
        return lv
    }

    fun bodyListString(block: (Any) -> String = { it.toString() }): SimpleListView {
        return bodyList {
            anyAdapter.onBindView = { v, p ->
                (v as TextView).text = block(anyAdapter.item(p))
            }
        }
    }

    fun bodyListCheck(block: CheckListView.() -> Unit): CheckListView {
        bodyViewParam = LParam.WidthFill.HeightFlex
        val lv = CheckListView(context)
        body(lv)
        lv.block()
        return lv
    }

    fun bodyCheckString(block: (Any) -> String = { it.toString() }): CheckListView {
        return bodyListCheck {
            onBindView = { v, p ->
                (v as TextView).text = block(anyAdapter.item(p))
            }
        }
    }

    fun bodyGrid(block: SimpleGridView.() -> Unit): SimpleGridView {
        bodyViewParam = LParam.WidthFill.HeightFlex
        val lv = SimpleGridView(context)
        lv.verticalSpacing = dp(5)
        body(lv)
        lv.block()
        return lv
    }


    private fun createView() {
        if (title.isNotEmpty()) {
            rootLayout.textView(LParam.WidthFill.height(titleHeight)) {
                textColorWhite().textSizeTitle()
                backColorTheme()
                if (TitleBar.TitleCenter) {
                    gravityCenter()
                } else {
                    padding(15, 0, 0, 0)
                    gravityLeftCenter()
                }
                textS = title
            }
        }

        bodyView.minimumHeight = dp(60)
        rootLayout.addView(bodyView, bodyViewParam)

        if (buttons.isNotEmpty()) {
            rootLayout.linearHor(LParam.WidthFill.HeightButton) {
                divider()
                for (b in buttons) {
                    textView(LParam.gravityCenter().HeightFill.WidthFlex) {
                        textSizeA()
                        textColor(b.color)
                        backColor(b.backColor)
                        padding(15, 10, 15, 10)
                        gravityCenter()
                        textS = b.text
                        onClick {
                            dismiss()
                            b.callback(b.text)
                        }
                    }
                }
            }
        }
    }


    fun show() {
        createView()
        dialog.window?.attributes = windowParam
        dialog.setContentView(cardView)
        dialog.setOnDismissListener {
            onDismiss(this)
        }
        dialog.show()
        val tm = timeoutToCloseSeconds
        if (tm > 1) {
            Task.foreDelay(tm * 1000L) {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
        }
    }


    fun dismiss() {
        beforeDismiss(this)
        dialog.dismiss()
    }

    fun gravityTop(yMargin: Int) {
        windowParam.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        windowParam.y = dp(yMargin)
        dialog.window!!.attributes = windowParam
    }

    class DialogButton(val text: String, val color: Int, val callback: (String) -> Unit) {
        var backColor: Int = Colors.WHITE
    }


    companion object {
        var timeoutToCloseSeconds = 0

        fun show(context: Context, block: DialogX.() -> Unit) {
            val d = DialogX(context)
            d.block()
            d.show()
        }

        fun alert(context: Context, msg: String) {
            alert(context, msg, null)
        }

        fun alert(context: Context, msg: String, title: String?, dismissCallback: () -> Unit = {}) {
            val d = DialogX(context)
            d.title(title)
            d.bodyText(msg) {
                if (msg.length < 16) {
                    gravityCenter()
                } else {
                    gravityLeftCenter()
                }
            }
            d.ok {}
            d.onDismiss = {
                dismissCallback()
            }
            d.show()
        }

        fun confirm(context: Context, msg: String, title: String?, onOK: () -> Unit) {
            val d = DialogX(context)
            d.title(title)
            d.bodyText(msg)
            d.cancel()
            d.ok {
                onOK()
            }
            d.show()
        }

        fun input(context: Context, title: String?, value: String = "", onOK: (String) -> Unit) {
            this.input(context, title, value, {}, onOK)
        }

        fun input(context: Context, title: String?, value: String, configBlock: (EditText) -> Unit, onOK: (String) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            d.bodyInput {
                this.textS = value
                configBlock(this)
            }
            d.cancel()
            d.ok {
                val s = d.result.first() as String
                onOK(s)
            }
            d.show()
        }

        fun inputLines(context: Context, title: String?, value: String = "", onOK: (String) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            d.bodyInputLines {
                this.textS = value
            }
            d.cancel()
            d.ok {
                val s = d.result.first() as String
                onOK(s)
            }
            d.show()
        }


        fun listAny(context: Context, items: List<Any>, title: String?, onResult: (Any) -> Unit) {
            this.listAny(context, items, title, { it.toString() }, onResult)
        }

        fun listAny(context: Context, items: List<Any>, title: String?, textBlock: (Any) -> String, onResult: (Any) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyListString(textBlock)
            lv.setItems(items)
            lv.onItemClick = {
                d.dismiss()
                onResult(it)
            }
            d.show()
        }

        fun listAnyN(context: Context, items: List<Any>, title: String?, textBlock: (Any) -> String, onResult: (Int) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyListString(textBlock)
            lv.setItems(items)
            lv.onItemClick2 = { _, _, p ->
                d.dismiss()
                onResult(p)
            }
            d.show()
        }

        fun <T : Any> listItem(context: Context, items: List<T>, title: String?, onResult: (T) -> Unit) {
            this.listItem(context, items, title, { it.toString() }, onResult)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> listItem(context: Context, items: List<T>, title: String?, textBlock: (T) -> String, onResult: (T) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyListString {
                textBlock(it as T)
            }
            lv.setItems(items)
            lv.onItemClick = {
                d.dismiss()
                onResult(it as T)
            }
            d.show()
        }


        fun <T : Any> listItemN(context: Context, items: List<T>, title: String?, onResult: (Int) -> Unit) {
            this.listItemN(context, items, title, { it.toString() }, onResult)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> listItemN(context: Context, items: List<T>, title: String?, textBlock: (T) -> String, onResult: (Int) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyListString {
                textBlock(it as T)
            }
            lv.setItems(items)
            lv.onItemClick2 = { _, _, p ->
                d.dismiss()
                onResult(p)
            }
            d.show()
        }

        fun checkAny(context: Context, items: List<Any>, checkedItems: List<Any>, title: String, onResult: (List<Any>) -> Unit) {
            this.checkAny(context, items, checkedItems, title, { it.toString() }, onResult)
        }

        fun checkAny(context: Context, items: List<Any>, checkedItems: List<Any>, title: String, textBlock: (Any) -> String, onResult: (List<Any>) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyCheckString(textBlock)
            lv.setItems(items)
            for (item in checkedItems) {
                lv.anyAdapter.checkItem(item)
            }
            d.cancel()
            d.ok {
                onResult(lv.anyAdapter.checkedItems)
            }
            d.show()
        }

        fun checkAnyN(context: Context, items: List<Any>, checkedItems: List<Any>, title: String, onResult: (Set<Int>) -> Unit) {
            this.checkAnyN(context, items, checkedItems, title, { it.toString() }, onResult)
        }

        fun checkAnyN(context: Context, items: List<Any>, checkedItems: List<Any>, title: String, textBlock: (Any) -> String, onResult: (Set<Int>) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyCheckString(textBlock)
            lv.setItems(items)
            for (item in checkedItems) {
                lv.anyAdapter.checkItem(item)
            }
            d.cancel()
            d.ok {
                onResult(lv.anyAdapter.checkedIndexs)
            }
            d.show()
        }


        fun <T : Any> checkItem(context: Context, items: List<T>, checkedItems: List<T>, title: String, onResult: (List<T>) -> Unit) {
            this.checkItem(context, items, checkedItems, title, { it.toString() }, onResult)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> checkItem(context: Context, items: List<T>, checkedItems: List<T>, title: String, textBlock: (T) -> String, onResult: (List<T>) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyCheckString {
                textBlock(it as T)
            }
            lv.setItems(items)
            for (item in checkedItems) {
                lv.anyAdapter.checkItem(item)
            }
            d.cancel()
            d.ok {
                onResult(lv.anyAdapter.checkedItems.map { it as T })
            }
            d.show()
        }

        fun <T : Any> checkItemN(context: Context, items: List<T>, checkedItems: List<T>, title: String, onResult: (Set<Int>) -> Unit) {
            this.checkItemN(context, items, checkedItems, title, { it.toString() }, onResult)
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> checkItemN(context: Context, items: List<T>, checkedItems: List<T>, title: String, textBlock: (T) -> String, onResult: (Set<Int>) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyCheckString {
                textBlock(it as T)
            }
            lv.setItems(items)
            for (item in checkedItems) {
                lv.anyAdapter.checkItem(item)
            }
            d.cancel()
            d.ok {
                onResult(lv.anyAdapter.checkedIndexs)
            }
            d.show()
        }

        fun gridAny(context: Context, items: List<Any>, configBlock: (DialogX, SimpleGridView) -> Unit, onResult: (Any) -> Unit) {
            val d = DialogX(context)
            d.bodyGrid {
                configBlock(d, this)
                setItems(items)
                onItemClick = {
                    d.dismiss()
                    onResult(it)
                }
            }
            d.show()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> gridItem(context: Context, items: List<T>, configBlock: (DialogX, SimpleGridView) -> Unit, onResult: (T) -> Unit) {
            val d = DialogX(context)
            d.bodyGrid {
                configBlock(d, this)
                setItems(items)
                onItemClick = {
                    d.dismiss()
                    onResult(it as T )
                }
            }
            d.show()
        }

    }

}

