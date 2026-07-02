package com.andreidornea.bibleapp.model.widget

data class DailyVerse (
    val verse: VerseData
    )

data class VerseData (
    val details: VerseDetails,
    val notice: String
)

data class VerseDetails (
    val text: String,
    val reference: String,
    val version: String,
    val verseurl: String
)
