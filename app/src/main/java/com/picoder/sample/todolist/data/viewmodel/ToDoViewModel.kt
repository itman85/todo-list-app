package com.picoder.sample.todolist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.picoder.sample.todolist.data.ToDoDatabase
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()

    private val repository: ToDoRepository = ToDoRepository(toDoDao)

    val getAllData: LiveData<List<ToDoData>> = repository.getAllData

    val sortByHighPriority: LiveData<List<ToDoData>> = repository.sortByHighPriority

    val sortByLowPriority: LiveData<List<ToDoData>> = repository.sortByLowPriority

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(toDoData)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchData(keyword: String): LiveData<List<ToDoData>> {
        return repository.searchData(keyword)
    }

}