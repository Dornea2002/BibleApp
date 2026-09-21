package com.andreidornea.bibleapp.api.service

import com.andreidornea.bibleapp.model.widget.YoutubeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeMetadataApiService {
    @GET("videos")
    suspend fun metadataID(
        @Query("part") part: String = "snippet",
        @Query("id") metadataID: String,
        @Query("key") apiKey: String
    ): YoutubeResponse
}