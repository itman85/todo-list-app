package com.picoder.sample.todolist.features.list.redux

import com.picoder.sample.todolist.model.RestoredToDoItem
import com.picoder.sample.todolist.model.ToDoItem

data class ListToDoState(
    val items: List<ToDoItem> = emptyList(),
    val restoredData: RestoredToDoItem? = null
) {
    fun isEmpty() = items.isEmpty()
}