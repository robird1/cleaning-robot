package com.ulsee.dabai.data.response

data class RobotListResponse(
        val data: List<Robot>
)
data class Robot (
        val robot_id: Int,
        val robot_mark: String,
        val connected: Boolean,
        val version: String,

)