@file:Suppress("unused")

package dev.entao.kan.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import dev.entao.kan.creator.frame
import dev.entao.kan.creator.linearVer
import dev.entao.kan.ext.*
import dev.entao.kan.widget.TabBar
import java.util.*

class TabBarPage : BasePage() {
    private lateinit var rootLinearView: LinearLayout
    private lateinit var containerView: FrameLayout
    private var containerId = 0

    lateinit var tabBar: TabBar
        private set

    private val pages = HashMap<String, BasePage>()

    var onReady: (TabBarPage) -> Unit = {}

    override fun onCreatePage(context: Context, pageView: RelativeLayout, savedInstanceState: Bundle?) {
        tabBar = TabBar(context)
        rootLinearView = pageView.linearVer(RParam.Fill) {
            containerView = frame(LParam.WidthFill.HeightFlex) {
                containerId = this.id
            }
            addGrayLine()
            addView(tabBar, Param.WidthFill.HeightBar)
        }
        tabBar.onSelect = {
            this@TabBarPage.onTabBarSelect(it.text)
        }
        onReady(this)
    }

    fun commit() {
        tabBar.commit()
    }

    fun selectTab(tag: String) {
        tabBar.select(tag, true)
    }

    fun tab(text: String, icon: Int, page: BasePage) {
        tabBar.tab(text, icon)
        pages[text] = page
    }

    fun tab(text: String, drawable: Drawable, page: BasePage) {
        tabBar.tab(text, drawable)
        pages[text] = page
    }

    private fun onTabBarSelect(text: String) {
        val page = pages[text] ?: return
        val t = fragMgr.beginTransaction()
        val ps = pages.values
        var found = false
        for (f in fragMgr.fragments) {
            if (f === page) {
                t.show(f)
                found = true
            } else if (f in ps) {
                t.hide(f)
            }
        }
        if (!found) {
            t.add(containerId, page, null)
        }
        t.commitAllowingStateLoss()
    }

}