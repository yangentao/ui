package dev.entao.util.app


import dev.entao.util.RefUtil

object SystemProperties {
    private val CLS = "android.os.SystemProperties"

    operator fun get(key: String): String {
        return RefUtil.invokeStatic(CLS, "get", key) as String
    }

    operator fun get(key: String, defVal: String): String {
        return RefUtil.invokeStatic(
            CLS,
            "get",
            arrayOf<Class<*>>(String::class.java, String::class.java),
            key,
            defVal
        ) as String
    }
}
