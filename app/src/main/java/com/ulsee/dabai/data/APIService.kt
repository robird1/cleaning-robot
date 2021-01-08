package com.ulsee.dabai.data

import com.ulsee.dabai.data.request.CreateMapRequest
import com.ulsee.dabai.data.request.LoginRequest
import com.ulsee.dabai.data.response.CreateMapResponse
import com.ulsee.dabai.data.response.LoadDynamicMapResponse
import com.ulsee.dabai.data.response.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

interface ApiService {

//    @POST("https://clear.hallbot.com/cloud/login/check/phone")
    @POST("https://120.78.217.167:5200/v1/login")
    suspend fun login(@Body params: LoginRequest): LoginResponse

    @POST("/api/maps")
    suspend fun createMap(@Body params: CreateMapRequest): CreateMapResponse

    @GET("/tmp/00000000/00000000.png")
    suspend fun loadDynamicMap(@Query("t") time: Long): LoadDynamicMapResponse


    companion object {
        fun create(baseUrl: String): ApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .hostnameVerifier{ s, sslSession -> true } // 允許所有hostname，避免 server ssl 異常
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
