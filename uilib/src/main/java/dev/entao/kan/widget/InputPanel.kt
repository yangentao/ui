package dev.entao.kan.widget

import android.content.Context
import android.view.View
import android.widget.*
import dev.entao.kan.appbase.Task
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.ShapeRect
import dev.entao.kan.appbase.ex.StateList
import dev.entao.kan.appbase.ex.VState
import dev.entao.kan.base.BasePage
import dev.entao.kan.base.hideInputMethod
import dev.entao.kan.creator.*
import dev.entao.kan.ext.*
import dev.entao.kan.res.D
import dev.entao.kan.res.Res
import dev.entao.kan.theme.ViewSize
import dev.entao.kan.ui.R
import dev.entao.kan.util.TimeDown
import dev.entao.kan.util.ToastUtil
import dev.entao.kan.util.app.SmsCodeFill
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */

class InputPanel constructor(context: Context, private val fragment: BasePage? = null) : LinearLayout(context) {
    var INPUT_HEIGHT = 45
    private val editMap = HashMap<String, EditText>()
    private val checkMap = HashMap<String, CheckBox>()
    private val buttonMap = HashMap<String, Button>()
    private var codeEdit: EditText? = null
    private var codeButton: Button? = null
    private var timeDownKey: String? = null
    private var codeClickTime: Long = 0


    var inputMarginTop = 10
    var buttonMarginTop = 30

    var onButtonClick: (String) -> Unit = {
    }
    var onRequestCodeClick: (InputPanel, String) -> Unit = { _, _ ->

    }

    init {
        this.orientationVertical().padding(40, 25, 40, 5)
    }


    fun isCheck(key: String): Boolean {
        return checkMap[key]?.isChecked ?: false
    }

    fun setCheck(key: String, check: Boolean) {
        checkMap[key]?.isChecked = check
    }

    val code: String?
        get() {
            return codeEdit?.text.toString()
        }

    fun getText(key: String): String {
        return editMap[key]?.text.toString()
    }

    fun setText(key: String, text: String) {
        editMap[key]?.setText(text)
    }

    fun disableEdit(key: String) {
        editMap[key]?.isEnabled = false
    }

    fun enableButton(key: String, enable: Boolean) {
        buttonMap[key]?.isEnabled = enable
    }

    fun setButtonText(key: String, text: String) {
        buttonMap[key]?.text = text
    }

    fun getButtonText(key: String): String? {
        return buttonMap[key]?.text?.toString()
    }

    fun button(key: String): Button {
        return buttonMap[key]!!
    }

    fun edit(key: String): EditText {
        return editMap[key]!!
    }


