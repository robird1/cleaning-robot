package com.ulsee.dabai.ui.main.task

import com.ulsee.dabai.data.response.Task

data class TaskListResult (
    val success: List<Task>? = null,
    val error: Int? = null
)
data class ExecuteTaskResult (
        val success: Boolean,
        val error: Int? = null
)