package dev.entao.kan.base

import android.content.Context
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.colors
import dev.entao.kan.creator.addViewX
import dev.entao.kan.ext.*
import dev.entao.kan.page.TitlePage

open class TabLayoutPage : TitlePage() {
    var tabLayoutTop = true
    val tabLayoutStyle = com.google.android.material.R.style.Widget_MaterialComponents_TabLayout
    lateinit var tabLayout: TabLayout
    lateinit var pager: ViewPager
    val tabItems = ArrayList<TitleIconPageItem>()
    var colorNormal: Int = Colors.TextColorMinor
    var colorSelected: Int = Colors.Theme


    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello Tab")
        }
        contentView.apply {
            if (tabLayoutTop) {
                tabLayout = addViewX(TabLayout(context, null, tabLayoutStyle).genId(), LParam.WidthFill.HeightWrap)
                pager = addViewX(ViewPager(context).genId(), LParam.WidthFill.HeightFlex)
            } else {
                pager = addViewX(ViewPager(context).genId(), LParam.WidthFill.HeightFlex)
                tabLayout = addViewX(TabLayout(context, null, tabLayoutStyle).genId(), LParam.WidthFill.HeightWrap)
            }
        }

        pager.adapter = object : FragmentStatePagerAdapter(this.fragMgr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return tabItems[position].page
            }

            override fun getCount(): Int {
                return tabItems.size
            }

        }


        val st = StateList.colors(colorNormal) {
            selected(colorSelected)
            checked(colorSelected)
        }
        tabLayout.tabIconTint = st
        tabLayout.tabTextColors = st


        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until tabLayout.tabCount) {
                    if (tabLayout.getTabAt(i) == tab) {
                        pager.currentItem = i
                        return
                    }
                }
            }
        })
        tabLayout.isInlineLabel = true
        applyChange()
    }

    fun applyChange() {
        tabLayout.removeAllTabs()
        for (item in this.tabItems) {
            val tab = tabLayout.newTab()
            tab.text = item.title
            tab.setIcon(item.icon)
            tabLayout.addTab(tab)
        }
        pager.adapter?.notifyDataSetChanged()
    }

    fun add(title: String, icon: Int, page: BasePage) {
        tabItems += TitleIconPageItem(title, icon, page)
    }

    fun add(title: String, page: BasePage) {
        tabItems += TitleIconPageItem(title, 0, page)
    }
}