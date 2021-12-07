package com.picoder.sample.todolist.features.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picoder.sample.todolist.base.redux.BaseStateViewModel
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.domain.usecase.DeleteToDo
import com.picoder.sample.todolist.domain.usecase.UpdateToDo
import com.picoder.sample.todolist.features.update.redux.UpdateToDoAction
import com.picoder.sample.todolist.features.update.redux.UpdateToDoNavigation
import com.picoder.sample.todolist.features.update.redux.UpdateToDoState
import com.picoder.sample.todolist.features.update.redux.UpdateToDoStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// use HiltViewModel annotation only with Inject in constructor
@HiltViewModel
class UpdateToDoViewModel @Inject constructor(stateMachine: UpdateToDoStateMachine) :
    BaseStateViewModel<UpdateToDoState, UpdateToDoAction, UpdateToDoNavigation>(stateMachine)