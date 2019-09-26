@file:Suppress("unused")

package dev.entao.kan.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.ShapeRect
import dev.entao.kan.appbase.ex.colorParse
import dev.entao.kan.base.BlockUnit
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.*
import java.util.*

/**
 * Created by yet on 2015/10/29.
 */
@SuppressLint("ViewConstructor")
class ArrayListIndexBar(context: Context, feedbackParentView: RelativeLayout) : LinearLayout(context) {

    private var selectView: View? = null
    private val selectDrawable = bgDraw()
    private val darkColor = colorParse("#ccc")
    private val normalColor = Color.TRANSPARENT
    private var feedbackView: TextView

    private var tagList: ArrayList<Char>? = null
    private var tagPosMap = HashMap<Char, Int>(30)

    private val hideFeedbackRun: BlockUnit

    var onIndexChanged: (Int) -> Unit = {}
    var onIndexBarVisiblityChanged: (Int) -> Unit = { }

    private val touchListener = View.OnTouchListener { _, event ->
        val action = event.actionMasked
        val y = event.y.toInt()
        if (action == MotionEvent.ACTION_DOWN) {
            setBackgroundColor(darkColor)
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
            || action == MotionEvent.ACTION_OUTSIDE
        ) {
            setBackgroundColor(normalColor)
            selectByY(y)
        } else if (action == MotionEvent.ACTION_MOVE) {
            selectByY(y)
        }
        false
    }

    init {
        orientationVertical().gravityCenterHorizontal().padding(0, 0, 0, 0).makeClickable()
        feedbackView = feedbackParentView.textView(relativeParam().centerInParent().size(70)) {
            val a = ShapeRect(colorParse("#555"), 10).stroke(2, colorParse("#ddd")).value
            textColor(Color.WHITE).textSizeSp(50).gravityCenter().backDrawable(a).gone()
        }
        hideFeedbackRun = { feedbackView.gone() }
        this.setOnTouchListener(touchListener)
    }


    private fun selectByY(y: Int) {
        for (i in 0..childCount - 1) {
            val itemView = getChildAt(i)
            if (y >= itemView.top && y <= itemView.bottom) {
                if (selectView !== itemView) {
                    val tv = itemView as TextView
                    val tag = tv.text.toString()[0]
                    select(tag, true)
                    Task.fore {
                        onIndexChanged(tagPosMap[tag]!!)
                    }
                }
                break
            }
        }
    }

    fun select(tag: Char) {
        Task.fore {
            select(tag, false)
        }
    }

    private fun select(tag: Char, feedback: Boolean) {
        val tagIndex = tagList!!.indexOf(tag)
        if (tagIndex >= 0) {
            if (selectView != null) {
                selectView!!.setBackgroundColor(Color.TRANSPARENT)
            }
            selectView = getChildAt(tagIndex)
            selectView!!.background = selectDrawable

            val str = (selectView as TextView).text.toString()
            feedbackView.text(str)
            if (feedback) {
                feedbackView.visiable()
                Task.foreDelay(650, hideFeedbackRun)
            }
        }
    }

    fun buildViews(tagList: ArrayList<Char>, tagPosMap: HashMap<Char, Int>) {
        this.tagList = tagList
        this.tagPosMap = tagPosMap
        removeAllViews()
        for (s in tagList) {
            textView(lParam().width(40).height(0).weight(1).gravityCenter()) {
                this.tag = s
                text(s.toString()).textSizeD().textColor(Color.BLACK).gravityCenter()
            }
        }
    }


    private fun bgDraw(): Drawable {
        val gd = GradientDrawable()
        gd.setColor(Color.GRAY)
        gd.setStroke(2, Color.WHITE)
        gd.cornerRadius = 5f

        val lu = dev.entao.kan.util.LayerBuilder()
        lu.add(gd) {
            insetX(6)
        }
        lu.add(ColorDrawable(Color.TRANSPARENT))
        return lu.value
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        onIndexBarVisiblityChanged(visibility)
    }

    fun postHide() {
        Task.fore { visibility = View.GONE }
    }

    fun postShow() {
        Task.fore { visibility = View.VISIBLE }
    }
}
