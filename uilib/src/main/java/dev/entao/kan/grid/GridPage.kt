package dev.entao.kan.grid

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import dev.entao.kan.ext.HeightFlex
import dev.entao.kan.ext.LParam
import dev.entao.kan.ext.WidthFill
import dev.entao.kan.list.AnyAdapter
import dev.entao.kan.page.TitlePage

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */

abstract class GridPage : TitlePage() {
    lateinit var gridView: LineGridView
    var anyAdapter: AnyAdapter = AnyAdapter()

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        gridView = LineGridView(context)
        gridView.numColumns = 2
        gridView.horizontalSpacing = 0
        gridView.verticalSpacing = 0
        anyAdapter.onBindView = { v, p ->
            onBindItemView(v, anyAdapter.getItem(p))
        }
        anyAdapter.onNewView = this::onNewItemView
        anyAdapter.onRequestItems = this::onRequestItems

        gridView.adapter = anyAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            onItemClick(anyAdapter.getItem(pos))
        }
        onLayoutGridView(gridView)
    }

    open fun onLayoutGridView(gridView: LineGridView) {
        contentView.addView(gridView, LParam.WidthFill.HeightFlex)
    }

    open fun setItems(items: List<Any>) {
        anyAdapter.setItems(items)
    }


    open fun onItemClick(item: Any) {

    }

    abstract fun onNewItemView(context: Context, position: Int): View

    abstract fun onBindItemView(view: View, item: Any)

    fun requestItems() {
        anyAdapter.requestItems()
    }

    open fun onRequestItems(): List<Any> {
        return emptyList()
    }
}