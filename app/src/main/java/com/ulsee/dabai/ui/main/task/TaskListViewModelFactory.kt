package com.ulsee.dabai.ui.main.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ulsee.dabai.data.TaskCloudDataSource
import com.ulsee.dabai.data.TaskCloudRepository


class TaskListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            return TaskListViewModel(
                repository = TaskCloudRepository(
                    dataSource = TaskCloudDataSource(
                        url = "https://120.78.217.167:5200/v1/"
                    )
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}