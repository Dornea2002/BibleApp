package com.andreidornea.bibleapp.ui.widget

import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.andreidornea.bibleapp.R
import com.andreidornea.bibleapp.databinding.YoutubeMetadataCardBinding
import com.andreidornea.bibleapp.model.widget.YoutubeMetadata
import com.andreidornea.bibleapp.utils.WidgetType
import com.bumptech.glide.Glide

object YoutubeMetadataWidget {
    val TAG = YoutubeMetadataWidget.javaClass.canonicalName
    fun bind(
        fragment: Fragment,
        binding: YoutubeMetadataCardBinding,
        video: YoutubeMetadata
    ) {
        Log.i(TAG, "Binding metadata ${video.title}")
        binding.cardTitle.setText(
            when (video.categoryId) {
                WidgetType.MUSIC.id -> R.string.music_card_title
                WidgetType.VIDEO.id -> 0
                WidgetType.SERMON.id -> R.string.sermon_card_title
                WidgetType.PODCAST.id -> 0
                else -> 0
            }
        )
        binding.videoTitle.text = video.title
        binding.channelName.text = video.channel

        Glide.with(fragment)
            .load("https://img.youtube.com/vi/${video.id}/hqdefault.jpg")
            .into(binding.videoThumbnail)

        binding.videoCard.setOnClickListener {
            val appIntent = Intent(
                Intent.ACTION_VIEW,
                "vnd.youtube:${video.id}".toUri()
            )

            val webIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.youtube.com/watch?v=${video.id}".toUri()
            )

            try {
                fragment.startActivity(appIntent)
            } catch (e: Exception) {
                fragment.startActivity(webIntent)
            }
        }
    }
}