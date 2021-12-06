package com.picoder.sample.todolist.domain.entity

interface ToDoEntity {
    val id:Int

    val title: String

    val priority: Priority

    val description: String
}