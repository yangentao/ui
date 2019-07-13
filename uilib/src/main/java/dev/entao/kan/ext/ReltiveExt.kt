@file:Suppress("unused")

package dev.entao.kan.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import dev.entao.kan.base.act

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

//create

fun Context.relativeLayout(): RelativeLayout {
    return RelativeLayout(this).genId()
}

fun Fragment.relativeLayout(): RelativeLayout {
    return this.act.relativeLayout()
}

fun ViewGroup.relativeLayout(): RelativeLayout {
    return context.relativeLayout()
}


inline fun <reified T : RelativeLayout> T.addViewParam(view: View, noinline f: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): T {
    val lp = relativeParam(f)
    this.addView(view, lp)
    return this
}

