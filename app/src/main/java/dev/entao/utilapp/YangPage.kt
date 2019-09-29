package dev.entao.utilapp

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import dev.entao.kan.base.TabLayoutPage

class YangPage : TabLayoutPage() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.add("A", R.drawable.yet_me, APage())
        this.add("B", R.drawable.yet_del, BPage())
    }

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Yang")
        }
        this.tabLayout.tabGravity
    }
}