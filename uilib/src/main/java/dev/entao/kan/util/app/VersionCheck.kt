@file:Suppress("unused")

package dev.entao.kan.util.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.Keep
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.sql.MapTable
import dev.entao.kan.base.BasePage
import dev.entao.kan.base.Progress
import dev.entao.kan.base.act
import dev.entao.kan.base.openActivity
import dev.entao.kan.dialogs.DialogX
import dev.entao.kan.http.Http
import dev.entao.kan.json.YsonObject
import dev.entao.kan.log.loge
import dev.entao.kan.util.ToastUtil
import java.io.File

/**
 * Created by entaoyang@163.com on 2017-06-13.
 */

@Keep
class VersionCheck(val jo: YsonObject) {
    val versionCode: Int by jo
    val versionName: String by jo
    val msg: String by jo
    val resId: Int by jo
    val download: String by jo

    fun great(): Boolean {
        return versionCode > App.versionCode
    }

    fun downloadFile(progress: Progress?): File? {
        val f = App.files.ex.temp(App.appName + ".apk")
        val r = Http(this.download).download(f, progress)
        if (r.OK) {
            return f
        }
        return null
    }


    companion object {
        var CHECK_URL: String = "http://app800.cn/am/check"
        var CHECK_HOURS = 4 //最多每4小时检查一次

        private val ignoreMap = MapTable("ver_ignore")

        private var lastCheckUpdate: Long by MapTable.config

        private fun isIgnored(verCode: Int): Boolean {
            return ignoreMap[verCode.toString()] != null
        }

        fun checkByUser(page: BasePage) {
            page.openActivity(VersionUpgradeActivity::class)
        }

        fun checkAuto(page: BasePage) {
            val last = lastCheckUpdate
            val now = System.currentTimeMillis()
            if (now - last < CHECK_HOURS * 60 * 60 * 1000) {
                return
            }
            Task.back {
                val v = check()
                Task.fore {
                    if (v != null) {
                        lastCheckUpdate = now
                        if (!isIgnored(v.versionCode)) {
                            if (page.activity != null) {
                                confirmInstall(page.act, v, true)
                            }
                        }
                    }

                }
            }
        }

        private fun confirmInstall(act: Activity, v: VersionCheck, quiet: Boolean) {
            if (!v.great()) {
                try {
                    if (!quiet) {
                        ToastUtil.showLong(act, "已经是最新版本")
                    }
                } catch (ex: Exception) {
                }
                return
            }

            DialogX(act).apply {
                title("检查升级")
                bodyText("发现新版本${v.versionName}")
                buttons {
                    ok("升级") {
                        act.openActivity(VersionUpgradeActivity::class)
                    }
                    normal("忽略此版本") {
                        ignoreMap.put(v.versionCode.toString(), v.versionName)
                    }
                    cancel("取消")
                }
                show()
            }
        }


        fun check(): VersionCheck? {
            val r = Http(CHECK_URL).arg("pkg", App.packageName).get()
            if (r.OK) {
                val jo = r.ysonObject() ?: return null
                if (jo.int("code") != 0) {
                    return null
                }
                val jdata = jo.obj("data") ?: return null
                return VersionCheck(jdata)
            }
            return null
        }

        fun checkAndInstallQuiet(context: Context) {
            Task.back {
                val v = check()
                if (v != null) {
                    if (v.great()) {
                        val f = v.downloadFile(null)
                        if (f != null) {
                            Task.fore {
                                installQuiet(context, f)
                            }
                        }
                    }
                }
            }
        }


        fun installQuiet(context: Context, file: File) {
            val apkUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(file)
            } else {
                FileProv.uriOfFile(file)
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            val ls = context.packageManager.queryIntentActivities(intent, 0)
            if (ls.isEmpty()) {
                loge("未找到APK安装程序")
                return
            }
            try {
                context.startActivity(intent)
            } catch (ex: Exception) {
                ex.printStackTrace()
                loge(ex)
            }
        }

    }
}