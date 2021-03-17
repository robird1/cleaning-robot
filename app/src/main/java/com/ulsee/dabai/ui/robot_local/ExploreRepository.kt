package com.ulsee.dabai.ui.robot_local

import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotDataSource
import com.ulsee.dabai.data.response.CreateMapResponse
import com.ulsee.dabai.data.response.LoadDynamicMapResponse


class ExploreRepository(val dataSource: RobotDataSource) {

    suspend fun loadDynamicMap(time: Long): Result<LoadDynamicMapResponse> {
        val result = dataSource.loadDynamicMap(time)
        return result
    }

    suspend fun exitCreateMap(mapName: String, floor: String): Result<CreateMapResponse> {
        val result = dataSource.createMap(mapName, floor, 2)
        return result
    }
}