package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.base.ManiPerm
import dev.entao.kan.base.reqPerm
import dev.entao.kan.log.logd
import dev.entao.kan.page.TitlePage
import dev.entao.kan.util.app.ApkDownReceiver

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightText("Down").onClick = {
                down()
            }
        }

    }

    fun down() {
        this.reqPerm(ManiPerm.WRITE_EXTERNAL_STORAGE) {
            logd("请求写download文件夹 $it ")
            if (it) {
                val a = "http://app800.cn/am/res/download?id=47"
                ApkDownReceiver.downloadAndInstall(a, "下载APK")
            }
        }

    }

}
