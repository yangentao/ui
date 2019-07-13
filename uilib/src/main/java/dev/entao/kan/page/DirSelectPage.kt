@file:Suppress("unused")

package dev.entao.kan.page

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import dev.entao.kan.base.act
import dev.entao.kan.list.ListPage
import dev.entao.kan.list.itemviews.TextDetailView
import dev.entao.kan.ui.R
import java.io.File

class DirSelectPage : ListPage() {

    lateinit var file: File
    private var onValue: (File) -> Unit = {}

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title(file.absolutePath + "/")
            rightImage(R.drawable.yet_sel_all).onClick = {
                onValue(file)
                finish()
            }
        }
        requestItems()
    }

    override fun onNewView(context: Context, position: Int): View {
        return TextDetailView(context)
    }


    override fun onRequestItems(): List<File> {
        val ls = file.listFiles()?.filter { !it.name.startsWith(".") } ?: emptyList()
        return ls.sortedBy { it.name }
    }

    override fun beforeSetAdapter() {
        super.beforeSetAdapter()
        val v = TextDetailView(act)
        v.setValues("上级目录..", null)
        listView.addHeaderView(v)
    }

    override fun onItemClickHeader(view: View, position: Int) {
        goUp()
    }

    private fun goUp() {
        val f = file.parentFile
        if (f != null) {
            this.file = f
            requestItems()
        }
    }

    override fun onBindView(itemView: View, position: Int) {
        val item = getItem(position) as File
        val v = itemView as TextDetailView
        var s = item.name
        if (item.isDirectory) {
            s += "/"
        }
        v.setValues(s, null)

    }

    override fun onItemClickAdapter(view: View, item: Any, position: Int) {
        item as File
        if (item.isDirectory) {
            this.file = item
            requestItems()
        }
    }


}
