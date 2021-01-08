package com.ulsee.dabai.ui.map

import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotDataSource
import com.ulsee.dabai.data.response.CreateMapResponse

class CreateMapRepository(val dataSource: RobotDataSource) {

    suspend fun createMap(mapName: String, floor: String): Result<CreateMapResponse> {
        val result = dataSource.createMap(mapName, floor)
        return result
    }

}