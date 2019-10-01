@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.kan.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.base.ColorX
import dev.entao.kan.base.act
import dev.entao.kan.creator.*
import dev.entao.kan.ext.*
import dev.entao.kan.grid.SimpleGridView
import dev.entao.kan.list.CheckListView
import dev.entao.kan.list.SimpleListView
import dev.entao.kan.list.itemviews.TextDetailView
import dev.entao.kan.list.itemviews.TextItemView
import dev.entao.kan.theme.ViewSize
import dev.entao.kan.widget.TitleBar

class DialogX(val context: Context) {
    private var titleHeight = 45
    val dialog = Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog)
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

    var timeoutToCloseSeconds = 0

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
//        windowParam.horizontalMargin = dp(30).toFloat()
//

        cardView.setCardBackgroundColor(Color.WHITE)
        cardView.cardElevation = dp(5).toFloat()
        cardView.preventCornerOverlap = false
        cardView.radius = ViewSize.DialogCorner.dp.toFloat()


        cardView.addView(rootLayout, FParam.WidthFill.HeightWrap)
        rootLayout.divider()

        cardView.minimumHeight = dp(80)
        val minW = App.screenWidthPx * 3 / 4
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
        button(text, ColorX.actionRed, block)
    }

    fun safe(text: String, block: (String) -> Unit) {
        button(text, ColorX.actionGreen, block)
    }

    fun normal(text: String, block: (String) -> Unit) {
        button(text, ColorX.textPrimary, block)
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
        tv.padding(15, 15, 15, 15)
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
            anyAdapter.onNewView = { c, _ ->
                val v = TextItemView(c)
                v.padding(16, 12, 16, 12)
                v
            }
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

    fun gravityCenter(): DialogX {
        windowParam.gravity = Gravity.CENTER
        return this
    }


    fun gravityBottom(margin: Int = 0): DialogX {
        windowParam.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        windowParam.y = dp(margin)
        return this
    }

    class DialogButton(val text: String, val color: Int, val callback: (String) -> Unit) {
        var backColor: Int = Color.WHITE
    }


    fun showAlert(msg: String) {
        showAlert(msg, null)
    }

    fun showAlert(msg: String, title: String?, dismissCallback: () -> Unit = {}) {
        val d = this
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


    fun showConfirm(msg: String, title: String?, onOK: () -> Unit) {
        val d = this
        d.title(title)
        d.bodyText(msg)
        d.cancel()
        d.ok {
            onOK()
        }
        d.show()
    }


    fun showInput(title: String?, value: String = "", onOK: (String) -> Unit) {
        this.showInput(title, value, {}, onOK)
    }

    fun showInput(title: String?, value: String, configBlock: (EditText) -> Unit, onOK: (String) -> Unit) {
        val d = this
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

    fun showInputLines(title: String?, value: String = "", onOK: (String) -> Unit) {
        val d = this
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

    fun showInputInteger(title: String, n: Long = 0, block: (Long) -> Unit) {
        this.showInput(title, n.toString(), {
            it.inputTypeNumber()
        }) { s ->
            if (s.trim().isNotEmpty()) {
                val lval = s.trim().toLongOrNull()
                if (lval != null) {
                    block(lval)
                }
            }
        }
    }

    fun showInputDecimal(title: String, n: Long = 0, block: (Long) -> Unit) {
        this.showInput(title, n.toString(), {
            it.inputTypeNumberDecimal()
        }) { s ->
            if (s.trim().isNotEmpty()) {
                val lval = s.trim().toLongOrNull()
                if (lval != null) {
                    block(lval)
                }
            }
        }
    }


    fun showListAny(items: List<Any>, title: String?, onResult: (Any) -> Unit) {
        this.showListAny(items, title, { it.toString() }, onResult)
    }

    fun showListAny(items: List<Any>, title: String?, textBlock: (Any) -> String, onResult: (Any) -> Unit) {
        val d = this
        d.title(title)
        val lv = d.bodyListString(textBlock)
        lv.setItems(items)
        lv.onItemClick = {
            d.dismiss()
            onResult(it)
        }
        d.show()
    }

    fun showListAnyN(items: List<Any>, title: String?, textBlock: (Any) -> String, onResult: (Int) -> Unit) {
        val d = this
        d.title(title)
        val lv = d.bodyListString(textBlock)
        lv.setItems(items)
        lv.onItemClick2 = { _, _, p ->
            d.dismiss()
            onResult(p)
        }
        d.show()
    }

    fun <T : Any> showListItem(items: List<T>, title: String?, onResult: (T) -> Unit) {
        this.showListItem(items, title, { it.toString() }, onResult)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> showListItem(items: List<T>, title: String?, textBlock: (T) -> String, onResult: (T) -> Unit) {
        val d = this
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


    fun <T : Any> showListItemN(items: List<T>, title: String?, onResult: (Int) -> Unit) {
        this.showListItemN(items, title, { it.toString() }, onResult)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> showListItemN(items: List<T>, title: String?, textBlock: (T) -> String, onResult: (Int) -> Unit) {
        val d = this
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

    fun showCheckAny(items: List<Any>, checkedItems: List<Any>, title: String, onResult: (List<Any>) -> Unit) {
        this.showCheckAny(items, checkedItems, title, { it.toString() }, onResult)
    }

    fun showCheckAny(items: List<Any>, checkedItems: List<Any>, title: String, textBlock: (Any) -> String, onResult: (List<Any>) -> Unit) {
        val d = this
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

    fun showCheckAnyN(items: List<Any>, checkedItems: List<Any>, title: String, onResult: (Set<Int>) -> Unit) {
        this.showCheckAnyN(items, checkedItems, title, { it.toString() }, onResult)
    }

    fun showCheckAnyN(items: List<Any>, checkedItems: List<Any>, title: String, textBlock: (Any) -> String, onResult: (Set<Int>) -> Unit) {
        val d = this
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


    fun <T : Any> showCheckItem(items: List<T>, checkedItems: List<T>, title: String, onResult: (List<T>) -> Unit) {
        this.showCheckItem(items, checkedItems, title, { it.toString() }, onResult)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> showCheckItem(items: List<T>, checkedItems: List<T>, title: String, textBlock: (T) -> String, onResult: (List<T>) -> Unit) {
        val d = this
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

    fun <T : Any> showCheckItemN(items: List<T>, checkedItems: List<T>, title: String, onResult: (Set<Int>) -> Unit) {
        this.showCheckItemN(items, checkedItems, title, { it.toString() }, onResult)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> showCheckItemN(items: List<T>, checkedItems: List<T>, title: String, textBlock: (T) -> String, onResult: (Set<Int>) -> Unit) {
        val d = this
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

    fun showGridAny(items: List<Any>, configBlock: (DialogX, SimpleGridView) -> Unit, onResult: (Any) -> Unit) {
        val d = this
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
    fun <T : Any> showGridItem(items: List<T>, configBlock: (DialogX, SimpleGridView) -> Unit, onResult: (T) -> Unit) {
        val d = this
        d.bodyGrid {
            configBlock(d, this)
            setItems(items)
            onItemClick = {
                d.dismiss()
                onResult(it as T)
            }
        }
        d.show()
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : Any> showListDetail(items: List<T>, title: String?, textBlock: (T) -> Pair<String, String>, onResult: (T) -> Unit) {
        val d = this
        d.title(title)
        val lv = bodyList {
            anyAdapter.onNewView = { c, _ ->
                TextDetailView(c)
            }
            anyAdapter.onBindView = { v, p ->
                v as TextDetailView
                val pp = textBlock(anyAdapter.item(p) as T)
                v.setValues(pp.first, pp.second)
            }
        }
        lv.setItems(items)
        lv.onItemClick = {
            d.dismiss()
            onResult(it as T)
        }
        d.show()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> showListDetailN(items: List<T>, title: String?, textBlock: (T) -> Pair<String, String>, onResult: (Int) -> Unit) {
        val d = this
        d.title(title)
        val lv = bodyList {
            anyAdapter.onNewView = { c, _ ->
                TextDetailView(c)
            }
            anyAdapter.onBindView = { v, p ->
                v as TextDetailView
                val pp = textBlock(anyAdapter.item(p) as T)
                v.setValues(pp.first, pp.second)
            }
        }
        lv.setItems(items)
        lv.onItemClick2 = { _, _, p ->
            d.dismiss()
            onResult(p)
        }
        d.show()
    }

}


val Fragment.dialogX: DialogX
    get() {
        return DialogX(this.act)
    }
val Activity.dialogX: DialogX
    get() {
        return DialogX(this)
    }

