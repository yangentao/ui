@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package dev.entao.kan.base


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.argb
import dev.entao.kan.appbase.ex.colors
import dev.entao.kan.creator.addViewX
import dev.entao.kan.creator.linearVer
import dev.entao.kan.ext.*

class TitleIconPageItem(val title: String, val icon: Int, val page: BasePage)

class BottomNavPage : BasePage() {
    var bottomNavStyle = com.google.android.material.R.style.Widget_MaterialComponents_BottomNavigationView
    var inactiveColor = 0x8a000000.argb
    var checkedColor: Int = ColorX.theme
    val navItems = ArrayList<TitleIconPageItem>()
    lateinit var bottomNav: BottomNavigationView
    lateinit var pager: ViewPager2

    var ready = false
        private set

    var onReady: (BottomNavPage) -> Unit = {}

    private var _enableUserInput = true
    var enableUserInput: Boolean
        get() {
            if (ready) {
                return this.pager.isUserInputEnabled
            } else {
                return _enableUserInput
            }
        }
        set(value) {
            if (ready) {
                this.pager.isUserInputEnabled = value
            } else {
                _enableUserInput = value
            }
        }

    fun selectTab(n: Int) {
        this.pager.setCurrentItem(n, false)
        bottomNav.menu.getItem(n).isChecked = true
    }

    fun selectTab(block: (TitleIconPageItem) -> Boolean) {
        for (i in this.navItems.indices) {
            if (block(navItems[i])) {
                selectTab(i)
                return
            }
        }
    }

    override fun onCreatePage(context: Context, pageView: RelativeLayout, savedInstanceState: Bundle?) {
        super.onCreatePage(context, pageView, savedInstanceState)
        pageView.linearVer(RParam.Fill) {
            pager = addViewX(ViewPager2(context), LParam.WidthFill.HeightFlex)
            val lineView = addViewX(View(context), LParam.WidthFill.height(1))
            lineView.backColor(0x44cccccc.argb)

            bottomNav = addViewX(BottomNavigationView(context, null, bottomNavStyle), LParam.WidthFill.HeightWrap)
        }
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return navItems.size
            }

            override fun createFragment(position: Int): Fragment {
                return navItems[position].page
            }
        }
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.menu.getItem(position).isChecked = true
            }
        })

        val cs = StateList.colors(inactiveColor) {
            selected(checkedColor)
            checked(checkedColor)
        }
        bottomNav.itemTextColor = cs
        bottomNav.itemIconTintList = cs
        bottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        bottomNav.setOnNavigationItemSelectedListener {
            val m = bottomNav.menu
            for (i in 0 until m.size()) {
                if (m.getItem(i) === it) {
                    pager.setCurrentItem(i, false)
                    break
                }
            }
            true
        }
        bottomNav.menu.buildItems {
            for (item in navItems) {
                item.title TO item.icon
            }
        }

        pager.isUserInputEnabled = _enableUserInput
        ready = true
        onReady(this)
    }

    fun add(title: String, icon: Int, page: BasePage) {
        navItems += TitleIconPageItem(title, icon, page)
    }
}
