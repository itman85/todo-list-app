package com.picoder.sample.todolist.data.repositories

import com.picoder.sample.todolist.data.database.dao.ToDoDao
import com.picoder.sample.todolist.data.database.entity.ToDoData
import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(private val toDoDao: ToDoDao) : ToDoRepository {

    override suspend fun getAllToDoList(): List<ToDoEntity> {
        return toDoDao.getAllData()
    }

    override suspend fun addToDo(toDoEntity: ToDoEntity) {
        toDoDao.insertData(
            ToDoData(
                toDoEntity.id,
                toDoEntity.title,
                toDoEntity.priority,
                toDoEntity.description
            )
        )
    }

    override suspend fun updateToDo(toDoEntity: ToDoEntity) {
        toDoDao.updateData(
            ToDoData(
                toDoEntity.id,
                toDoEntity.title,
                toDoEntity.priority,
                toDoEntity.description
            )
        )
    }

    override suspend fun deleteToDo(toDoEntity: ToDoEntity) {
        toDoDao.deleteItem(
            ToDoData(
                toDoEntity.id,
                toDoEntity.title,
                toDoEntity.priority,
                toDoEntity.description
            )
        )
    }

    override suspend fun deleteAllToDoList() {
        toDoDao.deleteAll()
    }

    override suspend fun searchToDos(keywords: String): List<ToDoEntity> {
        return toDoDao.searchData(keywords)
    }

    override suspend fun sortByHighPriority(): List<ToDoEntity> {
        return toDoDao.sortByHighPriority()
    }

    override suspend fun sortByLowPriority(): List<ToDoEntity> {
        return toDoDao.sortByLowPriority()
    }
}