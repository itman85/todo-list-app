package com.picoder.sample.todolist.data.database.dao

import androidx.room.*
import com.picoder.sample.todolist.data.database.entity.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM TODO_TABLE ORDER BY id ASC")
    fun getAllData(): List<ToDoData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("DELETE FROM TODO_TABLE")
    suspend fun deleteAll()

    @Query("SELECT * FROM TODO_TABLE WHERE title LIKE :searchQuery")
    fun searchData(searchQuery: String): List<ToDoData>

    @Query("SELECT * FROM TODO_TABLE ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): List<ToDoData>

    @Query("SELECT * FROM TODO_TABLE ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): List<ToDoData>
}