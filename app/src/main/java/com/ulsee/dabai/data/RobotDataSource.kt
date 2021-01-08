package com.ulsee.dabai.data

import android.util.Log
import com.ulsee.dabai.data.request.CreateMapRequest
import com.ulsee.dabai.data.response.CreateMapResponse
import com.ulsee.dabai.data.response.LoadDynamicMapResponse

private val TAG = RobotDataSource::class.java.simpleName

private const val url = "http://192.168.10.5:9980"

class RobotDataSource {

    suspend fun createMap(mapName: String, floor: String, command: Int = 1): Result<CreateMapResponse> {
        return try {
            val response = ApiService.create(url).createMap(CreateMapRequest(mapName, floor.toInt(), command))
            Result.Success(response)
        } catch (e: Exception) {
            Log.d(TAG, "e.message: ${e.message}")
            Result.Error(e)
        }
    }

    suspend fun loadDynamicMap(time: Long): Result<LoadDynamicMapResponse> {
        return try {
            val response = ApiService.create(url).loadDynamicMap(time)
            Result.Success(response)
        } catch (e: Exception) {
            Log.d(TAG, "e.message: ${e.message}")
            Result.Error(e)
        }
    }
}