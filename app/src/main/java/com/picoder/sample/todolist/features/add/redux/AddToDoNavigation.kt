package com.picoder.sample.todolist.features.add.redux

sealed class AddToDoNavigation {

    data class ShowErrorMessage(val message:String): AddToDoNavigation()

    data class ShowMessageAndBackToList(val message:String) : AddToDoNavigation()
}