package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.EmptyResponse
import com.ulsee.dabai.data.response.TaskListResponse

class TaskCloudDataSource(val url: String) {

    suspend fun getList(projectID: Int): Result<TaskListResponse> {
        return try {
            val response = ApiService.create(url).getTaskList(projectID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun executeTask(projectID: Int, taskID: Int): Result<EmptyResponse> {
        return try {
            val response = ApiService.create(url).executeTask(projectID, taskID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}