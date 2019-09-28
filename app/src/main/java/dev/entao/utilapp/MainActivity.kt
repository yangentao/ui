package dev.entao.utilapp

import android.os.Bundle
import dev.entao.kan.base.BottomNavPage
import dev.entao.kan.base.StackActivity

class MainActivity : StackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = BottomNavPage()
        p.add("Hello", R.drawable.yet_del, HelloPage())
        p.add("Yang", R.drawable.yet_me, YangPage())
        setContentPage(p)


    }

}


