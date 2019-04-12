@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import dev.entao.log.logd
import dev.entao.ui.R
import dev.entao.ui.ext.currentFragment
import dev.entao.ui.ext.fragMgr
import dev.entao.ui.viewcreator.createFrame

open class ContainerActivity : AppCompatActivity() {

    private lateinit var containerView: FrameLayout

    private val containerId: Int
        get() = containerView.id

    val pageSize: Int get() = this.fragMgr.fragments.size

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
        b.addToBackStack(null)
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

    fun popToBottom() {
        while (fragMgr.backStackEntryCount > 1) {
            fragMgr.popBackStackImmediate()
        }
    }
}


