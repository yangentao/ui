@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.kan.base

import android.os.Bundle
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
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
        this.pop()
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
//        val a = if (pushAnim) R.animator.yet_enter_right else 0
        //val b = if (popAnim) R.animator.yet_exit_right else 0
        val a = if (pushAnim) R.anim.yet_enter_right else 0
        val b = if (popAnim) R.anim.yet_exit_right else 0
        trans {
            if (fragMgr.fragments.size > 0) {
                if (pushAnim || popAnim) {
                    setCustomAnimations(a, 0, 0, b)
                }
            }
            add(containerId, fragment, fragment.uniqueName)
            addToBackStack(fragment.uniqueName)

//            add(containerId, fragment)
//            addToBackStack(null)
        }
    }

    fun push(fragment: BasePage) {
        push(fragment, pushAnim = true, popAnim = true)
    }

    fun pop() {
        if (backCount > 0) {
            logd("pop")
            fragMgr.popBackStack()
        } else {
            if (allowFinish()) {
                finish()
            }
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
//        fragMgr.popBackStackImmediate(null,0)
        while (backCount > 0) {
            fragMgr.popBackStackImmediate()
        }
    }
}


