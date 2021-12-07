package com.picoder.sample.todolist.domain.repository

import com.picoder.sample.todolist.domain.entity.ToDoEntity
import io.reactivex.rxjava3.core.Completable

interface ToDoRepository {

    suspend fun getAllToDoList(): List<ToDoEntity>

    suspend fun addToDo(toDoEntity: ToDoEntity)

    fun updateToDo(toDoEntity: ToDoEntity): Completable

    fun deleteToDo(toDoEntity: ToDoEntity): Completable

    suspend fun deleteAllToDoList()

    suspend fun searchToDos(keywords: String): List<ToDoEntity>

    suspend fun sortByHighPriority(): List<ToDoEntity>

    suspend fun sortByLowPriority(): List<ToDoEntity>
}