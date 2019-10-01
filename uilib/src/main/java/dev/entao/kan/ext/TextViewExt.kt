@file:Suppress("unused")

package dev.entao.kan.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.appbase.ex.sized
import dev.entao.kan.base.ColorX
import dev.entao.kan.res.D
import dev.entao.kan.theme.Space
import dev.entao.kan.theme.TextSize
import dev.entao.kan.ui.R
import dev.entao.kan.util.HtmlText
import dev.entao.kan.util.XTextWatcher

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


fun <T : TextView> T.html(block: HtmlText.() -> Unit): T {
    val h = HtmlText()
    h.block()
    this.text = h.spanned()
    return this
}

@Suppress("DEPRECATION")
fun <T : TextView> T.setHtmlString(s: String) {
    if (Build.VERSION.SDK_INT >= 24) {
        this.text = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)
    } else {
        this.text = Html.fromHtml(s)
    }
}


fun TextView.hideInputMethod() {
    if (this.isFocused) {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun <T : TextView> T.imeAction(action: Int, block: (TextView) -> Unit): T {
    this.setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == action) {
                if (v != null) {
                    block(v)
                }
                return true
            }
            return false
        }

    })
    return this
}

fun <T : TextView> T.imeDone(): T {
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    return this
}

fun <T : TextView> T.imeDone(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    this.imeAction(EditorInfo.IME_ACTION_DONE, block)
    return this
}

fun <T : TextView> T.imeGo(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_GO
    this.imeAction(EditorInfo.IME_ACTION_GO, block)
    return this
}

fun <T : TextView> T.imeNext(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_NEXT
    this.imeAction(EditorInfo.IME_ACTION_NEXT, block)
    return this
}

fun <T : TextView> T.imeSearch(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_SEARCH
    this.imeAction(EditorInfo.IME_ACTION_SEARCH, block)
    return this
}

fun <T : TextView> T.imeSend(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_SEND
    this.imeAction(EditorInfo.IME_ACTION_SEND, block)
    return this
}

fun <T : TextView> T.clickable(b: Boolean = true): T {
    this.isClickable = b
    return this
}

fun <T : TextView> T.gravity(n: Int): T {
    this.gravity = n
    return this
}

fun <T : TextView> T.gravityCenterVertical(): T {
    this.gravity = Gravity.CENTER_VERTICAL
    return this
}

fun <T : TextView> T.gravityCenterHorizontal(): T {
    this.gravity = Gravity.CENTER_HORIZONTAL
    return this
}

@SuppressLint("RtlHardcoded")
fun <T : TextView> T.gravityLeftCenter(): T {
    this.gravity = Gravity.LEFT or Gravity.CENTER
    return this
}

@SuppressLint("RtlHardcoded")
fun <T : TextView> T.gravityRightCenter(): T {
    this.gravity = Gravity.RIGHT or Gravity.CENTER
    return this
}

fun <T : TextView> T.gravityCenter(): T {
    this.gravity = Gravity.CENTER
    return this
}

@SuppressLint("RtlHardcoded")
fun <T : TextView> T.gravityTopLeft(): T {
    this.gravity = Gravity.TOP or Gravity.LEFT
    return this
}

fun <T : TextView> T.miniWidthDp(widthDp: Int): T {
    this.minWidth = dp(widthDp)
    return this
}

fun <T : TextView> T.miniHeightDp(heightDp: Int): T {
    this.minHeight = dp(heightDp)
    return this
}

fun <T : TextView> T.inputTypePassword(): T {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    return this
}

fun <T : TextView> T.inputTypePasswordNumber(): T {
    this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    return this
}

fun <T : TextView> T.inputTypePhone(): T {
    this.inputType = InputType.TYPE_CLASS_PHONE
    return this
}

fun <T : TextView> T.inputTypeEmail(): T {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    return this
}

fun <T : TextView> T.inputTypeNumber(): T {
    this.inputType = InputType.TYPE_CLASS_NUMBER
    return this
}

fun <T : TextView> T.inputTypeNumberDecimal(): T {
    this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    return this
}


fun <T : TextView> T.textSizeSp(sp: Int): T {
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.toFloat())
    return this
}


fun <T : TextView> T.textSizeA(): T {
    return this.textSizeBig()
}

fun <T : TextView> T.textSizeB(): T {
    return this.textSizeNormal()
}

fun <T : TextView> T.textSizeC(): T {
    return this.textSizeSmall()
}

fun <T : TextView> T.textSizeD(): T {
    return this.textSizeTiny()
}

fun <T : TextView> T.textSizeLarge(): T {
    return this.textSizeSp(TextSize.Large)
}

fun <T : TextView> T.textSizeTitle(): T {
    return this.textSizeSp(TextSize.Title)
}

fun <T : TextView> T.textSizeBig(): T {
    return this.textSizeSp(TextSize.Big)
}

fun <T : TextView> T.textSizeNormal(): T {
    return this.textSizeSp(TextSize.Normal)
}

fun <T : TextView> T.textSizeSmall(): T {
    return this.textSizeSp(TextSize.Small)
}

fun <T : TextView> T.textSizeTiny(): T {
    return this.textSizeSp(TextSize.Tiny)
}


