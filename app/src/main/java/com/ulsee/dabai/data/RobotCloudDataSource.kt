package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.RobotListResponse

class RobotCloudDataSource(val url: String) {

    suspend fun getList(projectID: Int): Result<RobotListResponse> {
        return try {
            val response = ApiService.create(url).getRobotList(projectID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}