package com.picoder.sample.todolist.domain.usecase

import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

class SearchToDoList @Inject constructor(private val repository: ToDoRepository) {

    fun searchToDoList(keywords: String) = repository.searchToDos(keywords)
}