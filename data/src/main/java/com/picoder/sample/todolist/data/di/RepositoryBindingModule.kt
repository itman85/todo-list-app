package com.picoder.sample.todolist.data.di

import com.picoder.sample.todolist.data.repositories.ToDoRepositoryImpl
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindingModule {

    @Binds
    abstract fun bindToDoRepository(impl: ToDoRepositoryImpl): ToDoRepository
}