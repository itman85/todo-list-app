package com.picoder.sample.todolist.data.repositories

import com.picoder.sample.todolist.data.database.dao.ToDoDao
import com.picoder.sample.todolist.data.database.entity.ToDoData
import com.picoder.sample.todolist.domain.entity.ToDoEntity
import com.picoder.sample.todolist.domain.repository.ToDoRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(private val toDoDao: ToDoDao) : ToDoRepository {

    override fun getAllToDoList(): Single<List<ToDoEntity>> {
        return Single.fromCallable { toDoDao.getAllData() }
    }

    override fun addToDo(toDoEntity: ToDoEntity): Completable {
        return Completable.fromAction {
            toDoDao.insertData(
                ToDoData(
                    toDoEntity.id,
                    toDoEntity.title,
                    toDoEntity.priority,
                    toDoEntity.description
                )
            )
        }
    }

    override suspend fun addToDoSuspend(toDoEntity: ToDoEntity) {
        toDoDao.insertData(
            ToDoData(
                toDoEntity.id,
                toDoEntity.title,
                toDoEntity.priority,
                toDoEntity.description
            )
        )
    }

    override fun updateToDo(toDoEntity: ToDoEntity): Completable {
        return Completable.fromAction {
            toDoDao.updateData(
                ToDoData(
                    toDoEntity.id,
                    toDoEntity.title,
                    toDoEntity.priority,
                    toDoEntity.description
                )
            )
        }
    }

    override fun deleteToDo(toDoEntity: ToDoEntity): Completable {
        return Completable.fromAction {
            toDoDao.deleteItem(
                ToDoData(
                    toDoEntity.id,
                    toDoEntity.title,
                    toDoEntity.priority,
                    toDoEntity.description
                )
            )
        }
    }

    override fun deleteAllToDoList(): Completable {
        return Completable.fromAction {
            toDoDao.deleteAll()
        }
    }

    override fun searchToDos(keywords: String): Single<List<ToDoEntity>> {
        return Single.fromCallable { toDoDao.searchData("%$keywords%") }
    }

    override fun sortByHighPriority(): Single<List<ToDoEntity>> {
        return Single.fromCallable { toDoDao.sortByHighPriority() }
    }

    override fun sortByLowPriority(): Single<List<ToDoEntity>> {
        return Single.fromCallable { toDoDao.sortByLowPriority() }
    }
}