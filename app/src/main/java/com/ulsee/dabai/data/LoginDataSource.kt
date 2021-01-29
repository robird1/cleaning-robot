package com.ulsee.dabai.data

import com.ulsee.dabai.data.request.LoginRequest
import com.ulsee.dabai.data.response.LoginResponse

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(val url: String) {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = ApiService.create(url).login(LoginRequest(username, password))
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun logout(): Result<LoginResponse> {
        return try {
            val response = ApiService.create(url).logout()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}