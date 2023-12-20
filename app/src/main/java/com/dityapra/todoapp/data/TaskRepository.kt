package com.dityapra.todoapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dityapra.todoapp.utils.FilterUtils
import com.dityapra.todoapp.utils.TasksFilterType

class TaskRepository(private val tasksDao: TaskDao) {

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TaskDatabase.getInstance(context)
                    instance = TaskRepository(database.taskDao())
                }
                instance as TaskRepository
            }
        }
    }

    fun getTasks(filter: TasksFilterType): LiveData<PagedList<Task>> {
        val query = FilterUtils.getFilteredQuery(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDERS)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(tasksDao.getTasks(query), config).build()
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return tasksDao.getTaskById(taskId)
    }

    fun getNearestActiveTask(): Task {
        return tasksDao.getNearestActiveTask()
    }

    suspend fun insertTask(newTask: Task): Long {
        return tasksDao.insertTask(newTask)
    }

    suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task)
    }

    suspend fun completeTask(task: Task, isCompleted: Boolean) {
        tasksDao.updateCompleted(task.id, isCompleted)
    }
}