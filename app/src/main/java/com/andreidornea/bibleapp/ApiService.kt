package com.andreidornea.bibleapp

import com.andreidornea.bibleapp.model.bible.AvailableTranslations
import com.andreidornea.bibleapp.model.bible.Chapter
import com.andreidornea.bibleapp.model.bible.TranslationBooks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService{
    @GET("api/available_translations.json")
    fun getAvailableTranslations(): Call<AvailableTranslations>

    @GET("api/{translation}/books.json")
    fun getBookListByTranslation(@Path("translation") translation: String): Call<TranslationBooks>

    @GET("api/{translation}/{book}/{chapter}.json")
    fun getChapterByBookTranslation(
        @Path("translation") translation: String,
        @Path("book") book: String,
        @Path("chapter") chapter: String,
        ): Call<Chapter>
}