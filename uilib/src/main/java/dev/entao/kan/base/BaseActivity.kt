@file:Suppress("MemberVisibilityCanBePrivate", "unused", "ObjectPropertyName")

package dev.entao.kan.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.json.YsonObject
import dev.entao.kan.theme.MyColor
import dev.entao.kan.util.Msg
import dev.entao.kan.util.MsgCenter
import dev.entao.kan.util.MsgListener

/**
 * Created by yangentao on 16/3/12.
 */

open class BaseActivity : AppCompatActivity(), MsgListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.init(this.application)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        statusBarColorTheme()
        MsgCenter.listenAll(this)

        intentArg(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentArg(intent)
    }

    private fun intentArg(i: Intent?) {
        val yo = i?.yson ?: return
        Task.fore {
            onIntentYson(yo)
        }
    }

    open fun onIntentYson(yo: YsonObject) {

    }

    override fun onMsg(msg: Msg) {

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Perm.onPermResult(requestCode)
    }


    fun statusBarColor(color: Int) {
        val w = window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.statusBarColor = color
        }
    }

    fun statusBarColorTheme() {
        val c = MyColor(Colors.Theme)
        statusBarColor(c.multiRGB(0.7))
    }

    val findRootView: View
        get() = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)


    override fun onDestroy() {
        super.onDestroy()
        MsgCenter.remove(this)

    }


    override fun onStart() {
        visiableActivityCount += 1
        if (visiableActivityCount == 1) {
            val app = App.inst
            if (app is AppVisibleListener) {
                Task.fore {
                    app.onEnterForeground()
                }
            }
            Msg(MsgEnterForeground).fireCurrent()
        }
        _topActivity = this
        super.onStart()
    }


    override fun onStop() {
        visiableActivityCount -= 1
        if (visiableActivityCount <= 0) {
            _topActivity = null
            val app = App.inst
            if (app is AppVisibleListener) {
                Task.fore {
                    app.onEnterBackground()
                }
            }
            Msg(MsgEnterBackground).fireCurrent()
        }
        super.onStop()
    }

    companion object {
        const val MsgEnterBackground = "enter_background"
        const val MsgEnterForeground = "enter_foreground"
        var visiableActivityCount: Int = 0
            private set

        private var _topActivity: BaseActivity? = null

        val topVisibleActivity: BaseActivity?
            get() {
                if (visiableActivityCount > 0) {
                    return _topActivity
                }
                return null
            }

    }

}
