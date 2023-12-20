package com.dityapra.todoapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dityapra.todoapp.data.TaskRepository
import com.dityapra.todoapp.ui.add.AddTaskViewModel
import com.dityapra.todoapp.ui.detail.DetailTaskViewModel
import com.dityapra.todoapp.ui.list.TaskViewModel

class ViewModelFactory private constructor(private val taskRepository: TaskRepository) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    TaskRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel(taskRepository) as T
            }
            modelClass.isAssignableFrom(DetailTaskViewModel::class.java) -> {
                DetailTaskViewModel(taskRepository) as T
            }
            modelClass.isAssignableFrom(AddTaskViewModel::class.java) -> {
                AddTaskViewModel(taskRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}
