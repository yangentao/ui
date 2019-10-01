package dev.entao.kan.material

import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import dev.entao.kan.base.ColorX
import dev.entao.kan.ext.backColor
import dev.entao.kan.ext.marginBottom
import dev.entao.kan.page.TitlePage

fun Snackbar.config() {
    this.view.backColor(ColorX.theme)
    val p = this.view.layoutParams as ViewGroup.MarginLayoutParams
    p.marginBottom(52)
}


fun TitlePage.snackShow(text: String) {
    val n = Snackbar.make(this.contentView, text, Snackbar.LENGTH_LONG)
    n.config()
    n.show()
}

fun TitlePage.snackShow(text: String, actionText: String, block: () -> Unit) {
    val n = Snackbar.make(this.contentView, text, Snackbar.LENGTH_INDEFINITE)
    n.config()
    n.setAction(actionText) {
        block()
    }
    n.show()
}