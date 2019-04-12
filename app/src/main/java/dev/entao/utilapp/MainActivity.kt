package dev.entao.utilapp

import android.os.Bundle
import dev.entao.ui.base.ContainerActivity
import dev.entao.ui.base.TabBarPage

class MainActivity : ContainerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabPage = TabBarPage()
        push(tabPage)
        tabPage.onReady = {
            it.tab("Hello", R.mipmap.ic_launcher, HelloPage())
            it.tab("Yang", R.mipmap.ic_launcher, YangPage())
            it.commit()
            it.selectTab("Hello")
        }
    }

}


