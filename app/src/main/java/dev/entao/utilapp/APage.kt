package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.base.PageActivity
import dev.entao.kan.page.TitlePage

class APage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("A")
            rightText("Open").onClick = {
                //                pushPage(YangPage(), true, true)
                PageActivity.openPage(requireActivity(), BPage())
            }
        }
    }
}