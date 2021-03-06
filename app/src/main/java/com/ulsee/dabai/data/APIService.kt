package com.ulsee.dabai.data

import com.ulsee.dabai.data.request.CreateMapRequest
import com.ulsee.dabai.data.request.LoginRequest
import com.ulsee.dabai.data.request.PositioningRequest
import com.ulsee.dabai.data.request.UploadMapRequest
import com.ulsee.dabai.data.response.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

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
    suspend fun getProjectMapList(@Path("projectID") projectID: Int): MapListResponse

    @GET("/v1/{projectID}/robots/{robotID}/maps")
    suspend fun getRobotMapList(@Path("projectID") projectID: Int, @Path("robotID") robotID: Int): MapListResponse

    @GET("/v1/{projectID}/tasks")
    suspend fun getTaskList(@Path("projectID") projectID: Int): TaskListResponse

    @POST("/v1/{projectID}/robots/{robotID}/location")
    suspend fun positioning(@Path("projectID") projectID: Int, @Path("robotID") robotID: Int, @Body params: PositioningRequest): EmptyResponse

    @POST("/v1/{projectID}/tasks/{taskID}/send")
    suspend fun executeTask(@Path("projectID") projectID: Int, @Path("taskID") taskID: Int): EmptyResponse

    @POST("/v1/{projectID}/maps/{mapID}/upload")
    suspend fun uploadMap(@Path("projectID") projectID: Int, @Path("mapID") mapID: Int, @Body params: UploadMapRequest): EmptyResponse

    // local get map list
    @GET("/api/maps")
    suspend fun getMapList(): MapListResponse

    @GET("/map/static/{projectID}/{mapID}/{mapID}.png")
    suspend fun getMap(@Path("projectID") projectID: Int,@Path("mapID") mapID: Int): ResponseBody

    companion object {
        var token: String? = null
        fun create(baseUrl: String): ApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val trustAllCerts: Array<X509TrustManager> = arrayOf<X509TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(chain: Array<X509Certificate>,
                                                        authType: String) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(chain: Array<X509Certificate>,
                                                        authType: String) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate?> {
                            return arrayOfNulls(0)
                        }
                    }
            )

            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()

            val client = OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier{ s, sslSession -> true } // ????????????hostname????????? server ssl ??????
                .addInterceptor(logger)
                .connectTimeout(60, TimeUnit.SECONDS)//??????????????????
                .readTimeout(60, TimeUnit.SECONDS)//??????????????????
                .writeTimeout(60, TimeUnit.SECONDS)//??????????????????
                .addInterceptor(object : Interceptor {
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
