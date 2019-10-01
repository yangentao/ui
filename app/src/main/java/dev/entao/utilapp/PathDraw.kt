package dev.entao.utilapp

import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape

fun aPathDraw(block: () -> Unit) {

}

class PathDraw(val path: Path, width: Int, height: Int) : ShapeDrawable(PathShape(path, width.toFloat(), height.toFloat())) {

    init {
        this.intrinsicWidth = width
        this.intrinsicHeight = height
    }

    fun fill(color: Int) {
        this.paint.color = color
        this.paint.style = Paint.Style.FILL
    }

    fun stroke(color: Int, width: Float) {
        this.paint.color = color
        this.paint.style = Paint.Style.STROKE
    }
}