package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.EmptyResponse
import com.ulsee.dabai.data.response.Task
import com.ulsee.dabai.data.response.TaskListResponse

class TaskCloudRepository(val dataSource: TaskCloudDataSource) {

    var list: List<Task>? = null
        private set
    init {
        list = null
    }

    suspend fun getList(projectID: Int): Result<TaskListResponse> {
        val result = dataSource.getList(projectID)

        if (result is Result.Success) {
            setList(result.data.data.tasks)
        }

        return result
    }

    suspend fun executeTask(projectID: Int, taskID: Int): Result<EmptyResponse> {
        val result = dataSource.executeTask(projectID, taskID)
        return result
    }

    private fun setList(list: List<Task>) {
        this.list = list
    }
}