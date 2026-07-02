package com.andreidornea.bibleapp.api

import com.andreidornea.bibleapp.utils.BIBLE_BASE_URL
import com.andreidornea.bibleapp.utils.DAILY_VERSE_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{

    val retrofitBible: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BIBLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitDailyVerse: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DAILY_VERSE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val bibleApiService: BibleApiService by lazy {
        RetrofitClient.retrofitBible.create(BibleApiService::class.java)
    }

    val dailyVerseApiService: DailyVerseApiService by lazy {
        RetrofitClient.retrofitDailyVerse.create(DailyVerseApiService::class.java)
    }
}