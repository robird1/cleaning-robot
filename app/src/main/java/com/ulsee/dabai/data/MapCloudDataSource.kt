package com.ulsee.dabai.data

import com.ulsee.dabai.data.request.PositioningRequest
import com.ulsee.dabai.data.request.UploadMapRequest
import com.ulsee.dabai.data.response.EmptyResponse
import com.ulsee.dabai.data.response.MapListResponse
import okhttp3.ResponseBody

class MapCloudDataSource(val url: String) {

    suspend fun getProjectMapList(projectID: Int): Result<MapListResponse> {
        return try {
            val response = ApiService.create(url).getProjectMapList(projectID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getRobotMapList(projectID: Int, robotID: Int): Result<MapListResponse> {
        return try {
            val response = ApiService.create(url).getRobotMapList(projectID, robotID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun upload(projectID: Int, mapID: Int, payload: UploadMapRequest): Result<EmptyResponse> {
        return try {
            val response = ApiService.create(url).uploadMap(projectID, mapID, payload)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getMapImage(projectID: Int, mapID: Int): Result<ResponseBody> {
        return try {
            val response = ApiService.create(url).getMap(projectID, mapID)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}