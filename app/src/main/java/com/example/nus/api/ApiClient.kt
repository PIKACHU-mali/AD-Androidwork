package com.example.nus.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // 云端服务器地址
    private const val BASE_URL = "http://3.0.18.247:8080/"

    // 用户认证信息
    private var userCredentials: String? = null

    // 设置用户认证信息
    fun setUserCredentials(email: String, password: String) {
        val credentials = "$email:$password"
        userCredentials = android.util.Base64.encodeToString(credentials.toByteArray(), android.util.Base64.NO_WRAP)
        println("Set user credentials for: $email")
    }

    // 清除用户认证信息
    fun clearUserCredentials() {
        userCredentials = null
        println("Cleared user credentials")
    }
    
    // Cookie管理器，用于维持session
    private val cookieJar = object : CookieJar {
        private val cookieStore = mutableMapOf<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
            println("Saved cookies for ${url.host}: ${cookies.map { "${it.name}=${it.value}" }}")
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url.host] ?: emptyList()
            println("Loading cookies for ${url.host}: ${cookies.map { "${it.name}=${it.value}" }}")
            return cookies
        }
    }

    // 认证拦截器
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // 如果是登录请求，不添加认证头
        if (originalRequest.url.encodedPath.contains("/api/user/login") ||
            originalRequest.url.encodedPath.contains("/api/user/register")) {
            chain.proceed(originalRequest)
        } else {
            // 其他请求添加Basic Auth头
            val newRequest = if (userCredentials != null) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Basic $userCredentials")
                    .build()
            } else {
                originalRequest
            }
            chain.proceed(newRequest)
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)       // 添加认证拦截器
        .addInterceptor(loggingInterceptor)
        .cookieJar(cookieJar)  // 添加Cookie管理
        .connectTimeout(10, TimeUnit.SECONDS)  // 减少连接超时时间
        .readTimeout(15, TimeUnit.SECONDS)     // 减少读取超时时间
        .writeTimeout(15, TimeUnit.SECONDS)    // 减少写入超时时间
        .retryOnConnectionFailure(true)        // 启用连接失败重试
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val userApiService: UserApiService = retrofit.create(UserApiService::class.java)
    val journalApiService: JournalApiService = retrofit.create(JournalApiService::class.java)
    val habitsApiService: HabitsApiService = retrofit.create(HabitsApiService::class.java)
}
