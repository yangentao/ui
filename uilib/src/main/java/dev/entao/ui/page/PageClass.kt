package dev.entao.ui.page

import dev.entao.ui.base.BaseFragment
import dev.entao.ui.base.ContainerActivity
import dev.entao.ui.base.pushPage
import kotlin.reflect.full.createInstance

/**
 * Created by entaoyang@163.com on 2018-07-13.
 */

open class PageClass<T : BaseFragment> {
    val pageClass = javaClass.enclosingClass.kotlin

    @Suppress("UNCHECKED_CAST")
    fun open(parentPage: BaseFragment, block: T.() -> Unit = {}) {
        val p = pageClass.createInstance() as T
        p.block()
        parentPage.pushPage(p)
    }

    @Suppress("UNCHECKED_CAST")
    fun open(act: ContainerActivity, block: T.() -> Unit = {}) {
        val p = pageClass.createInstance() as T
        p.block()
        act.push(p)
    }
}