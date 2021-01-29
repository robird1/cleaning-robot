package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.data.response.MapListResponse

class MapCloudRepository(val dataSource: MapCloudDataSource) {

    var list: List<Map>? = null
        private set
    init {
        list = null
    }

    suspend fun getList(projectID: Int): Result<MapListResponse> {
        val result = dataSource.getList(projectID)

        if (result is Result.Success) {
            setList(result.data.data)
        }

        return result
    }

    private fun setList(list: List<Map>) {
        this.list = list
    }
}