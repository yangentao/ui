package dev.entao.kan.page

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import dev.entao.kan.appbase.App
import dev.entao.kan.log.Yog
import dev.entao.kan.ui.R
import dev.entao.kan.base.ContainerActivity
import dev.entao.kan.base.containerAct
import dev.entao.kan.base.act
import dev.entao.kan.list.ListPage
import dev.entao.kan.list.itemviews.TextDetailView
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
			DirSelectPage.open(this.containerAct, f, onValue)
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
			DirSelectPage.open(this.containerAct, item, onValue)
			finish()
		}
	}


	companion object {
		private val SELECT = "选择"

		fun open(act: ContainerActivity, onValue: (File) -> Unit) {
			open(act, Environment.getExternalStorageDirectory(), onValue)
		}

		fun open(act: ContainerActivity, dir: File, onValue: (File) -> Unit) {
			if (App.debug && !dir.isDirectory) {
				Yog.fatal("应该给一个存在的目录做参数")
			}
			val page = DirSelectPage()
			page.file = dir
			page.onValue = onValue
			act.push(page)
		}
	}

}
