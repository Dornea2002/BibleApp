package com.andreidornea.bibleapp.screen.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreidornea.bibleapp.api.ApiClient
import com.andreidornea.bibleapp.model.widget.HomeWidget
import com.andreidornea.bibleapp.repository.DailyVerseRepository
import com.andreidornea.bibleapp.repository.WidgetRepository
import com.andreidornea.bibleapp.repository.YoutubeMetadataRepository
import com.andreidornea.bibleapp.utils.WidgetType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    firestore: FirebaseFirestore
) : ViewModel() {
    private val dailyVerseRepository =
        DailyVerseRepository(ApiClient.dailyVerseApiService)
    private val widgetRepository =
        WidgetRepository(firestore)
    private val youtubeRepository =
        YoutubeMetadataRepository(ApiClient.youtubeApiService)
    private val _widgets =
        MutableStateFlow<List<HomeWidget>>(emptyList())
    val widgets =
        _widgets.asStateFlow()
    private val _loading =
        MutableStateFlow(false)
    val loading =
        _loading.asStateFlow()

    init {
        loadWidgets()
    }

    private fun loadWidgets() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val widgets = buildList {
                    try {
                        val dailyVerse = dailyVerseRepository.getDailyVerse()
                        Log.i(
                            TAG,
                            "Daily verse found: ${dailyVerse.verse.details.reference}"
                        )
                        add(
                            HomeWidget.DailyVerseMetadata(
                                dailyVerse = dailyVerse
                            )
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error loading daily verse", e)
                    }

                    try {
                        val metadataID = widgetRepository.getMetadataID(WidgetType.MUSIC.id, 1)
                        if (metadataID != null) {
                            val music =
                                youtubeRepository.getMetadata(metadataID, WidgetType.MUSIC.id)
                            Log.i(
                                TAG,
                                "Music metadata found: ${music.title}"
                            )
                            add(
                                HomeWidget.YoutubeFeedMetadata(
                                    type = WidgetType.MUSIC,
                                    metadata = music
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error loading music", e)
                    }

                    try {
                        val metadataID = widgetRepository.getMetadataID(WidgetType.SERMON.id, 2)
                        if (metadataID != null) {
                            val sermon =
                                youtubeRepository.getMetadata(metadataID, WidgetType.SERMON.id)
                            Log.i(
                                TAG,
                                "Sermon metadata found: ${sermon.title}"
                            )
                            add(
                                HomeWidget.YoutubeFeedMetadata(
                                    type = WidgetType.SERMON,
                                    metadata = sermon
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error loading sermon", e)
                    }
                }
                _widgets.value = widgets
            } finally {
                _loading.value = false
            }
        }
    }

    /*        private fun loadMusicVideo() {
                viewModelScope.launch {
                    _loading.value = true
                    try {
                        val metadataID = widgetRepository.getMetadataID(WidgetType.MUSIC.id, 1)
                        if (metadataID == null) {
                            Log.e(
                                TAG,
                                "No music ID found"
                            )
                            return@launch
                        }
                        val video = youtubeRepository.getMetadata(metadataID, WidgetType.MUSIC.id)
                        _music.value = video
                        Log.i(
                            TAG,
                            "Music metadata found: ${video.title}"
                        )
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "Error while loading daily video",
                            e
                        )
                    }
                }
            }*/

    /*        private fun loadSermonVideo() {
                viewModelScope.launch {
                    _loading.value = true
                    try {
                        val metadataID = widgetRepository.getMetadataID(WidgetType.SERMON.id, 2)
                        if (metadataID == null) {
                            Log.e(
                                TAG,
                                "No sermon ID found"
                            )
                            return@launch
                        }
                        val video = youtubeRepository.getMetadata(metadataID, WidgetType.SERMON.id)
                        _sermon.value = video
                        Log.i(
                            TAG,
                            "Sermon metadata found: ${video.title}"
                        )
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "Error while loading sermon",
                            e
                        )
                    }
                }
            }*/

    companion object {
        val TAG = Companion::class.java.canonicalName
    }

}