fun <T : TextView> T.lines(lines: Int): T {
    setLines(lines)
    return this
}

fun <T : TextView> T.maxLines(maxLines: Int): T {
    setMaxLines(maxLines)
    return this
}

fun <T : TextView> T.textColorGreen(): T {
    return this.textColor(ColorX.green)
}

fun <T : TextView> T.textColorRed(): T {
    return this.textColor(ColorX.red)
}

fun <T : TextView> T.textColor(color: Int): T {
    this.setTextColor(color)
    return this
}


fun <T : TextView> T.textColor(color: Int, pressed: Int): T {
    this.setTextColor(StateList.lightColor(color, pressed))
    return this
}

fun <T : TextView> T.textColor(ls: ColorStateList): T {
    setTextColor(ls)
    return this
}

fun <T : TextView> T.textColorWhite(): T {
    this.setTextColor(Color.WHITE)
    return this
}

fun <T : TextView> T.colorWhite(): T {
    this.setTextColor(Color.WHITE)
    return this
}

fun <T : TextView> T.textColorMajor(): T {
    this.setTextColor(ColorX.textPrimary)
    return this
}

fun <T : TextView> T.colorMajor(): T {
    this.setTextColor(ColorX.textPrimary)
    return this
}

fun <T : TextView> T.textColorMinor(): T {
    this.setTextColor(ColorX.textSecondary)
    return this
}

fun <T : TextView> T.colorMinor(): T {
    this.setTextColor(ColorX.textSecondary)
    return this
}

fun <T : TextView> T.textColorMid(): T {
    this.setTextColor(ColorX.textPrimary)
    return this
}

fun <T : TextView> T.textColorSafe(): T {
    this.setTextColor(ColorX.green)
    return this
}

fun <T : TextView> T.colorGreen(): T {
    this.setTextColor(ColorX.green)
    return this
}

fun <T : TextView> T.colorRed(): T {
    this.setTextColor(ColorX.red)
    return this
}

fun <T : TextView> T.textColorMajorFade(): T {
    setTextColor(D.listColor(ColorX.textPrimary, ColorX.fade))
    return this
}

fun <T : TextView> T.singleLine(): T {
    this.setSingleLine(true)
    return this
}

fun <T : TextView> T.multiLine(): T {
    this.setSingleLine(false)
    return this
}

fun <T : TextView> T.ellipsizeStart(): T {
    ellipsize = TextUtils.TruncateAt.START
    return this
}

fun <T : TextView> T.ellipsizeMid(): T {
    ellipsize = TextUtils.TruncateAt.MIDDLE
    return this
}

fun <T : TextView> T.ellipsizeEnd(): T {
    ellipsize = TextUtils.TruncateAt.END
    return this
}

fun <T : TextView> T.ellipsizeMarquee(): T {
    ellipsize = TextUtils.TruncateAt.MARQUEE
    return this
}

fun <T : TextView> T.text(text: String?): T {
    setText(text)
    return this
}

var TextView.textS: String
    get() {
        return this.text.toString()
    }
    set(value) {
        this.text = value
    }

var TextView.textTrim: String
    get() {
        return this.text.toString().trim()
    }
    set(value) {
        this.text = value.trim()
    }


fun <T : TextView> T.textX(text: String?): T {
    setText(text)
    return this
}

fun <T : TextView> T.lineSpace(add: Float, multi: Float): T {
    setLineSpacing(add, multi)
    return this
}

fun <T : TextView> T.hint(text: String): T {
    this.hint = text
    return this
}

fun <T : TextView> T.linkifyAll(): T {
    this.autoLinkMask = Linkify.ALL
    this.movementMethod = LinkMovementMethod.getInstance()
    return this
}

fun <T : TextView> T.leftImage(resId: Int, size: Int, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(D.res(resId).sized(size), old[1], old[2], old[3])
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.leftImage(d: Drawable?, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(d, old[1], old[2], old[3])
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.rightImage(resId: Int, size: Int, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(old[0], old[1], D.res(resId).sized(size), old[3])
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.rightImage(d: Drawable?, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(old[0], old[1], d, old[3])
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.topImage(resId: Int, size: Int, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(old[0], D.res(resId).sized(size), old[2], old[3])
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.topImage(d: Drawable?, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(old[0], d, old[2], old[3])
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.bottomImage(resId: Int, size: Int, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(old[0], old[1], old[2], D.res(resId).sized(size))
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.bottomImage(d: Drawable?, margin: Int = Space.Small): T {
    val old = this.compoundDrawables
    this.setCompoundDrawables(old[0], old[1], old[2], d)
    this.compoundDrawablePadding = dp(margin)
    return this
}

fun <T : TextView> T.imagePadding(p: Int): T {
    this.compoundDrawablePadding = dp(p)
    return this
}


fun <T : TextView> T.onTextChanged(block: (String) -> Unit): T {
    this.addTextChangedListener(object : XTextWatcher() {
        override fun afterTextChanged(text: String) {
            block(text)
        }

    })
    return this
}

fun TextView.moreArrow() {
    this.rightImage(R.drawable.yet_arrow_right, 16, 10)
}