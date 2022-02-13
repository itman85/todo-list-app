package com.picoder.sample.todolist.features.add.redux

import android.util.Log
import com.freeletics.coredux.*
import com.picoder.sample.todolist.domain.usecase.AddToDo
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.model.toEntity
import com.picoder.sample.todolist.utils.validateDataFromInput
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import javax.inject.Inject

class AddToDoStateMachine @Inject constructor(private val addToDo: AddToDo) {
    companion object {
        const val TAG = "AddToDoStateMachine"
    }

    val navigation: Channel<AddToDoNavigation> = Channel(Channel.UNLIMITED)

    private val navigationSideEffect = object : SideEffect<AddToDoState, AddToDoAction> {
        override val name: String = "Add Todo Navigation"

        override fun CoroutineScope.start(
            input: ReceiveChannel<AddToDoAction>,
            stateAccessor: StateAccessor<AddToDoState>,
            output: SendChannel<AddToDoAction>,
            logger: SideEffectLogger
        ): Job = launch(context = CoroutineName(name)) {
            for (inputAction in input) {
                when (inputAction) {
                    is AddToDoAction.SaveDataSuccess -> navigation.send(
                        AddToDoNavigation.ShowMessageAndBackToList(
                            "Add To-Do Successfully"
                        )
                    )
                    is AddToDoAction.ValidateDataFail -> navigation.send(
                        AddToDoNavigation.ShowErrorMessage(inputAction.errorMsg)
                    )
                    else -> Log.d(TAG, "Not support navigation for action $inputAction")
                }
            }
        }
    }

    private val addToDoSideEffect = object : SideEffect<AddToDoState, AddToDoAction> {
        override val name: String = "Add Todo"

        override fun CoroutineScope.start(
            input: ReceiveChannel<AddToDoAction>,
            stateAccessor: StateAccessor<AddToDoState>,
            output: SendChannel<AddToDoAction>,
            logger: SideEffectLogger
        ): Job = launch(context = CoroutineName(name)) {
            for (inputAction in input) {
                logger.logSideEffectEvent {
                    LogEvent.SideEffectEvent.InputAction(
                        name,
                        inputAction
                    )
                }
                if (inputAction is AddToDoAction.AddToDoItem) {
                    val validation =
                        validateDataFromInput(inputAction.title, inputAction.description)
                    val outputAction = if (validation)
                        AddToDoAction.SaveData(
                            ToDoItem(
                                0,
                                inputAction.title,
                                inputAction.priority,
                                inputAction.description
                            )
                        )
                    else
                        AddToDoAction.ValidateDataFail("Please input all fields!")
                    //
                    logger.logSideEffectEvent {
                        LogEvent.SideEffectEvent.DispatchingToReducer(
                            name,
                            outputAction
                        )
                    }
                    output.send(outputAction)
                }
            }
        }
    }

    private val saveToDoSideEffect = object : SideEffect<AddToDoState, AddToDoAction> {
        override val name: String = "Save ToDo"

        override fun CoroutineScope.start(
            input: ReceiveChannel<AddToDoAction>,
            stateAccessor: StateAccessor<AddToDoState>,
            output: SendChannel<AddToDoAction>,
            logger: SideEffectLogger
        ): Job = launch(context = CoroutineName(name) + Dispatchers.IO) {
            for (inputAction in input) {
                logger.logSideEffectEvent {
                    LogEvent.SideEffectEvent.InputAction(
                        name,
                        inputAction
                    )
                }
                if (inputAction is AddToDoAction.SaveData) {
                    // access db need to run in thread so must add Dispatchers.IO for this coroutine context
                    addToDo.addToDoSuspend(inputAction.item.toEntity())
                    output.send(AddToDoAction.SaveDataSuccess)
                }
            }
        }

    }

    fun create(coroutineScope: CoroutineScope): Store<AddToDoState, AddToDoAction> = coroutineScope
        .createStore(
            name = "Add ToDo State Machine",
            initialState = AddToDoState(),
            logSinks = emptyList(),
            sideEffects = listOf(navigationSideEffect, addToDoSideEffect, saveToDoSideEffect),
            reducer = ::reducer
        )


    /**
     * The state reducer.
     * Takes Actions and the current state to calculate the new state.
     */
    private fun reducer(state: AddToDoState, action: AddToDoAction): AddToDoState {
        return state
    }
}