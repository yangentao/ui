package dev.entao.kan.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.ext.genId
import dev.entao.kan.ext.hideInputMethod
import dev.entao.kan.ext.padding
import dev.entao.kan.res.D

class EditTextX(context: Context) : EditText(context) {
	private var x: Drawable = D.EditClear

	init {
		genId()
		padding(5, 2, 5, 2)
		this.setOnTouchListener(View.OnTouchListener { _, event ->
			if (this@EditTextX.compoundDrawables[2] == null) {
				return@OnTouchListener false
			}
			if (event.action != MotionEvent.ACTION_UP) {
				return@OnTouchListener false
			}
			if (event.x > this@EditTextX.width - this@EditTextX.paddingRight
					- IMAGE_WIDTH - dp(15)) {
				this@EditTextX.setText("")
				this@EditTextX.setCompoundDrawables(null, null, null, null)
				Task.fore {
					this@EditTextX.hideInputMethod()
				}
			}
			false
		})
		this.addTextChangedListener(object : TextWatcher {

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				this@EditTextX.setCompoundDrawables(null, null, if (this@EditTextX.text.toString() == "")
					null
				else
					x, null)
			}

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

			}

			override fun afterTextChanged(s: Editable) {

			}
		})
	}

	override fun performClick(): Boolean {
		return super.performClick()
	}

	companion object {
		val IMAGE_WIDTH = 25
	}
}
