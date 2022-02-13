package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

// this show how we use both rx and coroutine, it will duplicate code for this usecase use in rx and coroutine
class AddToDo @Inject constructor(private val toDoRepository: ToDoRepository) {

    // use in rx
    fun addToDo(toDoEntity: ToDoEntity) = toDoRepository.addToDo(toDoEntity)

    // use in coroutine
    suspend fun addToDoSuspend(toDoEntity: ToDoEntity) = toDoRepository.addToDoSuspend(toDoEntity)
}