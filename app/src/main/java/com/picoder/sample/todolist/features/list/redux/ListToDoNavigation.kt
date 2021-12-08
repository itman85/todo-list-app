package com.picoder.sample.todolist.features.list.redux

import com.picoder.sample.todolist.model.ToDoItem

sealed class ListToDoNavigation {

    data class ShowDeleteItemSuccessMessageAndRestoreSnackbar(
        val message: String
    ) : ListToDoNavigation()

    data class ShowDeleteAllSuccessMessage(val message: String) : ListToDoNavigation()

    object OpenAddNewToDoItem : ListToDoNavigation()
}