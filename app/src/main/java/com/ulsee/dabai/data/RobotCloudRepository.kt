package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.Robot
import com.ulsee.dabai.data.response.RobotListResponse

class RobotCloudRepository(val dataSource: RobotCloudDataSource) {

    var list: List<Robot>? = null
        private set
    init {
        list = null
    }

    suspend fun getList(projectID: Int): Result<RobotListResponse> {
        // handle login
        val result = dataSource.getList(projectID)

        if (result is Result.Success) {
            setList(result.data.data)
        }

        return result
    }

    private fun setList(list: List<Robot>) {
        this.list = list
    }
}