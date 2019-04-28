@file:Suppress("unused")

package dev.entao.kan.list

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ListView

class SwipeHandlerX(val listView: ListView) : OnTouchListener {
    private var enable = true
    private val gd: GestureDetector
    private var curView: XSwipeItemView? = null

    init {
        gd = GestureDetector(listView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                return onScrollItem(e1, e2, distanceX)
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return onFlingItem(e1, e2)
            }

            override fun onDown(e: MotionEvent): Boolean {
                val view = findSwipeItemView(e)
                if (view !== curView) {
                    resetCurrent()
                }
                return false
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                resetCurrent()
                return false
            }
        })
        listView.setOnTouchListener(this)
        listView.isLongClickable = true
    }

    fun resetCurrent() {
        if (null != curView) {
            moveToX(curView, 0)
            curView = null
        }
    }

    private fun moveToX(view: View?, endX: Int) {
        if (view == null) {
            return
        }
        val n = view.scrollX
        if (n != endX) {
            val msg = MoveHandler(view, endX).obtainMessage()
            msg.sendToTarget()
        }
    }

    private fun findSwipeItemView(e: MotionEvent): XSwipeItemView? {
        return findSwipeItemView(e.x, e.y)
    }

    private fun findSwipeItemView(x: Float, y: Float): XSwipeItemView? {
        val selectPos = listView.pointToPosition(x.toInt(), y.toInt())
        if (selectPos >= 0) {
            val view = listView.getChildAt(selectPos - listView.firstVisiblePosition)
            if (view is XSwipeItemView) {
                return view
            }
        }
        return null
    }

    private fun onFlingItem(e1: MotionEvent, e2: MotionEvent): Boolean {
        if (curView != null) {
            val itemView = curView
            val dx = e2.x - e1.x
            val scrollX = itemView!!.scrollX
            if (scrollX > 0) {//<--
                if (dx < 0) {//<--
                    val rightWidth = itemView.rightViewWidth
                    if (scrollX > rightWidth / 4) {
                        moveToX(itemView, rightWidth)
                    } else {
                        moveToX(itemView, 0)
                    }
                } else {//-->
                    moveToX(itemView, 0)
                }
            } else {//-->
                if (dx > 0) {//-->
                    val leftWidth = itemView.leftViewWidth
                    if (-scrollX > leftWidth / 4) {
                        moveToX(itemView, -leftWidth)
                    } else {
                        moveToX(itemView, 0)
                    }
                } else {//<--
                    moveToX(itemView, 0)
                }

            }
        }
        return false
    }

    private fun onScrollItem(e1: MotionEvent, e2: MotionEvent, distanceX: Float): Boolean {
        val dx = e2.x - e1.x
        val dy = e2.y - e1.y
        val horScroll = Math.abs(dx) > Math.abs(dy * 2)
        if (horScroll) {
            val cancelEvent = MotionEvent.obtain(e2)
            cancelEvent.action = MotionEvent.ACTION_CANCEL
            listView!!.onTouchEvent(cancelEvent)
            val itemView = findSwipeItemView(e1)
            if (itemView != null) {
                if (itemView !== curView) {
                    moveToX(curView, 0)
                    curView = itemView
                }
                val newScrollX = distanceX + itemView.scrollX
                if (newScrollX > 0) {//<--
                    val rightWidth = itemView.rightViewWidth
                    if (newScrollX > rightWidth) {
                        itemView.scrollTo(rightWidth, 0)
                    } else {
                        itemView.scrollBy(distanceX.toInt(), 0)
                    }
                } else {//-->
                    val leftWidth = itemView.leftViewWidth
                    if (-newScrollX > leftWidth) {
                        itemView.scrollTo(-leftWidth, 0)
                    } else {
                        itemView.scrollBy(distanceX.toInt(), 0)
                    }
                }
                return true
            }
        }
        return false
    }

    fun enable(enable: Boolean) {
        this.enable = enable
    }

    fun enable(): Boolean {
        return this.enable
    }

    override fun onTouch(v: View, ev: MotionEvent): Boolean {
        return gd.onTouchEvent(ev)
    }

}
