@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.ui.base

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.widget.FrameLayout
import dev.entao.log.logd
import dev.entao.ui.R
import dev.entao.ui.creator.createFrame

open class ContainerActivity : BaseActivity() {

    protected lateinit var containerView: FrameLayout

    private val containerId: Int
        get() = containerView.id

    val backCount: Int get() = this.fragMgr.backStackEntryCount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        containerView = this.createFrame()
        setContentView(containerView)
    }


    override fun onBackPressed() {
        if ((this.currentFragment as? BaseFragment)?.onBackPressed() == true) {
            return
        }
        if (fragMgr.backStackEntryCount > 1) {
            fragMgr.popBackStack()
        } else {
            finish()
        }
    }


    fun push(fragment: BaseFragment, pushAnim: Boolean, popAnim: Boolean) {
        val b = fragMgr.beginTransaction()
        if (fragMgr.backStackEntryCount > 0) {
            if (pushAnim || popAnim) {
                b.setCustomAnimations(
                    if (pushAnim) R.animator.yet_right_in else 0,
                    if (pushAnim) R.animator.yet_fade_out else 0,
                    if (popAnim) R.animator.yet_fade_in else 0,
                    if (popAnim) R.animator.yet_right_out else 0
                )
            }
        }
        b.add(containerId, fragment)
        b.addToBackStack(fragment.uniqueName)
        b.commitAllowingStateLoss()
    }

    fun push(fragment: BaseFragment) {
        push(fragment, pushAnim = true, popAnim = true)
    }

    fun pop() {
        logd("pop")
        if (fragMgr.backStackEntryCount > 1) {
            fragMgr.popBackStack()
        } else {
            logd("finish")
            finish()
        }
    }

    fun pop(page: BaseFragment, immediate: Boolean = false) {
        if (immediate) {
            fragMgr.popBackStackImmediate(page.uniqueName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            fragMgr.popBackStack(page.uniqueName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun popToBottom() {
        while (fragMgr.backStackEntryCount > 1) {
            fragMgr.popBackStackImmediate()
        }
    }
}


