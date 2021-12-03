package com.picoder.sample.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.picoder.sample.todolist.data.model.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM TODO_TABLE ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>> // use live data here, any change on data make this query change result then it will emit data to observer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("DELETE FROM TODO_TABLE")
    suspend fun deleteAll()

    @Query("SELECT * FROM TODO_TABLE WHERE title LIKE :searchQuery")
    fun searchData(searchQuery: String): LiveData<List<ToDoData>> // use live data here, any change on data make this result of this query change then it will emit result of query to observer

    @Query("SELECT * FROM TODO_TABLE ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<ToDoData>>

    @Query("SELECT * FROM TODO_TABLE ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<ToDoData>>
}