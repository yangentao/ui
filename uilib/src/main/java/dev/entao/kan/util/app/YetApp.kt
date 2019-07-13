package dev.entao.kan.util.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dev.entao.kan.appbase.App
import dev.entao.kan.json.YsonObject
import dev.entao.kan.log.logd
import dev.entao.kan.base.AppVisibleListener

/**
 * Created by yet on 2015/10/10.
 */
open class YetApp : Application(), AppVisibleListener {

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}

	override fun onCreate() {
		super.onCreate()
		App.init(this)
	}

	override fun onEnterForeground() {
	}

	override fun onEnterBackground() {
	}

	open fun onNotifyClick(yo: YsonObject) {
		logd("onNotifyClick:", yo.toString())
	}
}
