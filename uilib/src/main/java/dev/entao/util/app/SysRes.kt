package dev.entao.util.app

import android.content.res.Resources
import dev.entao.util.RefUtil

/**
 * Created by entaoyang@163.com on 2016-10-05.
 */

object SysRes {
    fun bool(name: String, defVal: Boolean): Boolean {
        val n = idBool(name)
        return if (n != null) {
            Resources.getSystem().getBoolean(n)
        } else defVal
    }

    fun string(name: String, defVal: String): String {
        val n = idString(name)
        return if (n != null) {
            Resources.getSystem().getString(n)
        } else defVal
    }

    fun idBool(name: String): Int? {
        return RefUtil.getStatic("com.android.internal.R\$bool", name) as Int
    }

    fun idBool(name: String, defVal: Int): Int {
        val n = RefUtil.getStatic("com.android.internal.R\$bool", name) as Int
        return n ?: defVal
    }

    fun idString(name: String): Int? {
        return RefUtil.getStatic("com.android.internal.R\$string", name) as Int
    }

    fun idString(name: String, defVal: Int): Int {
        val n = RefUtil.getStatic("com.android.internal.R\$string", name) as Int
        return n ?: defVal
    }
}
