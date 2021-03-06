@file:Suppress("unused")

package dev.entao.kan.page

import android.Manifest
import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import dev.entao.kan.base.act
import dev.entao.kan.base.reqPerm
import dev.entao.kan.list.ListPage
import dev.entao.kan.list.itemviews.TextDetailView
import java.io.File

class FileSelectPage : ListPage() {
    private val RootSD = Environment.getExternalStorageDirectory()
    var dir: File = Environment.getExternalStorageDirectory()
    lateinit var onValue: (File?) -> Unit

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title(dir.absolutePath)
            rightText("清除").onClick = {
                onValue(null)
                finish()
            }
        }

        reqPerm(Manifest.permission.READ_EXTERNAL_STORAGE) {
            requestItems()
        }
    }

    override fun onItemsRefreshed() {
        super.onItemsRefreshed()
        titleBar.title(dir.absolutePath)
        titleBar.commit()
    }

    override fun onNewView(context: Context, position: Int): View {
        return TextDetailView(context)
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

    override fun onRequestItems(): List<File> {
        val files = dir.listFiles()
        val ls: List<File> = listOf(*files).filter { !it.name.startsWith(".") }
        return ls.sortedBy { it.name }
    }

    override fun beforeSetAdapter() {
        super.beforeSetAdapter()
        val v = TextDetailView(act)
        v.setValues("上级目录..", null)
        listView.addHeaderView(v)
    }


    override fun onItemClickHeader(view: View, position: Int) {
        super.onItemClickHeader(view, position)
        goUp()
    }


    override fun onBackPressed(): Boolean {
        if (dir.absolutePath == RootSD.absolutePath) {
            return super.onBackPressed()
        }
        goUp()
        return true
    }

    private fun goUp() {
        if (dir.absolutePath == RootSD.absolutePath) {
            return
        }
        val f = dir.parentFile
        if (f != null) {
            dir = f
            reqPerm(Manifest.permission.READ_EXTERNAL_STORAGE) {
                requestItems()
            }
        }
    }


    override fun onItemClickAdapter(view: View, item: Any, position: Int) {
        item as File
        if (item.isDirectory) {
            dir = item
            requestItems()
        } else {
            onValue(item)
            finish()
        }
    }


}
