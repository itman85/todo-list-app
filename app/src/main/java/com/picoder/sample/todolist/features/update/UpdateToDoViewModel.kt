package com.picoder.sample.todolist.features.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.domain.usecase.DeleteToDo
import com.picoder.sample.todolist.domain.usecase.UpdateToDo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// use HiltViewModel annotation only with Inject in constructor
@HiltViewModel
class UpdateToDoViewModel @Inject constructor(private val updateToDo: UpdateToDo) : ViewModel() {

    @Inject
    lateinit var deleteToDo: DeleteToDo

    fun addToDo(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateToDo.updateToDo(toDoData)
        }
    }

    fun deleteToDo(toDoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteToDo.deleteToDo(toDoItem)
        }
    }

    fun updateToDo(toDoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateToDo.updateToDo(toDoItem)
        }
    }
}