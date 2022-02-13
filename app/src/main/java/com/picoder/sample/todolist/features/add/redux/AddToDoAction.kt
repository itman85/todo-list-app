package com.picoder.sample.todolist.features.add.redux

import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.features.update.redux.UpdateToDoAction
import com.picoder.sample.todolist.model.ToDoItem

sealed class AddToDoAction {

    data class AddToDoItem(val title: String, val priority: Priority, val description: String) :
        AddToDoAction()

    data class ValidateDataFail(val errorMsg: String) : AddToDoAction()

    data class SaveData(val item: ToDoItem) : AddToDoAction()

    object SaveDataSuccess : AddToDoAction()
}