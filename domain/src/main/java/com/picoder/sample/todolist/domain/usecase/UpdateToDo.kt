package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

class UpdateToDo @Inject constructor(private val toDoRepository: ToDoRepository) {

    suspend fun updateToDo(toDoEntity: ToDoEntity) = toDoRepository.updateToDo(toDoEntity)
}