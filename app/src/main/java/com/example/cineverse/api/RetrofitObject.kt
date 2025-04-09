package com.example.cineverse.api

import android.content.Context
import com.example.cineverse.api.Constant.BASE_URL
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitObject {
    private var httpClient: OkHttpClient? = null

    fun initCache(context: Context) {
        httpClient = OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http_cache"), 10 * 1024 * 1024)) // 10 MB cache
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Cache-Control", "public, max-age=60") // Cache for 60 seconds
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    fun clearCache() {
        httpClient?.cache?.evictAll()
    }

    val api: MovieApiService by lazy {
        val client = httpClient ?: OkHttpClient.Builder().build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}