package com.marwadiuniversity.moodmelody.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Defines the YouTube API endpoints we will use with Retrofit
interface YoutubeApiService {
    @GET("youtube/v3/search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("videoCategoryId") categoryId: String = "10", // "10" is the ID for the "Music" category
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String
    ): Response<YoutubeSearchResponse>
}
