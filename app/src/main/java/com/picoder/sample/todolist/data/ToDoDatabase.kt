package com.picoder.sample.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.picoder.sample.todolist.data.model.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(PriorityConverter::class)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun toDoDao():ToDoDao

    companion object{

        @Volatile // make this variable visible to all threads immediately
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context):ToDoDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}