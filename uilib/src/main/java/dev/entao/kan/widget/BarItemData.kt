package dev.entao.kan.widget

import android.graphics.drawable.Drawable
import dev.entao.kan.base.BlockUnit

class BarItemData {
    var label: String = ""
    var drawable: Drawable? = null
    var drawResId: Int = 0
    var onAction: BlockUnit = {}
}

