package dev.entao.kan.base

import dev.entao.kan.appbase.ex.dp
import dev.entao.kan.appbase.ex.sp

val Int.dpf: Float get() = this.dp.toFloat()
val Int.spf: Float get() = this.sp.toFloat()