package com.andreidornea.bibleapp.repository

import com.andreidornea.bibleapp.api.DailyVerseApiService
import com.andreidornea.bibleapp.model.widget.DailyVerse

class DailyVerseRepository(
    private val api: DailyVerseApiService
) {
    suspend fun getDailyVerse(): DailyVerse{
        return api.getDailyVerse()
    }
}