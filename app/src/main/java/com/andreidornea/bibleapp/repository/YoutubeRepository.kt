package com.andreidornea.bibleapp.repository

import com.andreidornea.bibleapp.api.service.YoutubeApiService
import com.andreidornea.bibleapp.model.widget.YoutubeVideo
import com.andreidornea.bibleapp.BuildConfig

class YoutubeRepository(
    private val api: YoutubeApiService
) {
    suspend fun getVideo(videoID: String): YoutubeVideo{
        val response = api.getVideo(
            videoId = videoID,
            apiKey = BuildConfig.YOUTUBE_API_KEY
        )

        val item = response.items.firstOrNull()
            ?: throw IllegalStateException("Youtube video not found")

        val snippet = item.snippet

        return YoutubeVideo(
            id = videoID,
            title = snippet.title,
            channel = snippet.channelTitle
        )
    }
}