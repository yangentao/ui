package dev.entao.kan.util.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dev.entao.kan.appbase.App
import dev.entao.kan.base.yson

/**
 * Created by entaoyang@163.com on 2018-07-17.
 */

class NotifyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val yo = intent.yson ?: return
        val yetApp = App.inst as? YetApp
        yetApp?.onNotifyClick(yo)
    }

    companion object {
        private var nr: NotifyReceiver? = null
        fun reg() {
            if (nr != null) {
                return
            }
            val r = NotifyReceiver()
            this.nr = r
            val a = IntentFilter()
            App.inst.registerReceiver(r, a)
        }
    }

}