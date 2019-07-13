package dev.entao.kan.widget

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent
import dev.entao.kan.ext.genId
/**
 * Created by entaoyang@163.com on 2018-07-30.
 */

class MyViewPager(context: Context) : ViewPager(context) {

	var onUserTouch: () -> Unit = {}

	init {
		genId()
	}

	override fun onTouchEvent(ev: MotionEvent): Boolean {
		val a = ev.actionMasked
		if(a in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
			onUserTouch()
		}
		return super.onTouchEvent(ev)
	}

}