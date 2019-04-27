package dev.entao.kan.util

import android.os.Handler
import android.os.HandlerThread
import dev.entao.kan.appbase.Task

class TaskQueue(name: String) {
	val handler: Handler
	private val thread: HandlerThread = HandlerThread("QueueTask:$name")

	init {
		thread.isDaemon = true
		thread.start()
		handler = Handler(thread.looper)
	}


	fun quit() {
		handler.looper.quit()
	}

	fun back(r: Runnable) {
		handler.post(r)
	}

	/**
	 * thread handler中运行, 排队,保证顺序
	 */
	fun back(t: RunTask): RunTask {
		handler.post(t)
		return t
	}

	fun backDelay(millSec: Int, task: Runnable) {
		handler.postDelayed(task, millSec.toLong())
	}

	fun backDelay(millSec: Int, task: RunTask): RunTask {
		handler.postDelayed(task, millSec.toLong())
		return task
	}

	fun back(block: () -> Unit) {
		handler.post {
			block.invoke()
		}
	}

	fun fore(r: Runnable) {
		Task.mainHandler.post(r)
	}

	fun fore(r: RunTask): RunTask {
		Task.mainHandler.post(r)
		return r
	}

	fun foreDelay(millSec: Int, r: Runnable) {
		Task.mainHandler.postDelayed(r, millSec.toLong())
	}

	fun foreDelay(millSec: Int, r: RunTask): RunTask {
		Task.mainHandler.postDelayed(r, millSec.toLong())
		return r
	}
}
