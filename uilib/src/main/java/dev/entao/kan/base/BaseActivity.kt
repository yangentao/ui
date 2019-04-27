package dev.entao.kan.base

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.json.YsonObject
import dev.entao.kan.theme.MyColor
import dev.entao.kan.util.Msg
import dev.entao.kan.util.MsgCenter
import dev.entao.kan.util.MsgListener
import dev.entao.kan.util.app.yson
import dev.entao.kan.util.mergeAction
import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

open class BaseActivity : AppCompatActivity(), MsgListener {

    var fullScreen = false

    val watchMap = HashMap<Uri, ContentObserver>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.init(this.application)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (fullScreen) {
            setWindowFullScreen()
        }
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
        Task.foreDelay(100) {
            onIntentYson(yo)
        }
    }

    open fun onIntentYson(yo: YsonObject) {

    }

    override fun onMsg(msg: Msg) {

    }


    fun unWatch(uri: Uri) {
        val ob = watchMap[uri]
        if (ob != null) {
            contentResolver.unregisterContentObserver(ob)
        }
    }


    fun watch(uri: Uri, block: (Uri) -> Unit = {}) {
        if (watchMap.containsKey(uri)) {
            return
        }
        val ob = object : ContentObserver(Handler(Looper.getMainLooper())) {

            override fun onChange(selfChange: Boolean, uri: Uri) {
                mergeAction("watchUri:$uri") {
                    block(uri)
                    onUriChanged(uri)
                }
            }
        }
        watchMap[uri] = ob
        contentResolver.registerContentObserver(uri, true, ob)
    }

    open fun onUriChanged(uri: Uri) {

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

    fun toast(text: String) {
        Task.fore {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        MsgCenter.remove(this)
        for (ob in watchMap.values) {
            contentResolver.unregisterContentObserver(ob)
        }
        watchMap.clear()
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
        val MsgEnterBackground = "enter_background"
        val MsgEnterForeground = "enter_foreground"
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
