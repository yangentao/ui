@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package dev.entao.kan.page

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import dev.entao.kan.appbase.ex.Colors
import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.appbase.ex.sized
import dev.entao.kan.appbase.ex.tinted
import dev.entao.kan.base.act
import dev.entao.kan.base.getValue
import dev.entao.kan.base.labelProp_
import dev.entao.kan.base.nameProp
import dev.entao.kan.creator.createButton
import dev.entao.kan.creator.createView
import dev.entao.kan.creator.linearVer
import dev.entao.kan.ext.*
import dev.entao.kan.list.itemviews.TextDetailView
import dev.entao.kan.list.itemviews.TextItemView
import dev.entao.kan.res.drawableRes
import dev.entao.kan.widget.UserItemView
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

abstract class CmdPage : TitlePage() {
    var divider: Divider = Divider()
    val cmdList = ArrayList<Cmd>()
    lateinit var cmdPanel: LinearLayout

    var defaultItemHeight: Int = 50
    var defaultIconSize: Int = 32

    var tintCmdIcon = true

    val bindList = ArrayList<PropBind>()

    init {
        enableContentScroll = true
    }

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        cmdPanel = contentView.linearVer(LParam.WidthFill.HeightWrap) {
            setBackgroundColor(Color.WHITE)
            divider(divider)
        }
    }

    override fun onContentCreated() {
        super.onContentCreated()
        commit()
    }

    override fun onDestroyView() {
        cmdPanel.removeAllViews()
        cmdList.clear()
        bindList.clear()
        super.onDestroyView()
    }

    fun commit() {
        cmdPanel.removeAllViews()
        for (c in cmdList) {
            c.view.removeFromParent()
            cmdPanel.addView(c.view, c.param)
            c.view.tag = c
            if (c.clickable) {
                c.view.onClick {
                    val cc = it.tag as Cmd
                    cc.onClick(cc)
                }
            }
        }
    }

    fun textItemView(block: TextItemView.() -> Unit): TextItemView {
        val v = TextItemView(this.requireActivity())
        v.backColorWhiteFade()
        v.textSizeB()
        v.textColorMajor()
        v.block()
        return v
    }

    fun textDetailItemView(block: TextDetailView.() -> Unit): TextDetailView {
        val v = TextDetailView(act)
        v.backColorWhiteFade()
        v.textView.textSizeB()
        v.detailView.textSizeC()
        v.padding(20, 15, 20, 15)
        v.block()
        return v
    }

    fun buttonItemView(block: Button.() -> Unit): Button {
        val b = createButton()
        b.block()
        return b
    }

    fun findCmd(cmd: String): Cmd? {
        return cmdList.find { it.cmd == cmd }
    }

    fun cmd(cmd: String, block: Cmd.() -> Unit): Cmd {
        val a = Cmd(cmd)
        cmdList.add(a)
        a.block()
        return a
    }

    fun cmdTextDetail(label: String, value: String, cmd: String = label): Cmd {
        return cmd(cmd) {
            view = textDetailItemView {
                this.setText(label)
                this.setDetail(value)
            }
        }
    }

    fun cmdTextDetail(p: KProperty0<*>): Cmd {
        return cmdTextDetail(p.labelProp_, p.getValue()?.toString() ?: "", p.nameProp)
    }

    fun cmdTextDetailBind(p: KProperty1<*, *>, onBind: (TextDetailView, Any?) -> Unit = ::defaultBind): Cmd {
        val c = cmdTextDetail(p.labelProp_, "", p.nameProp)
        bindList += PropBind(c, p, onBind)
        return c
    }

    fun defaultBind(view: TextDetailView, value: Any?) {
        view.setDetail(value?.toString())
    }

    fun bind(model: Any) {
        bindList.forEach {
            val view = it.cmd.view as TextDetailView
            val value = it.prop.getValue(model)
            it.onBind(view, value)
        }
    }

    fun cmdTextView(cmd: String, block: TextItemView.() -> Unit): Cmd {
        return cmd(cmd) {
            view = textItemView {
                minimumHeight = dp(defaultItemHeight)
                this.block()
            }
        }
    }

    fun cmdText(text: String, @DrawableRes leftIcon: Int = 0, @DrawableRes rightIcon: Int = 0, rightSize: Int = 16, cmd: String = text): Cmd {
        return cmdTextView(cmd) {
            this.text = text
            if (leftIcon != 0) {
                if (tintCmdIcon) {
                    leftImage(leftIcon.drawableRes.tinted(Colors.Theme).sized(defaultIconSize))
                } else {
                    leftImage(leftIcon, defaultIconSize)
                }
            }
            if (rightIcon != 0) {
                rightImage(rightIcon, rightSize)
            }
        }
    }

    fun cmdUser(cmd: String = "user"): Cmd {
        return cmd(cmd) {
            val iv = UserItemView(act)
            iv.bindValues("点击登录", "")
            view = iv
        }
    }

    fun cmdButtonRed(text: String): Cmd {
        return cmd(text) {
            view = buttonItemView {
                styleRedRound()
                textS = text
            }
            param = LParam.width(280).HeightButton.gravityCenter().margins(20)
        }
    }

    fun cmdButtonGreen(text: String): Cmd {
        return cmd(text) {
            view = buttonItemView {
                styleGreenRound()
                textS = text
            }
            param = LParam.width(280).HeightButton.gravityCenter().margins(20)
        }
    }

    fun cmdButtonWhite(text: String): Cmd {
        return cmd(text) {
            view = buttonItemView {
                style {
                    outlineBlue()
                    cornersRound()
                }
                textS = text
            }
            param = LParam.width(280).HeightButton.gravityCenter().margins(20)
        }
    }

    fun sep(sepHeight: Int = 10): CmdPage {
        val a = Cmd(Cmd.Sep)
        a.clickable = false
        a.param.height(sepHeight)
        a.view = createView().backColor(divider.color)
        cmdList.add(a)
        return this
    }

    fun label(label: String, height: Int = 50): TextItemView {
        val a = Cmd(Cmd.Sep)
        a.clickable = false
        a.param.height(height)
        val tv = textItemView {
            text = label
            backColorPage()
        }
        a.view = tv
        cmdList.add(a)
        return tv
    }

    class PropBind(val cmd: Cmd, val prop: KProperty<*>, val onBind: (TextDetailView, Any?) -> Unit)

}
