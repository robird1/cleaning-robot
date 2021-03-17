package com.ulsee.dabai.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.TaskCloudRepository
import kotlinx.coroutines.launch


class TaskListViewModel(private val repository: TaskCloudRepository) : ViewModel() {

    private val _taskListResult = MutableLiveData<TaskListResult>()
    val taskListResult: LiveData<TaskListResult> = _taskListResult

    fun getList(projectID: Int) {
        viewModelScope.launch {
            val result = repository.getList(projectID)

            if (result is Result.Success) {
                _taskListResult.value = TaskListResult(success = result.data.data.tasks)
            } else {
                _taskListResult.value = TaskListResult(error = R.string.get_task_list_failed)
            }
        }
    }

    private val _executeTaskResult = MutableLiveData<ExecuteTaskResult>()
    val executeTaskResult: LiveData<ExecuteTaskResult> = _executeTaskResult
    fun execute(projectID: Int, taskID: Int) {
        viewModelScope.launch {
            val result = repository.executeTask(projectID, taskID)

            if (result is Result.Success) {
                _executeTaskResult.value = ExecuteTaskResult(success = true)
            } else {
                _executeTaskResult.value = ExecuteTaskResult(success = false, error = R.string.execute_task_failed)
            }
        }
    }
}