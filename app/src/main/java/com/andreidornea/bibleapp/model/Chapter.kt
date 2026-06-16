package com.andreidornea.bibleapp.model

import com.google.gson.annotations.SerializedName

data class Chapter(
    val translation: Translation,
    val book: Book,
    val thisChapterLink: String,
    val nextChapterApiLink: String?,
    val previousChapterApiLink: String?,
    val numberOfVerses: Int?,
    val chapter: ChapterData
)

data class ChapterData(
    val number: Int,
    @SerializedName("content")
    val chapterContent: List<Map<String, Any>>,
//    val chapterContent: List<ChapterContent>,
    val footnotes: List<ChapterFootnote>
)

data class ChapterFootnote(
    val noteId: Int,
    val text: String,
    val caller: String?
)

sealed class ChapterContent {

    data class ChapterHeading(
        val type: String,
        val content: List<String>
    ) : ChapterContent()

    data class Verse(
        val type: String,
        val number: Int,
        val content: List<VerseContent>
    ) : ChapterContent()

    data class LineBreak(
        val type: String,
    ) : ChapterContent()
}

data class VerseContent(
    val text: String?,
    val poem: Int?,
    val noteId: Int?
)

