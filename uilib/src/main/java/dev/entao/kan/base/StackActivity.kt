@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.kan.base

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.Window
import android.widget.FrameLayout
import dev.entao.kan.creator.createFrame
import dev.entao.kan.log.logd
import dev.entao.kan.ui.R

open class StackActivity : BaseActivity() {


    protected lateinit var containerView: FrameLayout
        private set

    private val containerId: Int
        get() = containerView.id

    val backCount: Int get() = this.fragMgr.backStackEntryCount

    var doubleBack = false
    private var lastBackTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        containerView = this.createFrame()
        setContentView(containerView)
    }


    override fun onBackPressed() {
        if ((this.currentFragment as? BasePage)?.onBackPressed() == true) {
            return
        }
        if (backCount > 0) {
            fragMgr.popBackStack()
        } else {
            if (allowFinish()) {
                finish()
            }
        }
    }

    open fun allowFinish(): Boolean {
        if (doubleBack) {
            val cur = System.currentTimeMillis()
            if (cur - lastBackTime < 2000) {
                return true
            }
            lastBackTime = cur
            toast("再按一次返回键退出")
            return false
        } else {
            return true
        }
    }


    fun setContentPage(fragment: BasePage) {
        trans {
            replace(containerId, fragment)
        }
    }

    fun <T : BasePage> setContentPage(fragment: T, block: T.() -> Unit) {
        fragment.block()
        setContentPage(fragment)
    }

    fun push(fragment: BasePage, pushAnim: Boolean, popAnim: Boolean) {
        trans {
            if (fragMgr.fragments.size > 0) {
                if (pushAnim || popAnim) {
                    setCustomAnimations(
                        if (pushAnim) R.animator.yet_right_in else 0,
                        if (pushAnim) R.animator.yet_fade_out else 0,
                        if (popAnim) R.animator.yet_fade_in else 0,
                        if (popAnim) R.animator.yet_right_out else 0
                    )
                }
            }
            add(containerId, fragment)
            addToBackStack(fragment.uniqueName)
        }
    }

    fun push(fragment: BasePage) {
        push(fragment, pushAnim = true, popAnim = true)
    }

    fun pop() {
        logd("pop")
        if (backCount > 0) {
            fragMgr.popBackStack()
        } else {
            logd("finish")
            finish()
        }
    }

    fun pop(page: BasePage, immediate: Boolean = false) {
        if (immediate) {
            fragMgr.popBackStackImmediate(page.uniqueName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            fragMgr.popBackStack(page.uniqueName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun popAll() {
        while (backCount > 0) {
            fragMgr.popBackStackImmediate()
        }
    }
}


