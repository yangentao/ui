@file:Suppress("unused")

package dev.entao.kan.ext

import android.widget.Button
import android.widget.CheckBox

/**
 * Created by entaoyang@163.com on 16/4/28.
 */

fun <T : Button> T.styleGreen(): T {
    this.style {
        fillGreen()
    }
    return this
}

fun <T : Button> T.styleGreenRound(): T {
    this.style {
        fillGreen()
        cornersRound()
    }
    return this
}


fun <T : Button> T.styleRed(): T {
    style {
        fillRed()
    }
    return this
}

fun <T : Button> T.styleRedRound(): T {
    style {
        fillRed()
        cornersRound()
    }
    return this
}

fun <T : Button> T.styleWhite(): T {
    style {
        outlineBlue()
    }
    return this
}


fun <T : CheckBox> T.styleSwitch(): T {
//	val w = 50
//	val h = 30
//	val dd1 = makeRoundEdgeRectDrawable(w, h, C.WHITE, 1, C.lightGrayColor)
//	val dd2 = makeRoundEdgeRectDrawable(w, h, C.safeColor)
////		val dd3 = makeRoundEdgeRectDrawable(w, h, C.lightGrayColor, 1, C.WHITE)
////		cb.checkMarkDrawable = StateImage(dd1).checked(dd2).enabled(dd3, false).value
////		cb.checkMarkDrawable = StateImage(dd1).checked(dd2).value
//	val draw = StateImage(dd1).checked(dd2).value
////		cb.setBackgroundDrawable(StateImage(dd1).checked(dd2).value)
////		cb.checkMarkDrawable = Res.drawable("checkbox", true)
//	cb.buttonDrawable = draw
//	cb.backColorPage()
//	cb.compoundDrawablePadding = 0
//	this.textSizeB().textColorMajor().backDrawable(ShapeDef.whiteButton())
    return this
}
