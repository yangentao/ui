package dev.entao.kan.list

import android.content.Context
import android.view.View
import android.widget.AbsListView
import android.widget.LinearLayout
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.ext.*
import dev.entao.kan.list.itemviews.TextItemView
import kotlin.reflect.KClass

class GroupItem<T : Any> {
    var label: String = ""
    var items: ArrayList<T> = ArrayList()
}

class GroupData<T : Any> {
    var groupList: ArrayList<GroupItem<T>> = ArrayList()

    var labelOf: (T) -> String = { throw IllegalAccessException("重写此方法") }
    var itemsSorter: (ArrayList<T>) -> Unit = { throw IllegalAccessException("重写此方法") }

    var groupSorter: (ArrayList<GroupItem<T>>) -> Unit = { ls ->
        ls.sortBy { it.label }
    }


    val labelList: List<String>
        get() {
            return this.groupList.map { it.label }
        }


    var flatList: ArrayList<Any> = ArrayList()

    fun process(items: List<T>) {
        val gl = ArrayList<GroupItem<T>>()
        val map = HashMap<String, GroupItem<T>>()
        for (item in items) {
            val lb = labelOf(item)
            val g = map[lb]
            val gg = if (g == null) {
                val a = GroupItem<T>()
                map[lb] = a
                gl.add(a)
                a
            } else {
                g
            }
            gg.label = lb
            gg.items.add(item)
        }

        val ls = ArrayList<Any>(items.size + map.size)
        for (a in gl) {
            ls.add(a)
            ls.addAll(a.items)
        }
        for (g in gl) {
            this.itemsSorter(g.items)
        }
        this.groupSorter(gl)
        this.flatList = ls
        this.groupList = gl
    }
}

abstract class GroupPage<T : Any>(val itemClass: KClass<T>) : ListPage() {
    private val groupData: GroupData<T> = GroupData()
    private lateinit var groupIndexBar: GroupIndexBar

    init {
        this.itemTypes = listOf(GroupItem::class, itemClass)
        groupData.labelOf = {
            this.labelOfItem(it)
        }
        groupData.itemsSorter = { ls ->
            this.onSortItems(ls)

        }
        groupData.groupSorter = {
            this.onSortGroups(it)
        }
    }

    abstract fun labelOfItem(item: T): String
    abstract fun onSortItems(ls: ArrayList<T>)
    open fun onSortGroups(gs: ArrayList<GroupItem<T>>) {
        gs.sortBy { it.label }
    }


    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        groupIndexBar = GroupIndexBar(context)
        this.listViewParent.addView(
            this.groupIndexBar,
            RParam.width(GroupIndexBar.WIDTH_PREFER).parentRight().parentTop().parentBottom()
        )
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

            }

            @Suppress("UNCHECKED_CAST")
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (visibleItemCount > 0) {
                    val obj = getItem(firstVisibleItem)
                    if (obj::class == itemClass) {
                        val lb = labelOfItem(obj as T)
                        groupIndexBar.setCurrentLabel(lb)
                    } else if (obj is GroupItem<*>) {
                        groupIndexBar.setCurrentLabel(obj.label)
                    }
                }
            }
        })

        this.groupIndexBar.onLabelChanged = this::labelChanged
    }

    private fun labelChanged(lb: String) {
        var n = this.listView.headerViewsCount
        for (g in this.groupData.groupList) {
            if (g.label == lb) {
                this.listView.setSelection(n)
                return
            }
            n += 1 + g.items.size
        }
    }

    override fun onItemsRefreshed() {
        super.onItemsRefreshed()
        if (anyAdapter.inFilter) {
            this.groupIndexBar.gone()
        } else {
            this.groupIndexBar.visiable()
            this.groupIndexBar.setLabelItems(this.groupData.labelList)
        }
    }

    abstract fun onNewItemView(context: Context, position: Int): View
    abstract fun onBindItemView(itemView: View, item: T)

    override fun onNewView(context: Context, position: Int): View {
        val item = getItem(position)
        return if (item::class == GroupItem::class) {
            val v = TextItemView(context)
            v.padding(10, 0, 0, 0)
            v.backColor(Colors.GRAY)
            v.textColorWhite()
            v.textSizeB()
            v
        } else {
            onNewItemView(context, position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindItem(itemView: View, item: Any) {
        if (item is GroupItem<*>) {
            val v = itemView as TextItemView
            v.text = item.label
        } else {
            onBindItemView(itemView, item as T)
        }
    }

    abstract fun onRequestItemModels(): List<T>

    override fun onRequestItems(): List<Any> {
        val ls = onRequestItemModels()
        groupData.process(ls)
        return groupData.flatList
    }
}