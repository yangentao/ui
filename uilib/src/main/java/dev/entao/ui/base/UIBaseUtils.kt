package dev.entao.ui.base

import android.support.v4.app.Fragment


val Fragment.containerActivity: ContainerActivity get() = this.act as ContainerActivity


fun BaseFragment.pushPage(p: BaseFragment, pushAnim: Boolean, popAnim: Boolean) {
    this.containerActivity.push(p, pushAnim, popAnim)
}

fun BaseFragment.pushPage(p: BaseFragment) {
    this.containerActivity.push(p)
}

fun BaseFragment.popPage() {
    this.containerActivity.pop()
}