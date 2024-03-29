package com.picoder.sample.todolist.data.database.converters

import androidx.room.TypeConverter
import com.picoder.sample.todolist.domain.entity.Priority

class PriorityConverter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}