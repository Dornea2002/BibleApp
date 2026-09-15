package com.andreidornea.bibleapp.api.service

import com.andreidornea.bibleapp.model.widget.DailyVerse
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyVerseApiService {
    @GET("api/v1/get")
    suspend fun getDailyVerse(
        @Query("format") format: String = "json",
        @Query("order") order: String = "daily"
    ): DailyVerse
}