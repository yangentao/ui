package dev.entao.utilapp

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.base.ManiPerm
import dev.entao.kan.base.reqPerm
import dev.entao.kan.creator.createTextView
import dev.entao.kan.ext.LParam
import dev.entao.kan.ext.WidthFill
import dev.entao.kan.ext.backColor
import dev.entao.kan.ext.height
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

        val a = SlidingPaneLayout(context)
        contentView.addView(a, LParam.WidthFill.height(200))
        val b = createTextView()
        val c = createTextView()
//        val d = createTextView()
        a.addView(b, 100.dp, ViewGroup.LayoutParams.MATCH_PARENT)
        a.addView(c, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        a.addView(d, 150.dp, ViewGroup.LayoutParams.MATCH_PARENT)

        a.backColor(Color.DKGRAY)
        b.backColor(Colors.RedMajor)
        c.backColor(Colors.GreenMajor)
//        d.backColor(Colors.BlueMajor)

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
