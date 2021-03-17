package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.MapListResponse

class MapLocalDataSource(val url: String) {

    suspend fun getList(): Result<MapListResponse> {
        return try {
            val response = ApiService.create(url).getMapList()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}