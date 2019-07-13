@file:Suppress("unused", "ObjectLiteralToLambda")

package dev.entao.kan.base

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dev.entao.kan.appbase.Task
import dev.entao.kan.base.ex.extraString
import dev.entao.kan.json.YsonObject
import dev.entao.kan.log.loge
import kotlin.reflect.KClass


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


fun Fragment.openActivity(cls: KClass<out Activity>) {
    val n = Intent(act, cls.java)
    act.openActivity(n)
}

fun Fragment.openActivity(cls: KClass<out Activity>, block: Intent.() -> Unit) {
    val n = Intent(act, cls.java)
    n.block()
    act.openActivity(n)
}

fun Fragment.openActivity(cls: Class<out Activity>) {
    val n = Intent(act, cls)
    act.openActivity(n)
}

fun Fragment.openActivity(cls: Class<out Activity>, block: Intent.() -> Unit) {
    val n = Intent(act, cls)
    n.block()
    act.openActivity(n)
}

fun Context.openActivity(cls: KClass<out Activity>) {
    val n = Intent(this, cls.java)
    this.openActivity(n)
}

fun Context.openActivity(cls: KClass<out Activity>, block: Intent.() -> Unit) {
    val n = Intent(this, cls.java)
    n.block()
    this.openActivity(n)
}

fun Context.openActivity(cls: Class<out Activity>) {
    val n = Intent(this, cls)
    this.openActivity(n)
}

fun Context.openActivity(cls: Class<out Activity>, block: Intent.() -> Unit) {
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


val AppCompatActivity.fragMgr: FragmentManager
    get() {
        return this.supportFragmentManager
    }


val Fragment.fragMgr: FragmentManager
    get() {
        return this.requireFragmentManager()
    }

fun AppCompatActivity.trans(block: FragmentTransaction.() -> Unit) {
    val b = fragMgr.beginTransaction()
    b.block()
    b.commitAllowingStateLoss()
}


fun Fragment.trans(block: FragmentTransaction.() -> Unit) {
    val b = fragMgr.beginTransaction()
    b.block()
    b.commitAllowingStateLoss()
}


val AppCompatActivity.currentFragment: Fragment? get() = fragMgr.fragments.lastOrNull()


val Fragment.act: FragmentActivity get() = this.requireActivity()


val Fragment.stackAct: StackActivity get() = this.act as StackActivity
val Fragment.stackActivity: StackActivity? get() = this.activity as? StackActivity


val Fragment.pageAct: PageActivity get() = this.act as PageActivity
val Fragment.pageActivity: PageActivity? get() = this.activity as? PageActivity


fun BasePage.pushPage(p: BasePage, pushAnim: Boolean, popAnim: Boolean) {
    this.stackActivity?.push(p, pushAnim, popAnim)
}

fun BasePage.pushPage(p: BasePage) {
    this.stackActivity?.push(p)
}

fun <T : BasePage> BasePage.pushPage(p: T, block: T.() -> Unit) {
    p.block()
    this.stackActivity?.push(p)
}

fun BasePage.popPage() {
    this.stackActivity?.pop(this)
}


fun Fragment.softInputAdjustResize() {
    act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

fun Fragment.hideInputMethod() {
    val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive && act.currentFocus != null) {
        imm.hideSoftInputFromWindow(
            act.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Activity.hideInputMethod() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val v = this.currentFocus ?: return
    if (imm.isActive) {
        imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Fragment.showInputMethod() {
    val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // 显示或者隐藏输入法
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Fragment.toast(vararg texts: Any) {
    this.activity?.toast(*texts)
}

fun Activity.toast(vararg texts: Any) {
    val s = texts.joinToString(", ") { it.toString() }
    Task.fore {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}


fun Fragment.viewImage(uri: Uri) {
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = Intent.ACTION_VIEW
    intent.setDataAndType(uri, "image/*")
    startActivity(intent)
}

fun Context.viewImage(uri: Uri) {
    this.viewAction(uri, "image/*")
}

fun Context.viewAction(uri: Uri, dataType: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setDataAndType(uri, dataType)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.openActivity(intent)
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


fun Fragment.pickDate(initDate: Long, block: (Long) -> Unit) {
    pickDate(MyDate(initDate), block)
}

fun Fragment.pickDate(date: MyDate, block: (Long) -> Unit) {
    val dlg = DatePickerDialog(act, object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            block(MyDate.makeDate(year, monthOfYear, dayOfMonth))
        }

    }, date.year, date.month, date.day)
    dlg.show()
}

fun Fragment.pickTime(time: Long, block: (Long) -> Unit) {
    pickTime(MyDate(time), block)
}

fun Fragment.pickTime(time: MyDate, block: (Long) -> Unit) {
    val dlg = TimePickerDialog(activity, object : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            block(MyDate.makeTime(hourOfDay, minute))
        }

    }, time.hour, time.minute, true)
    dlg.show()
}


val Context.configuration: Configuration
    get() = resources.configuration

val Context.isPortrait: Boolean get() = this.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
val Context.isLandscape: Boolean get() = this.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


val Intent.yson: YsonObject?
    get() {
        val s = this.extraString("intent_arg") ?: return null
        return YsonObject(s)
    }

fun Intent.yson(yo: YsonObject): Intent {
    this.putExtra("intent_arg", yo.toString())
    return this
}


