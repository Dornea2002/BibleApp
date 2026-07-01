package com.andreidornea.bibleapp.ui.widget

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.andreidornea.bibleapp.databinding.YoutubeVideoCardBinding
import com.andreidornea.bibleapp.model.YoutubeVideo
import com.bumptech.glide.Glide

object YoutubeVideoWidget {
    fun bind(
        fragment: Fragment,
        binding: YoutubeVideoCardBinding,
        video: YoutubeVideo
        ){
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
                "https://www.youtube.com/watch?v=\${video.id}".toUri()
            )

            try {
                fragment.startActivity(appIntent)
            } catch (e: Exception){
                fragment.startActivity(webIntent)
            }
        }
    }
}