package com.picoder.sample.todolist.domain.repository

import com.picoder.sample.todolist.domain.entity.ToDoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ToDoRepository {

    fun getAllToDoList(): Single<List<ToDoEntity>>

    fun addToDo(toDoEntity: ToDoEntity): Completable

    suspend fun addToDoSuspend(toDoEntity: ToDoEntity)

    fun updateToDo(toDoEntity: ToDoEntity): Completable

    fun deleteToDo(toDoEntity: ToDoEntity): Completable

    fun deleteAllToDoList(): Completable

    fun searchToDos(keywords: String): Single<List<ToDoEntity>>

    fun sortByHighPriority(): Single<List<ToDoEntity>>

    fun sortByLowPriority(): Single<List<ToDoEntity>>
}