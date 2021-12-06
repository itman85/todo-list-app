package com.picoder.sample.todolist.features.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.domain.usecase.*
import com.picoder.sample.todolist.model.ToDoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListToDoViewModel @Inject constructor(
    private val loadAllToDoList: LoadAllToDoList,
    private val deleteToDo: DeleteToDo,
    private val sortToDoList: SortToDoList,
    private val searchToDoList: SearchToDoList,
    private val addToDo: AddToDo
) : ViewModel() {

    private val _allData = MutableLiveData<List<ToDoItem>>()

    val getAllData: LiveData<List<ToDoItem>> = _allData

    private val _searchData = MutableLiveData<List<ToDoItem>>()

    val getSearchResultData: LiveData<List<ToDoItem>> = _searchData

    private val _sortData = MutableLiveData<List<ToDoItem>>()

    val getSortResultData: LiveData<List<ToDoItem>> = _sortData

    private suspend fun loadDataAndNotify(){
        _allData.postValue(loadAllToDoList.loadAllToDos().map {
            ToDoItem(it.id, it.title, it.priority, it.description)
        })
    }

    fun restoreToDoItem(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addToDo.addToDo(toDoData)
            loadDataAndNotify()
        }
    }

    fun deleteToDo(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteToDo.deleteToDo(toDoData)
            loadDataAndNotify()
        }
    }

    fun loadToDoList() {
        viewModelScope.launch(Dispatchers.IO) {
            loadDataAndNotify()
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteToDo.deleteAllToDos()
            loadDataAndNotify()
        }
    }

    fun searchToDoList(keywords: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchData.postValue(searchToDoList.searchToDoList(keywords).map {
                ToDoItem(it.id, it.title, it.priority, it.description)
            })
        }
    }

    fun sortToDoList(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            _sortData.postValue(sortToDoList.sortByPriority(priority).map {
                ToDoItem(it.id, it.title, it.priority, it.description)
            })
        }
    }
}