package com.picoder.sample.todolist.features.update.redux

sealed class UpdateToDoNavigation {

    data class ShowErrorMessage(val message:String): UpdateToDoNavigation()

    data class ShowMessageAndBackToList(val message:String) : UpdateToDoNavigation()
}