package com.ulsee.dabai.ui.main.robot

import com.ulsee.dabai.data.response.Robot

data class RobotListResult (
        val success: List<Robot>? = null,
        val error: Int? = null
)