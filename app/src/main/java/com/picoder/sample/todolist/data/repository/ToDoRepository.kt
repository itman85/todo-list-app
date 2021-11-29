package com.picoder.sample.todolist.data.repository

import androidx.lifecycle.LiveData
import com.picoder.sample.todolist.data.ToDoDao
import com.picoder.sample.todolist.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData:LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }
}