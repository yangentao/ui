package dev.entao.kan.base

import android.content.Intent


interface ActivityResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean
}