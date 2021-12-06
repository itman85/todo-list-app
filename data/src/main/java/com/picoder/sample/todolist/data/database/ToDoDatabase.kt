package com.picoder.sample.todolist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.picoder.sample.todolist.data.database.converters.PriorityConverter
import com.picoder.sample.todolist.data.database.dao.ToDoDao
import com.picoder.sample.todolist.data.database.entity.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(PriorityConverter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

}