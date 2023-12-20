package com.dityapra.todoapp.ui.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dityapra.todoapp.R
import com.dityapra.todoapp.data.Task
import com.dityapra.todoapp.data.TaskRepository
import com.dityapra.todoapp.utils.Event
import com.dityapra.todoapp.utils.TasksFilterType
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _filter = MutableLiveData<TasksFilterType>()
    val tasks: LiveData<PagedList<Task>> = _filter.switchMap {
        taskRepository.getTasks(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
    init {
        _filter.value = TasksFilterType.ALL_TASKS
    }

    fun filter(filterType: TasksFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
        if (completed) {
            _snackbarText.value = Event(R.string.task_marked_complete)
        } else {
            _snackbarText.value = Event(R.string.task_marked_active)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}