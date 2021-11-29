package com.picoder.sample.todolist.data.model

enum class Priority {
    HIGH, MEDIUM, LOW
}

fun String.toPriority():Priority{
    return when(this){
        "High Priority" -> Priority.HIGH
        "Medium Priority" -> Priority.MEDIUM
        "Low Priority" -> Priority.LOW
        else -> Priority.LOW
    }
}