package com.picoder.sample.todolist.features.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.domain.usecase.AddToDo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToDoViewModel @Inject constructor(private val addToDo: AddToDo) : ViewModel() {
    private val disposables = CompositeDisposable()

    fun addToDo(toDoData: ToDoItem) {
        disposables.add(
        addToDo.addToDo(toDoData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                // should callback to something in UI thread
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}