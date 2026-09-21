package com.andreidornea.bibleapp.repository

import com.andreidornea.bibleapp.BuildConfig
import com.andreidornea.bibleapp.api.service.YoutubeMetadataApiService
import com.andreidornea.bibleapp.model.widget.YoutubeMetadata

class YoutubeMetadataRepository(
    private val api: YoutubeMetadataApiService
) {
    suspend fun getMetadata(
        metadataID: String,
        categoryId: String
    ): YoutubeMetadata {
        val response = api.metadataID(
            metadataID = metadataID,
            apiKey = BuildConfig.YOUTUBE_API_KEY
        )

        val item = response.items.firstOrNull()
            ?: throw IllegalStateException("Youtube metadata not found")

        val snippet = item.snippet

        return YoutubeMetadata(
            id = metadataID,
            title = snippet.title,
            channel = snippet.channelTitle,
            categoryId = categoryId
        )
    }
}