@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.kan.base

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import dev.entao.kan.creator.createFrame
import dev.entao.kan.util.SyncQueue
import java.lang.ref.WeakReference

open class PageActivity : BaseActivity() {

    private lateinit var containerView: FrameLayout

    private val containerId: Int
        get() = containerView.id

    private val page: BasePage? = pageQueue.popFirst()?.get()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        containerView = this.createFrame()
        setContentView(containerView)
        val p = this.page
        if (p != null) {
            setContentPage(p)
        }
    }


    override fun onBackPressed() {
        if ((this.currentFragment as? BasePage)?.onBackPressed() == true) {
            return
        }
        finish()
    }

    fun setContentPage(fragment: BasePage) {
        trans {
            replace(containerId, fragment)
        }
    }

    companion object {
        private val pageQueue = SyncQueue<WeakReference<BasePage>>()

        fun <T : BasePage> openPage(context: Context, page: T) {
            this.openPage(context, page) {}
        }

        fun <T : BasePage> openPage(context: Context, page: T, block: T.() -> Unit) {
            page.block()
            pageQueue.add(WeakReference(page))
            context.openActivity(PageActivity::class)

        }
    }

}


val Fragment.pageAct: PageActivity get() = this.act as PageActivity
val Fragment.pageActivity: PageActivity? get() = this.activity as? PageActivity
