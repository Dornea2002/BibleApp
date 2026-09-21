package com.andreidornea.bibleapp.model.widget

data class YoutubeResponse (
    val items: List<YoutubeItem>
)

data class YoutubeItem(
    val snippet: YoutubeSnippet
)

data class YoutubeSnippet(
    val title: String,
    val channelTitle: String
)