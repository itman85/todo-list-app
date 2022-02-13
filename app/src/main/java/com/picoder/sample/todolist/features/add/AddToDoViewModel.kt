package com.picoder.sample.todolist.features.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeletics.coredux.subscribeToChangedStateUpdates
import com.picoder.sample.todolist.features.add.redux.AddToDoAction
import com.picoder.sample.todolist.features.add.redux.AddToDoNavigation
import com.picoder.sample.todolist.features.add.redux.AddToDoState
import com.picoder.sample.todolist.features.add.redux.AddToDoStateMachine
import com.picoder.sample.todolist.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AddToDoViewModel @Inject constructor(
    private val stateMachine: AddToDoStateMachine
) : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val coReduxStore = stateMachine.create(this).also {
        it.subscribeToChangedStateUpdates { newState ->
            mutableState.value = newState
        }
    }

    private val mutableState = MutableLiveData<AddToDoState>()

    private val mutableNav = SingleLiveEvent<AddToDoNavigation>()

    val dispatchAction: (AddToDoAction) -> Unit = coReduxStore::dispatch

    val state: LiveData<AddToDoState> = mutableState

    val nav: LiveData<AddToDoNavigation> = mutableNav

    init {
        viewModelScope.launch {
            for(navigation in stateMachine.navigation){
                mutableNav.value = navigation
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}