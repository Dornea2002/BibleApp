package com.andreidornea.bibleapp.api.service

import com.andreidornea.bibleapp.model.bible.AvailableTranslations
import com.andreidornea.bibleapp.model.bible.Chapter
import com.andreidornea.bibleapp.model.bible.TranslationBooks
import com.andreidornea.bibleapp.model.widget.YoutubeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("videos")
    suspend fun getVideo(
        @Query("part") part: String = "snippet",
        @Query("id") videoId: String,
        @Query("key") apiKey: String
    ): YoutubeResponse
}