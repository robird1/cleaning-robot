package com.ulsee.dabai.data

import com.ulsee.dabai.data.response.LoginResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var token: String? = null
        private set

    var projectID: Int? = null
        private set

    val isLoggedIn: Boolean
        get() = token != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        token = null
        projectID = null
    }

    suspend fun logout(): Result<LoginResponse> {
        token = null
        projectID = null
        return dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUserInfo(result.data)
        }

        return result
    }

    private fun setLoggedInUserInfo(info: LoginResponse) {
        this.token = info.data.token
        this.projectID = info.data.projects.firstOrNull()?.project_id
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}