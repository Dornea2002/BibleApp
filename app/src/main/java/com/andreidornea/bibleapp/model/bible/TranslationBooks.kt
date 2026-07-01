package com.andreidornea.bibleapp.model.bible

data class TranslationBooks(
    val books: List<Book>,
    val translation: Translation
)

data class Book(
    val id: String,
    val name: String,
    val commonName: String,
    val title: String?,
    val order: Int,
    val numberOfChapters: Int,
    val firstChapterNumber: Int,
    val firstChapterApiLink: String,
    val lastChapterNumber: Int,
    val lastChapterApiLink: String,
    val totalNumberOfVerses: Int,
    val isApocryphal: Boolean? = null
)