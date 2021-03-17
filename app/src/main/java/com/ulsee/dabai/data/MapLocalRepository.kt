package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.data.response.MapListResponse

class MapLocalRepository(val dataSource: MapLocalDataSource) {

    var list: List<Map>? = null
        private set
    init {
        list = null
    }

    suspend fun getList(): Result<MapListResponse> {
        val result = dataSource.getList()

        if (result is Result.Success) {
            setList(result.data.data)
        }

        return result
    }

    private fun setList(list: List<Map>) {
        this.list = list
    }
}