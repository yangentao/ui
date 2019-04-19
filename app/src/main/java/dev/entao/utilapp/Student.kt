package dev.entao.utilapp

import dev.entao.json.YsonObject

class Student(val yo: YsonObject) {
    var name: String by yo
}