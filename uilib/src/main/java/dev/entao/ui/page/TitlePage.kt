package dev.entao.ui.page

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import dev.entao.appbase.Task
import dev.entao.appbase.ex.Colors
import dev.entao.base.Progress
import dev.entao.ui.base.BaseFragment
import dev.entao.ui.base.ContainerActivity
import dev.entao.ui.base.act
import dev.entao.ui.creator.append
import dev.entao.ui.creator.linearVer
import dev.entao.ui.ext.*
import dev.entao.ui.theme.MyColor
import dev.entao.ui.widget.BottomBar
import dev.entao.ui.widget.TitleBar
import dev.entao.ui.widget.TopProgressBar
import dev.entao.util.app.OS

open class TitlePage : BaseFragment(), Progress {
    lateinit var rootLinearView: LinearLayout
        private set

    lateinit var titleBar: TitleBar
        private set

    lateinit var contentView: LinearLayout
        private set

    var hasBottomBar = false
    var bottomBar: BottomBar? = null
        private set

    var hasTopProgress = true
    var topProgress: TopProgressBar? = null

    var hasSnak = false
    var snack: Snack? = null

    var contentCreated = false
        private set

    var enableContentScroll = false

    override fun onCreatePage(context: Context, pageView: RelativeLayout, savedInstanceState: Bundle?) {
        rootLinearView = pageView.linearVer(RParam.Fill) {
            backColorWhite()
            if (hasTopProgress) {
                topProgress = append(TopProgressBar(act).gone(), LParam.WidthFill.height(6))
            }
            titleBar = append(TitleBar(act), LParam.WidthFill.height(TitleBar.HEIGHT))
            if (hasSnak) {
                snack = append(Snack(act).gone(), LParam.WidthFill.HeightWrap.GravityCenterVertical)
            }
            if (enableContentScroll) {
                append(NestedScrollView(rootLinearView.context).genId(), LParam.WidthFill.height(0).weight(1)) {
                    contentView = linearVer(LParam.WidthFill.HeightWrap) {}
                }
            } else {
                contentView = linearVer(LParam.WidthFill.height(0).weight(1)) {}
            }
            if (hasBottomBar) {
                bottomBar = append(BottomBar(act), LParam.WidthFill.height(BottomBar.HEIGHT))
            }
        }


        val ac = this.act
        if (ac is ContainerActivity) {
            if (ac.backCount > 1) {
                titleBar.showBack()
            }
        } else {
            titleBar.showBack()
        }

        onCreateContent(this.act, contentView)
        titleBar.commit()
        bottomBar?.commit()
        if (OS.GE50) {
            val c = MyColor(Colors.Theme)
            statusBarColor(c.multiRGB(0.7))
        }
        onContentCreated()
        contentCreated = true
    }


    fun titleBar(block: TitleBar.() -> Unit) {
        titleBar.block()
    }


    open fun onContentCreated() {

    }

    open fun onCreateContent(context: Context, contentView: LinearLayout) {

    }


    override fun onProgressStart(total: Int) {
        topProgress?.show(100)
    }

    override fun onProgress(current: Int, total: Int, percent: Int) {
        topProgress?.visiable()
        topProgress?.setProgress(percent)
    }

    override fun onProgressFinish() {
        val p = this.topProgress ?: return
        Task.foreDelay(1500) {
            p.hide()
        }
    }

}
