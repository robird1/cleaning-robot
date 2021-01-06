package com.ulsee.dabai.data.response

data class LoginResponse(
    val data: LoginResponseData
)
data class LoginResponseData(
    val token: String,
    val projects: List<LoginResponseDataProject>
)
data class LoginResponseDataProject(
    val project_id: Int,
    val project_name: String
)