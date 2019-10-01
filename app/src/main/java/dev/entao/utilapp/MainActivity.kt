package dev.entao.utilapp

import android.os.Bundle
import dev.entao.kan.base.BottomNavPage
import dev.entao.kan.base.StackActivity
import dev.entao.kan.util.Msg

class MainActivity : StackActivity() {
    val p = BottomNavPage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        p.add("Hello", R.drawable.yet_del, HelloPage())
        p.add("Yang", R.drawable.yet_me, YangPage())
        p.enableUserInput = false
        setContentPage(p)


    }

    override fun onMsg(msg: Msg) {
        if (msg.isMsg("tab")) {
//            p.selectTab(1)
            p.selectTab {
                it.title == "Yang"
            }
            return
        }
        super.onMsg(msg)
    }

}


