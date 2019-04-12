package dev.entao.utilapp

import android.widget.LinearLayout
import dev.entao.ui.activities.TitledActivity

class MainActivity : TitledActivity() {

    override fun onCreateContent(contentView: LinearLayout) {
        titleBar.title("Hello")
        titleBar.showBack()
    }



}


