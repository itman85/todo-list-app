package com.picoder.sample.todolist.features.list.redux

import androidx.annotation.VisibleForTesting
import com.freeletics.rxredux.SideEffect
import com.picoder.sample.todolist.base.redux.BaseStateMachine
import com.picoder.sample.todolist.domain.usecase.*
import com.picoder.sample.todolist.model.RestoredToDoItem
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.utils.navigationWithThrottling
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ListToDoStateMachine @Inject constructor(
    private val loadAllToDoList: LoadAllToDoList,
    private val deleteToDo: DeleteToDo,
    private val sortToDoList: SortToDoList,
    private val searchToDoList: SearchToDoList,
    private val addToDo: AddToDo
) : BaseStateMachine<ListToDoState, ListToDoAction, ListToDoNavigation>() {

    @VisibleForTesting
    val navigationSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, state ->
        actions.ofType(ListToDoAction::class.java)
            .navigationWithThrottling(1000L) { action ->
                when (action) {
                    is ListToDoAction.DeleteItemSuccess -> ListToDoNavigation.ShowDeleteItemSuccessMessageAndRestoreSnackbar(
                        "Successfully Removed: '${action.deletedItem.title}'"
                    )

                    is ListToDoAction.DeleteAllSuccess -> ListToDoNavigation.ShowDeleteAllSuccessMessage(
                        "Successfully Removed All!"
                    )

                    is ListToDoAction.AddNewToDoItem -> ListToDoNavigation.OpenAddNewToDoItem
                    else -> null
                }
            }
            .doOnNext {
                nav.onNext(it)
            }.switchMap { Observable.empty() }
    }

    @VisibleForTesting
    val openScreenSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, _ ->
        actions.ofType(ListToDoAction.OpenScreen::class.java)
            .map { ListToDoAction.LoadToDoList }
    }

    @VisibleForTesting
    val loadToDoListSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, _ ->
        actions.ofType(ListToDoAction.LoadToDoList::class.java)
            .switchMap {
                loadAllToDoList.loadAllToDos()
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        data.map { entity ->
                            ToDoItem(entity.id, entity.title, entity.priority, entity.description)
                        }
                    }
                    .flatMapObservable { data ->
                        Observable.just(ListToDoAction.ToDoListLoaded(data))
                    }
            }
    }

    @VisibleForTesting
    val searchToDoListSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, _ ->
        actions.ofType(ListToDoAction.SearchToDoList::class.java)
            .switchMap { action ->
                searchToDoList.searchToDoList(action.query)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        data.map { entity ->
                            ToDoItem(entity.id, entity.title, entity.priority, entity.description)
                        }
                    }
                    .flatMapObservable { data ->
                        Observable.just(ListToDoAction.ToDoListLoaded(data))
                    }
            }
    }

    @VisibleForTesting
    val sortToDoListSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, _ ->
        actions.ofType(ListToDoAction.SortToDoList::class.java)
            .switchMap { action ->
                sortToDoList.sortByPriority(action.priority)
                    .subscribeOn(Schedulers.io())
                    .map { data ->
                        data.map { entity ->
                            ToDoItem(entity.id, entity.title, entity.priority, entity.description)
                        }
                    }
                    .flatMapObservable { data ->
                        Observable.just(ListToDoAction.ToDoListLoaded(data))
                    }
            }
    }

    @VisibleForTesting
    val deleteToDoItemSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, _ ->
        actions.ofType(ListToDoAction.SwipeToDeleteToDoItem::class.java)
            .switchMap { action ->
                deleteToDo.deleteToDo(action.deletedItem)
                    .subscribeOn(Schedulers.io())
                    .andThen(
                        Observable.defer {
                            Observable.just(
                                ListToDoAction.DeleteItemSuccess(
                                    action.deletedItem,
                                    action.deletedItemPosition
                                )
                            )
                        }
                    )
            }
    }

    @VisibleForTesting
    val restoreDeletedItemSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, state ->
        actions.ofType(ListToDoAction.RestoreDeletedItem::class.java)
            .switchMap {
                state().restoredData?.let { restoredData ->
                    addToDo.addToDo(restoredData.restoredItem)
                        .subscribeOn(Schedulers.io())
                        .andThen(
                            Observable.defer {
                                Observable.just(ListToDoAction.RestoreDeletedItemSuccess)
                            }
                        )
                } ?: Observable.empty() // if need to handle restore failed case, just emit restore fail action
            }
    }

    @VisibleForTesting
    val deleteAllToDoListSideEffect: SideEffect<ListToDoState, ListToDoAction> = { actions, _ ->
        actions.ofType(ListToDoAction.DeleteAll::class.java)
            .switchMap {
                deleteToDo.deleteAllToDos()
                    .subscribeOn(Schedulers.io())
                    .andThen(
                        Observable.defer {
                            Observable.just(ListToDoAction.DeleteAllSuccess)
                        }
                    )
            }
    }

    override val initialState = ListToDoState()

    override fun sideEffects(): List<SideEffect<ListToDoState, ListToDoAction>> =
        listOf(
            navigationSideEffect,
            openScreenSideEffect,
            loadToDoListSideEffect,
            searchToDoListSideEffect,
            sortToDoListSideEffect,
            deleteToDoItemSideEffect,
            deleteAllToDoListSideEffect,
            restoreDeletedItemSideEffect
        )

    override fun reducer(state: ListToDoState, action: ListToDoAction): ListToDoState {
        return when (action) {

            is ListToDoAction.ToDoListLoaded -> state.copy(items = action.todosList)

            is ListToDoAction.DeleteItemSuccess -> state.copy(items = state.items.filter {
                it.id != action.deletedItem.id
            }, restoredData = RestoredToDoItem(action.deletedItemPosition,action.deletedItem))

            is ListToDoAction.DeleteAllSuccess -> state.copy(items = listOf())

            is ListToDoAction.RestoreDeletedItemSuccess -> {
                val dataList = mutableListOf<ToDoItem>()
                dataList.addAll(state.items)
                state.restoredData?.let { restoredData ->
                    if (restoredData.restoredPosition != -1 && restoredData.restoredPosition <= state.items.size)
                        dataList.add(restoredData.restoredPosition, restoredData.restoredItem)
                    else dataList.add(restoredData.restoredItem)
                }
                // update list to-do items and clear restored data
                state.copy(items = dataList,restoredData = null)
            }

            is ListToDoAction.ClearRestoredData -> state.copy(restoredData = null)

            else -> state
        }
    }


}