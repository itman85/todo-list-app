package com.picoder.sample.todolist.features.update.redux

import androidx.annotation.VisibleForTesting
import com.freeletics.rxredux.Reducer
import com.freeletics.rxredux.SideEffect
import com.picoder.sample.todolist.base.redux.BaseStateMachine
import com.picoder.sample.todolist.domain.usecase.DeleteToDo
import com.picoder.sample.todolist.domain.usecase.UpdateToDo
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.model.toEntity
import com.picoder.sample.todolist.utils.navigationWithThrottling
import com.picoder.sample.todolist.utils.validateDataFromInput
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class UpdateToDoStateMachine @Inject constructor(
    private val updateToDo: UpdateToDo,
    private val deleteToDo: DeleteToDo
) :
    BaseStateMachine<UpdateToDoState, UpdateToDoAction, UpdateToDoNavigation>() {

    @VisibleForTesting
    val navigationSideEffect: SideEffect<UpdateToDoState, UpdateToDoAction> = { actions, state ->
        actions.ofType(UpdateToDoAction::class.java)
            .navigationWithThrottling(1000L) { action ->
                when (action) {
                    is UpdateToDoAction.SaveDataSuccess -> UpdateToDoNavigation.ShowMessageAndBackToList(
                        "Successfully Updated!"
                    )
                    is UpdateToDoAction.ValidateDataFail -> UpdateToDoNavigation.ShowErrorMessage(
                        action.errorMsg
                    )
                    is UpdateToDoAction.DeleteDataSuccess -> UpdateToDoNavigation.ShowMessageAndBackToList(
                        action.deleteMessage
                    )
                    else -> null
                }
            }
            .doOnNext {
                nav.onNext(it)
            }.switchMap { Observable.empty() }
    }

    @VisibleForTesting
    val updateToDoItemSideEffect: SideEffect<UpdateToDoState, UpdateToDoAction> =
        { actions, state ->
            actions.ofType(UpdateToDoAction.UpdateToDoItem::class.java)
                .switchMap { action ->
                    val validation = validateDataFromInput(action.title, action.description)
                    if (validation)
                        Observable.just(
                            UpdateToDoAction.SaveData(
                                ToDoItem(
                                    state().updatedItem.id,
                                    action.title,
                                    action.priority,
                                    action.description
                                )
                            )
                        )
                    else
                        Observable.just(UpdateToDoAction.ValidateDataFail("Please input all fields!"))
                }
        }

    @VisibleForTesting
    val saveDataSideEffect: SideEffect<UpdateToDoState, UpdateToDoAction> =
        { actions, _ ->
            actions.ofType(UpdateToDoAction.SaveData::class.java)
                .switchMap { action ->
                    updateToDo.updateToDo(action.item.toEntity())
                        .subscribeOn(Schedulers.io())
                        .andThen(Observable.defer {
                            Observable.just(UpdateToDoAction.SaveDataSuccess)
                        })

                }
        }

    @VisibleForTesting
    val deleteDataSideEffect: SideEffect<UpdateToDoState, UpdateToDoAction> =
        { actions, state ->
            actions.ofType(UpdateToDoAction.DeleteToDoItem::class.java)
                .switchMap {
                    deleteToDo.deleteToDo(state().updatedItem.toEntity())
                        .subscribeOn(Schedulers.io())
                        .andThen(Observable.defer {
                            Observable.just(UpdateToDoAction.DeleteDataSuccess("Successfully Removed: ${state().updatedItem.title}"))
                        })

                }
        }


    override val initialState = UpdateToDoState(ToDoItem.defaultData())

    override fun sideEffects(): List<SideEffect<UpdateToDoState, UpdateToDoAction>> =
        listOf(
            navigationSideEffect,
            updateToDoItemSideEffect,
            saveDataSideEffect,
            deleteDataSideEffect
        )

    override fun reducer(): Reducer<UpdateToDoState, UpdateToDoAction> = this::reducer

    private fun reducer(state: UpdateToDoState, action: UpdateToDoAction): UpdateToDoState {
        return when (action) {
            is UpdateToDoAction.OpenScreen -> state.copy(updatedItem = action.item)

            is UpdateToDoAction.SaveData -> state.copy(updatedItem = action.item)

            else -> state
        }
    }


}