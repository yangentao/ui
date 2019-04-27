package dev.entao.kan.base

import android.os.Bundle
import android.widget.LinearLayout
import dev.entao.kan.ext.LParam
import dev.entao.kan.ext.WidthFill
import dev.entao.kan.ext.backColorWhite
import dev.entao.kan.ext.height
import dev.entao.kan.creator.createLinearVertical
import dev.entao.kan.widget.TitleBar

/**
 * Created by entaoyang@163.com on 16/4/14.
 */

abstract class TitledActivity : BaseActivity() {
	lateinit var rootView: LinearLayout
	lateinit var titleBar: TitleBar

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		rootView = this.createLinearVertical()
		rootView.backColorWhite()
		setContentView(rootView)
		titleBar = TitleBar(this)
		rootView.addView(titleBar, LParam.WidthFill.height(TitleBar.HEIGHT))

		onCreateContent(rootView)
		titleBar.commit()
	}


	abstract fun onCreateContent(contentView: LinearLayout)
}