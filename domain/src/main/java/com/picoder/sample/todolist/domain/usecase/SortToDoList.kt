package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

class SortToDoList @Inject constructor(private val todoRepository: ToDoRepository) {

    suspend fun sortByPriority(priority: Priority): List<ToDoEntity> {
        return when (priority) {
            Priority.HIGH -> todoRepository.sortByHighPriority()
            Priority.LOW -> todoRepository.sortByLowPriority()
            else -> todoRepository.getAllToDoList()
        }
    }
}