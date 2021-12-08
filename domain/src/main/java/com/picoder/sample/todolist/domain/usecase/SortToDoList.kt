package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SortToDoList @Inject constructor(private val todoRepository: ToDoRepository) {

     fun sortByPriority(priority: Priority): Single<List<ToDoEntity>> {
        return when (priority) {
            Priority.HIGH -> todoRepository.sortByHighPriority()
            Priority.LOW -> todoRepository.sortByLowPriority()
            else -> todoRepository.getAllToDoList()
        }
    }
}