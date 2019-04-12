@file:Suppress("unused")

package dev.entao.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import dev.entao.base.getValue
import dev.entao.log.loge
import dev.entao.ui.dialogs.DialogX
import dev.entao.ui.dialogs.GridConfig
import dev.entao.ui.page.WebPage
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Created by entaoyang@163.com on 16/5/23.
 */
fun Context.openActivity(n: Intent) {
    try {
        this.startActivity(n)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}


fun Fragment.openActivity(cls: KClass<out Activity>, block: Intent.() -> Unit = {}) {
    val n = Intent(act, cls.java)
    n.block()
    act.openActivity(n)
}

fun Context.openActivity(cls: KClass<out Activity>, block: Intent.() -> Unit = {}) {
    val n = Intent(this, cls.java)
    n.block()
    this.openActivity(n)
}

fun Fragment.openActivity(cls: Class<out Activity>, block: Intent.() -> Unit = {}) {
    val n = Intent(act, cls)
    n.block()
    act.openActivity(n)
}

fun Context.openActivity(cls: Class<out Activity>, block: Intent.() -> Unit = {}) {
    val n = Intent(this, cls)
    n.block()
    this.openActivity(n)
}

fun Activity.setWindowFullScreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun Activity.setWindowFullScreen(full: Boolean) {
    if (full) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity.hasPermission(p: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}


fun Context.viewUrl(uri: Uri) {
    val it = Intent(Intent.ACTION_VIEW, uri)
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    openActivity(it)
}

fun Context.openApk(uri: Uri) {
    try {
        val i = Intent(Intent.ACTION_VIEW)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(i)
    } catch (e: Exception) {
        this.viewUrl(uri)
    }

}

val AppCompatActivity.fragMgr: FragmentManager
    get() {
        return this.supportFragmentManager
    }


val Fragment.fragMgr: FragmentManager
    get() {
        return this.requireFragmentManager()
    }

val AppCompatActivity.currentFragment: Fragment? get() = fragMgr.fragments?.lastOrNull()


val Fragment.act: FragmentActivity get() = this.requireActivity()

fun Fragment.openWeb(title: String, url: String) {
    WebPage.open(containerActivity, title, url)
}

fun Fragment.openAssetHtml(title: String, file: String) {
    WebPage.openAsset(containerActivity, title, file)
}

fun Fragment.smsTo(phoneSet: Set<String>, body: String = "") {
    if (phoneSet.isNotEmpty()) {
        smsTo(phoneSet.joinToString(";"), body)
    }
}

fun Fragment.smsTo(phone: String, body: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone"))
    if (body.isNotEmpty()) {
        intent.putExtra("sms_body", body)
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    act.startActivity(intent)
}


fun Fragment.dial(phone: String) {
    try {
        val uri = Uri.fromParts("tel", phone, null)
        val it = Intent(Intent.ACTION_DIAL, uri)
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        act.startActivity(it)
    } catch (e: Throwable) {
        loge(e)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Fragment.selectItemT(title: String, items: Collection<T>, displayBlock: (T) -> String, resultBlock: (T) -> Unit) {
    DialogX.listItem(act, items.toList(), title, { displayBlock(it as T) }, { resultBlock(it as T) })
}


fun <T : Any> Fragment.selectItemT(title: String, items: Collection<T>, prop: KProperty1<*, *>, resultBlock: (T) -> Unit) {
    selectItemT(title, items, { prop.getValue(it)?.toString() ?: "" }, resultBlock)
}

fun Fragment.selectItem(items: Collection<Any>, prop: KProperty1<*, *>, resultBlock: (Any) -> Unit) {
    selectItem(items, { prop.getValue(it)?.toString() ?: "" }, resultBlock)
}

fun Fragment.selectItem(items: Collection<Any>, displayBlock: (Any) -> String, resultBlock: (Any) -> Unit) {
    DialogX.listItem(act, items.toList(), "", displayBlock, resultBlock)
}

fun Fragment.selectString(items: Collection<String>, resultBlock: (String) -> Unit) {
    DialogX.listItem(act, items.toList(), "", { it as String }) {
        resultBlock(it as String)
    }
}

fun Fragment.selectStringN(items: Collection<String>, block: (Int) -> Unit) {
    DialogX.listStringN(act, items.toList(), "", block)
}


fun Fragment.selectGrid(items: List<Any>, callback: GridConfig.() -> Unit) {
    DialogX.selectGrid(act, items, callback)
}

fun Fragment.showDialog(block: DialogX.() -> Unit): DialogX {
    val d = DialogX(act)
    d.block()
    d.show()
    return d
}