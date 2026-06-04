package com.andreidornea.bibleapp.model

data class AvailableTranslations (
    val translations: List<Translation>
)

data class Translation(
    val id: String,
    val name: String,
    val englishName: String,
    val website: String,
    val licenseUrl: String,
    val  shortName: String,
    val language: String,
    val  languageName: String?,
    val languageEnglishName: String?,
    val textDirection: TextDirection,
    val availableFormats: List<Format>,
    val  listOfBooksApiLink: String,
    val numberOfBooks: Int,
    val totalNumberOfChapters: Int,
    val totalNumberOfVerses: Int,
    val numberOfApocryphalBooks: Int?,
    val totalNumberOfApocryphalChapters: Int?,
    val totalNumberOfApocryphalVerses: Int?
)

enum class TextDirection{
    ltr,
    rtl
}

enum class Format{
    json,
    usfm
}