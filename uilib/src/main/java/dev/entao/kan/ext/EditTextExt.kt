package dev.entao.kan.ext

import android.widget.EditText
import dev.entao.kan.theme.Str
import dev.entao.kan.res.D

/**
 * Created by entaoyang@163.com on 2017-01-06.
 */

fun <T : EditText> T.styleSearch(): T {
	this.textSizeB().backDrawable(D.InputSearch)
	this.padding(15, 2, 15, 2)
	this.hint(Str.SEARCH)
	return this
}