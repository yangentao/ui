package dev.entao.kan.grid

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.entao.kan.appbase.Task
import dev.entao.kan.creator.relative
import dev.entao.kan.creator.swipeRefresh
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.*
import dev.entao.kan.list.AnyAdapter
import dev.entao.kan.page.TitlePage

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */

abstract class GridPage : TitlePage() {
    var anyAdapter: AnyAdapter = AnyAdapter()
    lateinit var gridView: LineGridView
    lateinit var gridParent: RelativeLayout
        private set
    lateinit var refreshLayout: SwipeRefreshLayout
        private set
    lateinit var emptyView: TextView
        private set

    var emptyText = "没有内容"
    var loadingText = "加载中..."

    override fun onDestroyView() {
        super.onDestroyView()
    }

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
        anyAdapter.onItemsRefreshed = this::onItemsRefreshed

        gridView.adapter = anyAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            onItemClick(anyAdapter.getItem(pos))
        }
        gridParent = contentView.relative(LParam.WidthFill.HeightFlex) {
            refreshLayout = swipeRefresh(RParam.Fill) {
                isEnabled = false
                setOnRefreshListener {
                    onPullRefresh()
                }
                setColorSchemeResources(
                    android.R.color.holo_green_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_dark,
                    android.R.color.holo_red_dark
                )
                addView(gridView, MParam.Fill)
            }
            emptyView = textView(RParam.Fill) {
                textS = emptyText
                gravityCenter().textSizeB().gone()
            }
            gridView.emptyView = emptyView
        }
    }

    protected open fun onItemsRefreshed() {
        setRefreshing(false)
    }

    fun enablePullRefresh(enable: Boolean = true) {
        refreshLayout.isEnabled = enable
    }

    fun setRefreshing(refresh: Boolean) {
        Task.mainThread {
            refreshLayout.isRefreshing = refresh
            if (refresh) {
                emptyView.text = loadingText
            } else {
                emptyView.text = emptyText
            }
        }
    }

    open fun onPullRefresh() {
        requestItems()
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
        if (refreshLayout.isEnabled) {
            setRefreshing(true)
        }
    }

    open fun onRequestItems(): List<Any> {
        return emptyList()
    }
}