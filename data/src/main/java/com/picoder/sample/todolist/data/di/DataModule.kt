package com.picoder.sample.todolist.data.di

import android.content.Context
import androidx.room.Room
import com.picoder.sample.todolist.data.database.ToDoDatabase
import com.picoder.sample.todolist.data.database.dao.ToDoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ToDoDatabase {
        return Room.databaseBuilder(
            appContext,
            ToDoDatabase::class.java,
            "todo.db"
        ).build()
    }

    @Provides
    fun provideToDoDao(database: ToDoDatabase): ToDoDao {
        return database.toDoDao()
    }
}