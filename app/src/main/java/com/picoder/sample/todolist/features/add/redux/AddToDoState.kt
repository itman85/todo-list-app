package com.picoder.sample.todolist.features.add.redux

import com.picoder.sample.todolist.model.ToDoItem

data class AddToDoState(val addedItem: ToDoItem? = null)