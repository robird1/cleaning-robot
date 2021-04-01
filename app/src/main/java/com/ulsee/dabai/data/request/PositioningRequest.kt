package com.ulsee.dabai.data.request

data class PositioningRequest (
        val map_id: Int,
        val pose: PositioningRequestPose
)
data class PositioningRequestPose (
        val x: Double,
        val y: Double,
        val yaw: Int
)