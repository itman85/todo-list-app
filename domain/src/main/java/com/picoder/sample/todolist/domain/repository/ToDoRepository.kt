package com.picoder.sample.todolist.domain.repository

import com.picoder.sample.todolist.domain.entity.ToDoEntity

interface ToDoRepository {

    suspend fun getAllToDoList(): List<ToDoEntity>

    suspend fun addToDo(toDoEntity: ToDoEntity)

    suspend fun updateToDo(toDoEntity: ToDoEntity)

    suspend fun deleteToDo(toDoEntity: ToDoEntity)

    suspend fun deleteAllToDoList()

    suspend fun searchToDos(keywords: String): List<ToDoEntity>

    suspend fun sortByHighPriority(): List<ToDoEntity>

    suspend fun sortByLowPriority(): List<ToDoEntity>
}