    private fun makeEdit(hint: String, marginTop: Int): EditText {
        val ed = context.createEdit().hint(hint)
        ed.padding(5, 2, 5, 2)
        linearParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0).set(ed)
        return ed
    }

    fun addEdit(key: String, hint: String, marginTop: Int = inputMarginTop): EditText {
        val ed = makeEdit(hint, marginTop)
        addView(ed)
        editMap.put(key, ed)
        return ed
    }

    fun addPhone(key: String, hint: String = Res.str(R.string.yet_phone_input), marginTop: Int = inputMarginTop): EditText {
        val ed = makeEdit(hint, marginTop)
        ed.inputTypePhone()
        addView(ed)
        editMap.put(key, ed)
        return ed
    }

    @JvmOverloads
    fun addNumber(key: String, hint: String, marginTop: Int = inputMarginTop): EditText {
        val ed = makeEdit(hint, marginTop)
        ed.inputTypeNumber()
        addView(ed)
        editMap.put(key, ed)
        return ed
    }

    fun addPasswordAgain(key: String): EditText {
        return addPassword(key, Res.str(R.string.yet_pwd_again), inputMarginTop)
    }

    fun addPassword(key: String, hint: String = Res.str(R.string.yet_pwd_input), marginTop: Int = inputMarginTop): EditText {
        val ed = makeEdit(hint, marginTop)
        ed.inputTypePassword()
        addView(ed)
        editMap.put(key, ed)
        return ed
    }

    fun addPasswordNumber(key: String, hint: String = Res.str(R.string.yet_pwd_input), marginTop: Int = inputMarginTop): EditText {
        val ed = makeEdit(hint, marginTop)
        ed.inputTypePasswordNumber()
        addView(ed)
        editMap.put(key, ed)
        return ed
    }

    fun searchSmsCode() {
        if (this.codeEdit != null && codeClickTime != 0L) {
            SmsCodeFill.searchSmsCode(this.codeEdit as TextView, codeClickTime)
        }
    }

    fun startTimeDown(seconds: Int) {
        Task.fore {
            TimeDown.startClick(timeDownKey!!, seconds, codeButton!!)
        }
    }

    fun addVerifyCode(timeDownKey: String, phoneEditKey: String, block: (String) -> Unit) {
        addVerifyCode(timeDownKey, phoneEditKey, inputMarginTop, block)
    }

    fun addVerifyCode(timeDownKey: String, phoneEditKey: String, marginTop: Int, block: (String) -> Unit) {
        val llDraw = ShapeRect(Colors.TRANS, ViewSize.EditCorner).stroke(1, Colors.EditFocus).value
        val editDraw = ShapeRect(Colors.WHITE).corners(ViewSize.EditCorner, 0, 0, ViewSize.EditCorner).value
        val btnNormalDraw = ShapeRect(Colors.Theme).corners(0, ViewSize.EditCorner, ViewSize.EditCorner, 0).value
        val btnPressDraw = ShapeRect(Colors.Fade).corners(0, ViewSize.EditCorner, ViewSize.EditCorner, 0).value
        val btnDisableDraw = ShapeRect(Colors.Disabled).corners(0, ViewSize.EditCorner, ViewSize.EditCorner, 0).value
        val btnDraw = StateList.drawable(btnNormalDraw, VState.Pressed to btnPressDraw, VState.Disabled to btnDisableDraw)

        linearHor(lParam().widthFill().height(ViewSize.EditHeight).margins(0, marginTop, 0, 0)) {
            backDrawable(llDraw).padding(1)
            codeEdit = edit(lParam().width(0).weight(1f).heightFill()) {
                hint("输入验证码").inputTypeNumber().backDrawable(editDraw).padding(15, 0, 15, 0)
            }
            codeButton = button(lParam().widthWrap().heightFill()) {
                text("获取验证码").backDrawable(btnDraw).textColorWhite()
            }
        }

        this.timeDownKey = timeDownKey
        if (timeDownKey.isNotEmpty()) {
            TimeDown.updateView(this.timeDownKey!!, codeButton!!)
        }
        codeButton!!.setOnClickListener(View.OnClickListener {
            codeClickTime = System.currentTimeMillis()
            val phone = getText(phoneEditKey)
            if (phone.length < 11) {
                ToastUtil.show("请输入正确的手机号")
            } else {
                startTimeDown(60)
                Task.back {
                    block(phone)
                }
            }
        })
    }

    fun addCheckbox(key: String, title: String, marginTop: Int = inputMarginTop) {
        checkMap[key] = checkBox(linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0)) {
            padding(20, 5, 5, 5)
            text = title
            buttonDrawable = D.CheckBox
        }
    }

    fun addSafeButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
        buttonMap[key] = button(LParam.WidthFill.HeightButton.margins(0, marginTop, 0, 0)) {
            text = title
            styleGreen()
            setOnClickListener { _onButtonClick(key) }
        }
    }

    fun addRedButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
        buttonMap[key] = button(LParam.WidthFill.HeightButton.margins(0, marginTop, 0, 0)) {
            text = title
            styleRed()
            setOnClickListener { _onButtonClick(key) }
        }
    }

    fun addWhiteButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
        buttonMap[key] = button(LParam.WidthFill.HeightButton.margins(0, marginTop, 0, 0)) {
            text = title
            styleWhite()
            setOnClickListener { _onButtonClick(key) }
        }
    }

    fun _onButtonClick(key: String) {
        fragment?.hideInputMethod()
        onButtonClick(key)
    }

}
