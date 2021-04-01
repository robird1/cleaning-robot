package com.ulsee.dabai.data

import com.ulsee.dabai.data.request.PositioningRequest
import com.ulsee.dabai.data.request.UploadMapRequest
import com.ulsee.dabai.data.response.EmptyResponse
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.data.response.MapListResponse
import okhttp3.ResponseBody

class MapCloudRepository(val dataSource: MapCloudDataSource) {

    var list: List<Map>? = null
        private set
    init {
        list = null
    }

    suspend fun getProjectMapList(projectID: Int): Result<MapListResponse> {
        val result = dataSource.getProjectMapList(projectID)

        if (result is Result.Success) {
            setList(result.data.data)
        }

        return result
    }

    suspend fun getRobotMapList(projectID: Int, robotID: Int): Result<MapListResponse> {
        val result = dataSource.getRobotMapList(projectID, robotID)
        return result
    }

    private fun setList(list: List<Map>) {
        this.list = list
    }

    suspend fun upload(projectID: Int, mapID: Int, payload: UploadMapRequest): Result<EmptyResponse> {
        val result = dataSource.upload(projectID, mapID, payload)
        return result
    }

    suspend fun getMapImage(projectID: Int, mapID: Int): Result<ResponseBody> {
        val result = dataSource.getMapImage(projectID, mapID)
        return result
    }
}