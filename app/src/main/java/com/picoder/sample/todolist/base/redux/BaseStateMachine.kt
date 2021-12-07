package com.picoder.sample.todolist.base.redux

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.freeletics.rxredux.Reducer
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

abstract class BaseStateMachine<S : Any, A : Any, N : Any> {

    val input: Subject<A> = PublishSubject.create()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    open val nav: Subject<N> = PublishSubject.create()

    protected abstract val initialState: S

    protected abstract fun sideEffects(): List<SideEffect<S, A>>

    protected abstract fun reducer(): Reducer<S, A>

    open fun navigation(): Observable<N> = nav

    fun state(): Observable<S> = input
        .doOnNext { Log.i(this.javaClass.simpleName, "Action fired: $it") }
        .reduxStore(
            initialState = initialState,
            sideEffects = sideEffects(),
            reducer = reducer()
        )
        .distinctUntilChanged()
        .doOnNext {
            Log.i(this.javaClass.simpleName, "state updated $it")
        }
}