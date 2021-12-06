package com.picoder.sample.todolist.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.domain.entity.ToDoEntity

@Entity(tableName = "todo_table")
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    override var id: Int,
    override var title: String,
    override var priority: Priority,
    override var description: String
) : ToDoEntity