@file:Suppress("unused")

package dev.entao.kan.creator

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act
import dev.entao.kan.ext.genId
import dev.entao.kan.ext.gravityLeftCenter
/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

//RadioButton
fun  ViewGroup.radio(param:ViewGroup.LayoutParams, block: RadioButton.() -> Unit): RadioButton {
	val v = this.createRadioButton()
	this.addView(v, param)
	v.block()
	return v
}

fun  ViewGroup.radio(index: Int, param:ViewGroup.LayoutParams, block: RadioButton.() -> Unit): RadioButton {
	val v = this.createRadioButton()
	this.addView(v, index, param)
	v.block()
	return v
}

fun  ViewGroup.radioBefore(ankor: View, param:ViewGroup.LayoutParams, block: RadioButton.() -> Unit): RadioButton {
	val n = this.indexOfChild(ankor)
	return this.radio(n, param, block)
}

fun View.createRadioButton(): RadioButton {
	return this.context.createRadioButton()
}

fun Fragment.createRadioButton(): RadioButton {
	return this.act.createRadioButton()
}

fun Context.createRadioButton(): RadioButton {
	return RadioButton(this).genId().gravityLeftCenter()
}


//RadioGroup
fun  ViewGroup.radioGroup(param:ViewGroup.LayoutParams, block: RadioGroup.() -> Unit): RadioGroup {
	val v = this.createRadioGroup()
	this.addView(v, param)
	v.block()
	return v
}

fun  ViewGroup.radioGroup(index: Int, param:ViewGroup.LayoutParams, block: RadioGroup.() -> Unit): RadioGroup {
	val v = this.createRadioGroup()
	this.addView(v, index, param)
	v.block()
	return v
}

fun  ViewGroup.radioGroupBefore(ankor: View, param:ViewGroup.LayoutParams, block: RadioGroup.() -> Unit): RadioGroup {
	return this.radioGroup(this.indexOfChild(ankor), param, block)
}

fun View.createRadioGroup(): RadioGroup {
	return this.context.createRadioGroup()
}

fun Fragment.createRadioGroup(): RadioGroup {
	return this.act.createRadioGroup()
}

fun Context.createRadioGroup(): RadioGroup {
	return RadioGroup(this).genId()
}