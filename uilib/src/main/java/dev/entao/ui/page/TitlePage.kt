package dev.entao.ui.page

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import dev.entao.appbase.ex.Colors
import dev.entao.base.Progress
import dev.entao.ui.activities.TabBarActivity
import dev.entao.ui.base.BaseFragment
import dev.entao.ui.base.ContainerActivity
import dev.entao.ui.ext.*
import dev.entao.ui.viewcreator.createLinearVertical
import dev.entao.ui.widget.BottomBar
import dev.entao.ui.widget.LinearLayoutX
import dev.entao.ui.widget.TitleBar
import dev.entao.ui.widget.TopProgressBar
import dev.entao.util.Task
import dev.entao.util.app.OS

open class TitlePage : BaseFragment(), Progress {
    lateinit var rootLinearView: LinearLayoutX
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
        rootLinearView = LinearLayoutX(act)
        pageView.addView(rootLinearView, RParam.Fill)
        rootLinearView.vertical()
        rootLinearView.backColorWhite()
        if (hasTopProgress) {
            val b = TopProgressBar(act).gone()
            rootLinearView.addView(b, LParam.WidthFill.height(6))
            topProgress = b
        }
        titleBar = TitleBar(act)
        rootLinearView.addView(titleBar, LParam.WidthFill.height(TitleBar.HEIGHT))


        if (hasSnak) {
            val v = Snack(act).gone()
            rootLinearView.addView(v, LParam.WidthFill.HeightWrap.GravityCenterVertical)
            snack = v
        }

        contentView = createLinearVertical()
        if (enableContentScroll) {
//			val scrollView = createScroll()
            val scrollView = NestedScrollView(rootLinearView.context).genId()
            rootLinearView.addView(scrollView, LParam.WidthFill.height(0).weight(1))
            scrollView.addView(contentView, LParam.WidthFill.HeightWrap)
        } else {
            rootLinearView.addView(contentView, LParam.WidthFill.height(0).weight(1))
        }
        if (hasBottomBar) {
            val bar = BottomBar(act)
            rootLinearView.addView(bar, LParam.WidthFill.height(BottomBar.HEIGHT))
            bottomBar = bar
        }

        val ac = this.activity
        if (ac !is TabBarActivity) {
            if (ac is ContainerActivity) {
                if (ac.pageSize == 0) {
                    titleBar.showBack()
                }
            } else {
                titleBar.showBack()
            }
        }

        onCreateContent(this.act, contentView)
        titleBar.commit()
        bottomBar?.commit()
        if (OS.GE50) {
            val c = dev.entao.ui.MyColor(Colors.Theme)
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
