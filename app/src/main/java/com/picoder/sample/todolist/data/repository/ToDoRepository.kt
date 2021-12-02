package com.picoder.sample.todolist.data.repository

import androidx.lifecycle.LiveData
import com.picoder.sample.todolist.data.ToDoDao
import com.picoder.sample.todolist.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()

    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()


    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchData(keyword: String): LiveData<List<ToDoData>> {
        return toDoDao.searchData("%$keyword%")
    }
}