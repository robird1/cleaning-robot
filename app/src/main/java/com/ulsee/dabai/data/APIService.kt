package com.ulsee.dabai.data

import com.ulsee.dabai.data.request.CreateMapRequest
import com.ulsee.dabai.data.request.LoginRequest
import com.ulsee.dabai.data.response.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

//    @POST("https://clear.hallbot.com/cloud/login/check/phone")
    @POST("/v1/login")
    suspend fun login(@Body params: LoginRequest): LoginResponse

    @POST("/v1/signout")
    suspend fun logout(): LoginResponse

    @POST("/api/maps")
    suspend fun createMap(@Body params: CreateMapRequest): CreateMapResponse

    @GET("/tmp/00000000/00000000.png")
    suspend fun loadDynamicMap(@Query("t") time: Long): LoadDynamicMapResponse

    @GET("/v1/{projectID}/robots")
    suspend fun getRobotList(@Path("projectID") projectID: Int): RobotListResponse

    @GET("/v1/{projectID}/maps")
    suspend fun getMapList(@Path("projectID") projectID: Int): MapListResponse

    @GET("/v1/{projectID}/tasks")
    suspend fun getTaskList(@Path("projectID") projectID: Int): TaskListResponse

//todo test, modify response
    @POST("/v1/{projectID}/robots/{robotID}/location")
    suspend fun positioning(@Path("projectID") projectID: Int, @Path("robotID") robotID: Int): CreateMapResponse

//todo test, modify response
    @POST("/v1/{projectID}/tasks/{taskID}/send")
    suspend fun executeTask(@Path("projectID") projectID: Int, @Path("taskID") taskID: Int): CreateMapResponse

    companion object {
        var token: String? = null
        fun create(baseUrl: String): ApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .hostnameVerifier{ s, sslSession -> true } // 允許所有hostname，避免 server ssl 異常
                .addInterceptor(object: Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original: Request = chain.request()

                        val request: Request = original.newBuilder()
                            .header("token", token ?: "")
                            .method(original.method, original.body)
                            .build()
                        return chain.proceed(request)
                    }
                })
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
