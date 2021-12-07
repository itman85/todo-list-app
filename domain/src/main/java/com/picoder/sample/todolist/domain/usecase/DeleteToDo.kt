package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

class DeleteToDo @Inject constructor(private val repository: ToDoRepository) {

    fun deleteToDo(toDoEntity: ToDoEntity) = repository.deleteToDo(toDoEntity)

    suspend fun deleteAllToDos() = repository.deleteAllToDoList()
}