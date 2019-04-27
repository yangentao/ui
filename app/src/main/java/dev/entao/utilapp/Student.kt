package dev.entao.utilapp

import dev.entao.kan.json.YsonObject

class Student(val yo: YsonObject) {
    var name: String by yo
}