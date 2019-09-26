package dev.entao.kan.page

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.base.BasePage
import dev.entao.kan.base.Progress
import dev.entao.kan.base.StackActivity
import dev.entao.kan.base.act
import dev.entao.kan.creator.append
import dev.entao.kan.creator.linearVer
import dev.entao.kan.ext.*
import dev.entao.kan.theme.MyColor
import dev.entao.kan.widget.BottomBar
import dev.entao.kan.widget.TitleBar
import dev.entao.kan.widget.TopProgressBar

open class TitlePage : BasePage(), Progress {
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
        pageView.linearVer(RParam.Fill) {
            rootLinearView = this
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
        if (ac is StackActivity) {
            if (ac.backCount > 0) {
                titleBar.showBack()
            }
        } else {
            titleBar.showBack()
        }

        onCreateContent(this.act, contentView)
        titleBar.commit()
        bottomBar?.commit()
        val c = MyColor(Colors.Theme)
        statusBarColor(c.multiRGB(0.7))
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
        Task.fore {
            topProgress?.visiable()
            topProgress?.setProgress(percent)
        }
    }

    override fun onProgressFinish() {
        val p = this.topProgress ?: return
        Task.foreDelay(1500) {
            p.hide()
        }
    }

}
