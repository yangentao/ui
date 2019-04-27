package dev.entao.kan.util.app

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dev.entao.kan.util.Msg
import dev.entao.kan.util.MsgCenter
import dev.entao.kan.util.MsgListener

/**
 * Created by entaoyang@163.com on 2017-03-17.
 */

open class BaseService : Service(), MsgListener {

	override fun onCreate() {
		super.onCreate()
		MsgCenter.listenAll(this)
	}

	override fun onDestroy() {
		super.onDestroy()
		MsgCenter.remove(this)
	}

	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	override fun onMsg(msg: Msg) {

	}
}
