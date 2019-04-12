package dev.entao.ui.page

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import dev.entao.appbase.App
import dev.entao.log.Yog
import dev.entao.ui.R
import dev.entao.ui.ext.act
import dev.entao.ui.list.ListPage
import dev.entao.ui.list.views.TextDetailView
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
		val files = file.listFiles()
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
		goUp()
	}

	private fun goUp() {
		val f = file.parentFile
		if (f != null) {
			DirSelectPage.open(act, f, onValue)
			finish()
		}
	}

	override fun onBindView(itemView: View, position: Int) {
		val item  = getItem(position) as File
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
			DirSelectPage.open(act, item, onValue)
			finish()
		}
	}


	companion object {
		private val SELECT = "选择"

		fun open(context: Context, onValue: (File) -> Unit) {
			open(context, Environment.getExternalStorageDirectory(), onValue)
		}

		fun open(context: Context, dir: File, onValue: (File) -> Unit) {
			if (App.debug && !dir.isDirectory) {
				Yog.fatal("应该给一个存在的目录做参数")
			}
			val page = DirSelectPage()
			page.file = dir
			page.onValue = onValue
			dev.entao.ui.activities.Pages.open(context, page)
		}
	}

}
