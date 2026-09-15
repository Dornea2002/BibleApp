package com.andreidornea.bibleapp.screen.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreidornea.bibleapp.api.ApiClient
import com.andreidornea.bibleapp.model.widget.DailyVerse
import com.andreidornea.bibleapp.model.widget.YoutubeVideo
import com.andreidornea.bibleapp.repository.DailyVerseRepository
import com.andreidornea.bibleapp.repository.WidgetRepository
import com.andreidornea.bibleapp.repository.YoutubeRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    firestore: FirebaseFirestore
): ViewModel() {
    private val dailyVerseRepository =
        DailyVerseRepository(ApiClient.dailyVerseApiService)
    private val widgetRepository =
        WidgetRepository(firestore)
    private val youtubeRepository =
        YoutubeRepository(ApiClient.youtubeApiService)
    private val _dailyVerse =
        MutableStateFlow<DailyVerse?>(null)
    val dailyVerse =
        _dailyVerse.asStateFlow()
    private val _youtubeVideo =
        MutableStateFlow<YoutubeVideo?>(null)
    val youtubeVideo =
        _youtubeVideo.asStateFlow()
    private val _loading =
        MutableStateFlow(false)
    val loading =
        _loading.asStateFlow()

    init {
        loadDailyVerse()
        loadYoutubeVideo()
    }

    private fun loadDailyVerse(){
        viewModelScope.launch {
            _loading.value = true
            try {
                _dailyVerse.value = dailyVerseRepository.getDailyVerse()
            } catch (e: Exception){
                Log.e(TAG, "Error", e)
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadYoutubeVideo(){
        viewModelScope.launch {
            _loading.value = true
            try {
                val videoID = widgetRepository.getMetadataID("PxoNBQSohemDXFgKbEOT", 1)
                if(videoID == null){
                    Log.e(TAG,
                        "No video ID found")
                    return@launch
                }
                val video = youtubeRepository.getVideo(videoID)
                _youtubeVideo.value = video
            } catch (e: Exception){
                Log.e(TAG,
                    "Error while loading daily video",
                    e)
            }
        }
    }

    companion object{
        val TAG = Companion::class.java.canonicalName
    }

}