package dev.entao.kan.util

object SeqN {
	private var value = 0

	operator fun next(): Int {
		return ++value
	}
}

