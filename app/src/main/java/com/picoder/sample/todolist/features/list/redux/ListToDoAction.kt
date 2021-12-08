package com.picoder.sample.todolist.features.list.redux

import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.model.ToDoItem

sealed class ListToDoAction {

    object OpenScreen : ListToDoAction()

    object LoadToDoList : ListToDoAction()

    data class ToDoListLoaded(val todosList: List<ToDoItem>) : ListToDoAction()

    data class SearchToDoList(val query: String) : ListToDoAction()

    data class SortToDoList(val priority: Priority) : ListToDoAction()

    data class SwipeToDeleteToDoItem(val deletedItem: ToDoItem, val deletedItemPosition: Int) :
        ListToDoAction()

    data class DeleteItemSuccess(val deletedItem: ToDoItem, val deletedItemPosition: Int) :
        ListToDoAction()

    object RestoreDeletedItem : ListToDoAction()

    object RestoreDeletedItemSuccess : ListToDoAction()

    object ClearRestoredData : ListToDoAction()

    object DeleteAll : ListToDoAction()

    object DeleteAllSuccess : ListToDoAction()

    object AddNewToDoItem : ListToDoAction()
}