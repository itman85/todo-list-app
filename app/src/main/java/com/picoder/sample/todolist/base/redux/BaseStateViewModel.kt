package com.picoder.sample.todolist.base.redux

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picoder.sample.todolist.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.Subject

abstract class BaseStateViewModel<S : Any, A : Any, N : Any>(
    stateMachine: BaseStateMachine<S, A, N>
) : ViewModel() {

    private val mutableState = MutableLiveData<S>()

    private val disposables = CompositeDisposable()

    private val mutableNav = SingleLiveEvent<N>()

    private val input: Subject<A> = stateMachine.input

    val state: LiveData<S> = mutableState

    val nav: LiveData<N> = mutableNav

    init {
        addDisposable {
            stateMachine.state()
                // important to set live data values on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state ->
                    mutableState.value = state
                }
        }

        stateMachine.navigation().let {
            addDisposable {
                it
                    // important to set live data values on main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { navState ->
                        mutableNav.value = navState
                    }
            }
        }
    }

    private fun addDisposable(disposable: () -> Disposable) {
        disposables.add(disposable())
    }

    fun doAction(action: A) {
        input.onNext(action)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}