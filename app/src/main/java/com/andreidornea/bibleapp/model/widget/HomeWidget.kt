package com.andreidornea.bibleapp.model.widget

import com.andreidornea.bibleapp.utils.WidgetType

sealed interface HomeWidget {
    data class DailyVerseMetadata(
        val dailyVerse: DailyVerse
    ): HomeWidget

    data class YoutubeFeedMetadata(
        val type: WidgetType,
        val metadata: YoutubeMetadata
    ) : HomeWidget
}
