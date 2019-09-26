package dev.entao.kan.widget

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.MyTimer
import dev.entao.kan.appbase.ex.ShapeOval
import dev.entao.kan.appbase.ex.grayColor
import dev.entao.kan.appbase.ex.sized
import dev.entao.kan.creator.createImageView
import dev.entao.kan.creator.imageView
import dev.entao.kan.creator.linearHor
import dev.entao.kan.ext.*
import dev.entao.kan.res.D

class IndicatorPager(context: Context) : RelativeLayout(context) {
    val pointSize: Int = 8
    val adapter = ViewPagerAdapter()
    val viewPager = MyViewPager(context)
    val indicatorLayout: LinearLayout
    var imageScaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_XY

    var onNewView: (Context, Int) -> View = { c, _ ->
        val v = c.createImageView()
        v.scaleType = imageScaleType
        v
    }
    var onDestroyItem: (View, Int) -> Unit = { _, _ -> }

    var onPageClick: (View, Int) -> Unit = { _, _ -> }

    var dotColor = Color.argb(180, 150, 150, 150)
    var dotLightColor = grayColor(255)

    var timer: MyTimer = MyTimer()


    init {
        genId()
        addView(viewPager, RParam.WidthFill.HeightWrap)
        indicatorLayout = linearHor(RParam.Wrap.CenterHorizontal.ParentBottom) {
            padding(10)
        }


        adapter.onNewView = { c, n ->
            this@IndicatorPager.onNewView(c, n)
        }
        adapter.onPageClick = { v, n ->
            this@IndicatorPager.onPageClick(v, n)
        }
        adapter.onDestroyItem = { v, n ->
            this@IndicatorPager.onDestroyItem(v, n)
        }

        adapter.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                bind()
            }

            override fun onInvalidated() {

            }
        })
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                selectIndicator(position)
            }
        })
        viewPager.adapter = adapter

        viewPager.onUserTouch = {
            this@IndicatorPager.onUserTouch()
        }

    }


    fun onUserTouch() {
        timer.paused = true
    }

    private fun bind() {
        indicatorLayout.removeAllViews()
        for (i in 0 until adapter.count) {
            indicatorLayout.imageView(LParam.size(pointSize).margins(pointSize / 2, 0, pointSize / 2, 0)) {
                val d1 = ShapeOval().fill(dotColor).value
                val d2 = ShapeOval().fill(dotLightColor).value
                val d = D.light(d1, d2).sized(pointSize)
                setImageDrawable(d)
                this.scaleCenterInside()

            }
        }
        if (adapter.count > 0) {
            selectIndicator(getCurrentPage())
        }
    }

    fun hideIndicatorView() {
        indicatorLayout.gone()
    }

    fun setCurrentPage(n: Int, smooth: Boolean) {
        viewPager.setCurrentItem(n, smooth)
    }

    fun getCurrentPage(): Int {
        return viewPager.currentItem
    }

    fun setDrawableItems(ls: List<Drawable>) {
        setImageItems(ls) { v, item ->
            v.setImageDrawable(item as Drawable)
        }
    }

    fun setImageResItems(ls: List<Int>) {
        setImageItems(ls) { v, item ->
            v.setImageResource(item as Int)
        }
    }


    fun setImageItems(ls: List<Any>, block: (ImageView, Any) -> Unit) {
        onNewView = { c, p ->
            val v = c.createImageView()
            v.scaleType = imageScaleType
            block(v, getItem(p))
            v
        }
        setItems(ls)
    }

    fun setViewItems(ls: List<View>) {
        onNewView = { _, p ->
            getItem(p) as View
        }
        setItems(ls)
    }

    fun setItems(ls: List<Any>) {
        adapter.setItems(ls)
    }

    fun getCount(): Int {
        return adapter.count
    }

    fun getItem(position: Int): Any {
        return adapter.getItem(position)
    }

    private fun selectIndicator(p: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            indicatorLayout.getChildAt(i).isSelected = i == p
        }
    }

    private fun timerCallback() {
        Task.fore {
            val c = adapter.count
            val n = viewPager.currentItem
            viewPager.setCurrentItem((n + 1) % c, true)
        }
    }

    fun startFlip(period: Long) {
        timer.callback = {
            timerCallback()
        }
        timer.repeat(period)
    }

    fun stopFlip() {
        timer.cancel()
    }
}

