package dev.entao.kan.page

import android.content.Context
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.kan.appbase.Task
import dev.entao.kan.base.ColorX
import dev.entao.kan.creator.textView
import dev.entao.kan.ext.*
import java.util.*

/**
 * Created by yet on 2015/10/20.
 */
class Snack(context: Context) : LinearLayout(context) {
	val textView: TextView
	private val cancelButton: TextView
	private var okButton: TextView
	private val gd: GestureDetector

	private val items = LinkedList<SnackOpt>()


	init {
		horizontal()
		padding(10, 5, 10, 5)
		backColor(Color.DKGRAY)
		gravityCenterVertical()
		textView = textView(LParam.WidthFlex.HeightWrap.GravityCenterVertical) {
			textColorWhite()
			padding(10)
		}
		cancelButton = textView(LParam.HeightButton.WidthWrap.GravityCenterVertical) {
			gone()
			padding(15, 0, 15, 0)
			textSizeA()
			textColorWhite()
			backColor(Color.TRANSPARENT, ColorX.fade)
			gravityCenter()
			text("取消")
			onClick {
				hide()
			}
		}
		okButton = textView(LParam.HeightButton.WidthWrap.GravityCenterVertical) {
			gone()
			padding(15, 0, 15, 0)
			textSizeA()
			textColorWhite()
			backColor(Color.TRANSPARENT, ColorX.fade)
			gravityCenter()

		}


		gd = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
			override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
				Task.fore {
					hide()
				}
				return true
			}
		})
		this.isLongClickable = true
		setOnTouchListener { _, event ->
			gd.onTouchEvent(event)
		}

	}


	fun hide() {
		cancelButton.gone()
		okButton.gone()
		gone()
		checkList()
	}

	fun show(block: SnackOpt.() -> Unit) {
		val op = SnackOpt()
		op.block()
		Task.mainThread {
			items.add(op)
			checkList()
		}
	}

	private fun checkList() {
		if(isVisiable()) {
			return
		}
		Task.mainThread {
			val a: SnackOpt? = items.pollFirst()
			if (a != null) {
				showOpt(a)
			}
		}
	}

	private fun showOpt(opt: SnackOpt) {
		val flag = System.currentTimeMillis().toString()
		this.textView.tag = flag

		this.textView.text = opt.msg
		if (opt.hasCancel) {
			this.cancelButton.visiable()
		}
		if (opt.ok.isNotEmpty()) {
			this.okButton.text = opt.ok
			this.okButton.visiable()
			this.okButton.onClick {
				opt.onOK()
			}
		}
		if (!opt.hasCancel && opt.ok.isEmpty()) {
			val sec = 4 + opt.msg.length / 2
			Task.foreDelay(sec * 1000L) {
				if (textView.tag == flag) {
					hide()
				}
			}
		}
		visiable()
	}

	class SnackOpt {
		var msg = ""
		var hasCancel = false
		var ok: String = ""
		var onOK: () -> Unit = {}

		fun ok(label: String = "确定", block: () -> Unit) {
			ok = label
			onOK = block
		}
	}


}