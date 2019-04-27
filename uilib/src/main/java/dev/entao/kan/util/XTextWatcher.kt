package dev.entao.kan.util

import android.text.Editable

/**
 * Created by yet on 2015/10/15.
 */
abstract class XTextWatcher : android.text.TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        afterTextChanged(s.toString())
    }

    abstract fun afterTextChanged(text: String)
}
