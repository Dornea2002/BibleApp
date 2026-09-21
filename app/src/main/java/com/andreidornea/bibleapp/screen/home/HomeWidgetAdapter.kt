package com.andreidornea.bibleapp.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.vector.Path
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andreidornea.bibleapp.R
import com.andreidornea.bibleapp.databinding.DailyVerseCardBinding
import com.andreidornea.bibleapp.databinding.YoutubeMetadataCardBinding
import com.andreidornea.bibleapp.model.widget.HomeWidget
import com.andreidornea.bibleapp.ui.widget.YoutubeMetadataWidget
import com.andreidornea.bibleapp.utils.WidgetType
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

class HomeWidgetAdapter(private val fragment: HomeFragment) :
    ListAdapter<HomeWidget, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_DAILY_VERSE = 0
        private const val TYPE_YOUTUBE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeWidget.DailyVerseMetadata -> TYPE_DAILY_VERSE
            is HomeWidget.YoutubeFeedMetadata -> TYPE_YOUTUBE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_DAILY_VERSE -> {
                val binding = DailyVerseCardBinding.inflate(
                    inflater, parent, false
                )
                DailyVerseViewHolder(binding)
            }

            TYPE_YOUTUBE -> {
                val binding = YoutubeMetadataCardBinding.inflate(
                    inflater, parent, false
                )
                YoutubeMetadataViewHolder(fragment, binding)
            }

            else -> error("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (val item = getItem(position)) {
            is HomeWidget.DailyVerseMetadata -> {
                (holder as DailyVerseViewHolder).bind(item)
            }

            is HomeWidget.YoutubeFeedMetadata -> {
                (holder as YoutubeMetadataViewHolder).bind(item)
            }
        }
    }

    class DailyVerseViewHolder(
        private val binding: DailyVerseCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(widget: HomeWidget.DailyVerseMetadata) {
            binding.dailyVerseText.text =
                widget.dailyVerse.verse.details.text
            binding.dailyVerseReference.text =
                widget.dailyVerse.verse.details.reference
        }
    }

    class YoutubeMetadataViewHolder(
        private val fragment: Fragment,
        private val binding: YoutubeMetadataCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(widget: HomeWidget.YoutubeFeedMetadata) {
            YoutubeMetadataWidget.bind(
                fragment = fragment,
                binding = binding,
                video = widget.metadata,
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeWidget>() {
        override fun areItemsTheSame(oldItem: HomeWidget, newItem: HomeWidget): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HomeWidget, newItem: HomeWidget): Boolean {
            return oldItem == newItem
        }
    }
}