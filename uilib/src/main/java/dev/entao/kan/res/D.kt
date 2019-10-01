@file:Suppress("unused")

package dev.entao.kan.res

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.DrawableRes
import dev.entao.kan.appbase.ex.*
import dev.entao.kan.base.ColorX
import dev.entao.kan.theme.IconSize
import dev.entao.kan.theme.ViewSize
import dev.entao.kan.ui.R
import dev.entao.kan.widget.EditTextX

/**
 * Created by entaoyang@163.com on 2016-07-23.
 */

object D {
    val CheckBox: Drawable get() = checked(R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
    val EditClear: Drawable get() = sized(R.drawable.yet_edit_clear, EditTextX.IMAGE_WIDTH)
    val Back: Drawable get() = res(R.drawable.yet_back).sized(IconSize.Normal)
    val ArrowRight: Drawable get() = res(R.drawable.yet_arrow_right).sized(IconSize.Tiny)
    val RedPoint: Drawable
        get() = ShapeOval().fill(RGB(255, 128, 0)).size(10).value
    val Input: Drawable
        get() {
            val corner: Int = ViewSize.EditCorner
            val normal = ShapeRect(Color.WHITE, corner).stroke(1, ColorX.borderGray).value
            val focused = ShapeRect(Color.WHITE, corner).stroke(1, ColorX.EditFocus).value
            return focused(normal, focused)
        }
    val InputSearch: Drawable
        get() {
            val corner: Int = ViewSize.EditHeightSearch / 2
            val normal = ShapeRect(Color.WHITE, corner).stroke(1, ColorX.borderGray).value
            val focused = ShapeRect(Color.WHITE, corner).stroke(1, ColorX.EditFocus).value
            return focused(normal, focused)
        }

    val InputRect: Drawable
        get() {
            val normal = ShapeRect(Color.WHITE, 2).stroke(1, ColorX.borderGray).value
            val focused = ShapeRect(Color.WHITE, 2).stroke(1, ColorX.EditFocus).value
            return focused(normal, focused)
        }

    fun buttonGray(corner: Int = ViewSize.ButtonCorner): Drawable {
        return buttonColor(ColorX.backDisabled, corner)
    }

    fun buttonGreen(corner: Int = ViewSize.ButtonCorner): Drawable {
        return buttonColor(ColorX.actionGreen, corner)
    }

    fun buttonRed(corner: Int = ViewSize.ButtonCorner): Drawable {
        return buttonColor(ColorX.red, corner)
    }

    fun buttonWhite(corner: Int = ViewSize.ButtonCorner): Drawable {
        return buttonColor(Color.rgb(230, 230, 230), corner)
    }

    fun buttonColor(color: Int, corner: Int = ViewSize.ButtonCorner): Drawable {
        val normal = ShapeRect(color, corner).value
        val pressed = ShapeRect(ColorX.fade, corner).value
        val enableFalse = ShapeRect(ColorX.backDisabled, corner).value
        return StateList.drawable(normal, VState.Pressed to pressed, VState.Disabled to enableFalse)
    }

    fun panelBorder(color: Int = ColorX.borderGray, corner: Int = ViewSize.ButtonCorner): Drawable {
        return ShapeRect(Color.WHITE, corner).stroke(1, color).value
    }

    fun res(@DrawableRes resId: Int): Drawable {
        return Res.drawable(resId)
    }

    fun sized(@DrawableRes resId: Int, size: Int): Drawable {
        return res(resId).sized(size, size)
    }

    fun limited(@DrawableRes resId: Int, edge: Int): Drawable {
        return res(resId).limited(edge)
    }

    fun tinted(@DrawableRes res: Int, color: Int): Drawable {
        return res(res).tinted(color)
    }

    fun tintTheme(@DrawableRes res: Int): Drawable {
        return tinted(res, ColorX.theme)
    }

    fun tintLight(@DrawableRes res: Int, normalColor: Int, lightColor: Int): StateListDrawable {
        return Bmp.tintLight(res, normalColor, lightColor)
    }

    fun color(color: Int): ColorDrawable {
        return ColorDrawable(color)
    }

    fun listColor(normal: Int, pressed: Int): ColorStateList {
        return StateList.lightColor(normal, pressed)
    }

    fun light(normal: Drawable, pressed: Drawable): StateListDrawable {
        return StateList.lightDrawable(normal, pressed)
    }

    fun light(@DrawableRes normal: Int, @DrawableRes light: Int): StateListDrawable {
        return light(normal.drawableRes, light.drawableRes)
    }

    fun lightColor(normalColor: Int, pressedColor: Int): StateListDrawable {
        return StateList.lightColorDrawable(normalColor, pressedColor)
    }

    fun checked(@DrawableRes normalId: Int, @DrawableRes checkedId: Int): StateListDrawable {
        return StateList.drawable(normalId, VState.Checked to checkedId)
    }

    fun focused(@DrawableRes normalId: Int, @DrawableRes focusId: Int): StateListDrawable {
        return StateList.drawable(normalId, VState.Focused to focusId)
    }

    fun focused(normal: Drawable, focusedImage: Drawable): StateListDrawable {
        return StateList.drawable(normal, VState.Focused to focusedImage)
    }

    fun layerOval(resId: Int, tintColor: Int, ovalColor: Int, inset: Int): LayerDrawable {
        return layerOval(resId.drawableRes.tinted(tintColor), ovalColor, inset)
    }

    fun layerOval(resId: Int, ovalColor: Int, inset: Int): LayerDrawable {
        return layerOval(resId.drawableRes, ovalColor, inset)
    }

    fun layerOval(drawable: Drawable, ovalColor: Int, inset: Int): LayerDrawable {
        val bg = ShapeOval().fill(ovalColor).value
        val ld = LayerDrawable(arrayOf(bg, drawable))
        val n = dp(inset)
        ld.setLayerInset(1, n, n, n, n)
        return ld
    }
}