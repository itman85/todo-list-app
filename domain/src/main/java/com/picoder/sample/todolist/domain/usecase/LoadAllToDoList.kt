package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

class LoadAllToDoList @Inject constructor(private val toDoRepository: ToDoRepository) {

    fun loadAllToDos() = toDoRepository.getAllToDoList()
}