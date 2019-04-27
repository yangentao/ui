@file:Suppress("unused")

package dev.entao.kan.base

import android.support.v4.app.Fragment


val Fragment.containerActivity: ContainerActivity get() = this.act as ContainerActivity
val Fragment.containerAct: ContainerActivity? get() = this.activity as? ContainerActivity


fun BaseFragment.pushPage(p: BaseFragment, pushAnim: Boolean, popAnim: Boolean) {
    this.containerAct?.push(p, pushAnim, popAnim)
}

fun BaseFragment.pushPage(p: BaseFragment) {
    this.containerAct?.push(p)
}

fun <T : BaseFragment> BaseFragment.pushPage(p: T, block: T.() -> Unit) {
    p.block()
    this.containerAct?.push(p)
}

fun BaseFragment.popPage() {
    this.containerAct?.pop(this)
}