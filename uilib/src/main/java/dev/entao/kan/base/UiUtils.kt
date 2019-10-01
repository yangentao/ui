package dev.entao.kan.base

import dev.entao.kan.json.YsonArray
import dev.entao.kan.json.YsonObject


object YsonObjectTextConvert : ITextConvert {
    override val defaultValue: Any = YsonObject()
    override fun fromText(text: String): Any? {
        return YsonObject(text)
    }
}

object YsonArrayTextConvert : ITextConvert {
    override val defaultValue: Any = YsonArray()
    override fun fromText(text: String): Any? {
        return YsonArray(text)
    }
}
