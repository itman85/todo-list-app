package com.picoder.sample.todolist.features.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.domain.usecase.AddToDo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToDoViewModel @Inject constructor(private val addToDo: AddToDo) : ViewModel() {

    fun addToDo(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addToDo.addToDo(toDoData)
        }
    }
}