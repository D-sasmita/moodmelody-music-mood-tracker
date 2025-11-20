
package com.marwadiuniversity.moodmelody.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // The base URL for all YouTube Data API v3 requests
    private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/"

    // Create a custom Gson instance with our safe adapter
    private val gson = GsonBuilder()
        .registerTypeAdapter(YoutubeSearchResponse::class.java, SafeYoutubeResponseAdapter())
        .create()

    // Create a logging interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create an OkHttpClient with the logger
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Create the Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(YOUTUBE_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // Lazily create the YouTube API service
    val youtubeApiService: YoutubeApiService by lazy {
        retrofit.create(YoutubeApiService::class.java)
    }
}
