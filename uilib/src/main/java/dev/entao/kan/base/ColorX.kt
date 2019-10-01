@file:Suppress("unused")

package dev.entao.kan.base

import android.graphics.Color
import dev.entao.kan.appbase.App
import dev.entao.kan.appbase.ex.argb
import dev.entao.kan.appbase.ex.rgb

object ColorX {
    val theme: Int get() = App.themeColor

    var red: Int = 0xAD1919.rgb
    var redDark: Int = 0x981414.rgb
    var redLight: Int = 0xD80909.rgb
    var green: Int = 0x2BA62E.rgb
    var greenDark: Int = 0x167724.rgb
    var greenLight: Int = 0x24CD1B.rgb
    var blue: Int = 0x3188BA.rgb
    var blueDark: Int = 0x0B5684.rgb
    var blueLight: Int = 0x11A0E0.rgb
    var cyan: Int = 0x41A993.rgb
    var cyanDark: Int = 0x0F8169.rgb
    var cyanLight: Int = 0x23D1AC.rgb


    var actionBlue: Int = 0x3F52B5L.rgb
    var actionRed: Int = 0xF31717L.rgb
    var actionGreen: Int = 0x45A859L.rgb
    var fade: Int = 0xFFFF8800.argb

    var textDisabled: Int = 0x61000000L.argb
    var backDisabled: Int = 0x1F000000L.argb

    var textPrimary: Int = 0xFF333333.argb
    var textSecondary: Int = 0xFF888888.argb

    var lineGray: Int = Color.LTGRAY
    var borderGray: Int = Color.LTGRAY
    var backGray: Int = 0xFFDDDDDD.argb

    val TRANS = Color.TRANSPARENT

    var EditFocus = 0xFF38C4B0.argb

    var Progress :Int = this.greenLight

    var Unselected = 0xFFAAAAAA.argb

    //0xff8800 --> "#ff8800"
    fun toStringColor(color: Int): String {
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        if (a == 0xff) {
            return "#" + Hex.encodeByte(r) + Hex.encodeByte(g) + Hex.encodeByte(b)
        }
        return "#" + Hex.encodeByte(a) + Hex.encodeByte(r) + Hex.encodeByte(g) + Hex.encodeByte(b)
    }

}