package com.picoder.sample.todolist.features.update.redux

import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.model.ToDoItem

sealed class UpdateToDoAction {

    data class OpenScreen(val item: ToDoItem) : UpdateToDoAction()

    data class UpdateToDoItem(val title: String, val priority: Priority, val description: String) :
        UpdateToDoAction()

    data class ValidateDataFail(val errorMsg: String) : UpdateToDoAction()

    data class SaveData(val item: ToDoItem) : UpdateToDoAction()

    object SaveDataSuccess : UpdateToDoAction()

    object DeleteToDoItem : UpdateToDoAction()

    data class DeleteDataSuccess(val deleteMessage:String) : UpdateToDoAction()
}