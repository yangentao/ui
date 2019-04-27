@file:Suppress("unused")

package dev.entao.kan.dialogs

import android.app.Activity
import android.app.Dialog
import android.support.v4.app.Fragment
import android.view.Gravity
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.base.act
import dev.entao.kan.base.toast
import dev.entao.kan.ext.inputTypeNumber
import dev.entao.kan.ext.inputTypeNumberDecimal
import dev.entao.kan.grid.SimpleGridView

/**
 * Created by entaoyang@163.com on 16/5/10.
 */


fun Dialog.gravityCenter(): Dialog {
    val lp = this.window.attributes
    lp.gravity = Gravity.CENTER
    return this
}

fun Dialog.gravityTop(margin: Int = 0): Dialog {
    val lp = this.window.attributes
    lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    lp.y = dp(margin)
    return this
}

fun Dialog.gravityBottom(margin: Int = 0): Dialog {
    val lp = this.window.attributes
    lp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
    lp.y = dp(margin)
    return this
}

fun Fragment.showDialog(block: DialogX.() -> Unit): DialogX {
    val d = DialogX(act)
    d.block()
    d.show()
    return d
}

fun Fragment.alert(msg: String) {
    DialogX.alert(this.act, msg)
}

fun Activity.alert(msg: String) {
    DialogX.alert(this, msg)
}

fun Fragment.alert(msg: String, title: String? = null, dismissBlock: () -> Unit = {}) {
    if (activity != null) {
        DialogX.alert(act, msg, title, dismissBlock)
    } else {
        this.toast(msg)
    }
}

fun Activity.alert(msg: String, title: String? = null, dismissBlock: () -> Unit = {}) {
    DialogX.alert(this, msg, title, dismissBlock)
}


fun Fragment.confirm(title: String?, msg: String, block: () -> Unit) {
    DialogX.confirm(act, msg, title, block)
}

fun Activity.confirm(title: String?, msg: String, block: () -> Unit) {
    DialogX.confirm(this, msg, title, block)
}


fun Fragment.showInput(title: String, defaultValue: String = "", block: (String) -> Unit) {
    DialogX.input(act, title, defaultValue, block)
}

fun Activity.showInput(title: String, defaultValue: String = "", block: (String) -> Unit) {
    DialogX.input(this, title, defaultValue, block)
}


fun Fragment.showInputInteger(title: String, n: Long = 0, block: (Long) -> Unit) {
    DialogX.input(act, title, n.toString(), {
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

fun Activity.showInputInteger(title: String, n: Long = 0, block: (Long) -> Unit) {
    DialogX.input(this, title, n.toString(), {
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

fun Fragment.showInputDecimal(title: String, n: Long = 0, block: (Long) -> Unit) {
    DialogX.input(act, title, n.toString(), {
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

fun Activity.showInputDecimal(title: String, n: Long = 0, block: (Long) -> Unit) {
    DialogX.input(this, title, n.toString(), {
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

fun Fragment.showInputMultiLine(title: String, defaultValue: String = "", block: (String) -> Unit) {
    DialogX.inputLines(act, title, defaultValue, block)
}

fun Activity.showInputMultiLine(title: String, defaultValue: String = "", block: (String) -> Unit) {
    DialogX.inputLines(this, title, defaultValue, block)
}


fun <T : Any> Fragment.listItem(items: List<T>, title: String?, textBlock: (T) -> String, onResult: (T) -> Unit) {
    DialogX.listItem(act, items, title, textBlock, onResult)
}

fun <T : Any> Fragment.listItem(items: List<T>, title: String?, onResult: (T) -> Unit) {
    DialogX.listItem(act, items, title, onResult)
}

fun <T : Any> Fragment.gridItem(items: List<T>, configBlock: (DialogX, SimpleGridView) -> Unit, onResult: (T) -> Unit) {
    DialogX.gridItem(act, items, configBlock, onResult)
}