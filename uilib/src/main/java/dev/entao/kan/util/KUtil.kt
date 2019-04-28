package dev.entao.kan.util

import dev.entao.kan.appbase.App

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */


val debug: Boolean by lazy { App.debug }


fun ByteArray?.prefix(vararg bs: Byte): Boolean {
    if (this == null) {
        return false
    }
    if (this.size < bs.size) {
        return false
    }
    for (i: Int in bs.indices) {
        if (this[i] != bs[i]) {
            return false
        }
    }
    return true
}

fun ByteArray?.skip(n: Int): ByteArray {
    if (this == null || this.size <= n) {
        return byteArrayOf()
    }
    val arr = ByteArray(this.size - n)
    for (i in this.indices) {
        if (i >= n) {
            arr[i - n] = this[i]
        }
    }
    return arr
}


