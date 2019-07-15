package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.kan.dialogs.dialogX
import dev.entao.kan.log.logd
import dev.entao.kan.page.TitlePage

class HelloPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightText("Test").onClick = {
                test()
            }
        }
    }

    fun test() {
        this.dialogX.showListItem(listOf("删除", "复制", "查看", "复制号码和姓名"), null) {
            logd(it)
        }
    }
}