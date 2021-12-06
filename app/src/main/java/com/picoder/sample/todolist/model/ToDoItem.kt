package com.picoder.sample.todolist.model

import android.os.Parcelable
import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.domain.entity.ToDoEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoItem(
    override val id: Int,
    override val title: String,
    override val priority: Priority,
    override val description: String
) : ToDoEntity, Parcelable