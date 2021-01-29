package com.ulsee.dabai.data.response

data class TaskListResponse(
    val data: TaskListResponseData
)

data class TaskListResponseData(
        val tasks: List<Task>,
        val total: Int
)

data class Task (
    val id: Int,
    val name: String,
    val type: Int,
    val project_id: Int,
    val robot_id: Int,
    val robot_mark: String,
//    val timings: Int,
    val level: Int,
    val on_off: Int,
    val days: String,
    val uuid: String,
    val created_at: String,
    val updated_at: String,
    val project_name: String,
    val project_address: String,
    val sub_tasks: List<SubTask>,
)

data class SubTask(
    val type: Int,
    val extra: String,
    val value: String,
    val map_id: Int,
)