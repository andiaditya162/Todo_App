package com.dityapra.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface TaskDao {
    @RawQuery(observedEntities = [Task::class])
    fun getTasks(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * from tasks where completed = 0 Order by dueDate asc LIMIT 1")
    fun getNearestActiveTask(): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("Update tasks set completed = :completed where id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)
}