package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.MapListResponse

class MapCloudDataSource(val url: String) {

    suspend fun getList(projectID: Int): Result<MapListResponse> {
        return try {
            val response = ApiService.create(url).getMapList(projectID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}