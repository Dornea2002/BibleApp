package com.andreidornea.bibleapp.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreidornea.bibleapp.api.ApiClient
import com.andreidornea.bibleapp.model.widget.DailyVerse
import com.andreidornea.bibleapp.repository.DailyVerseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val dailyVerseRepository = DailyVerseRepository(ApiClient.dailyVerseApiService)
    private val _dailyVerse = MutableStateFlow<DailyVerse?>(null)
    val dailyVerse = _dailyVerse.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        loadDailyVerse()
    }

    private fun loadDailyVerse(){
        viewModelScope.launch {
            _loading.value = true

            try {
                _dailyVerse.value = dailyVerseRepository.getDailyVerse()
            } catch (e: Exception){
                Log.e("HomeViewModel", "Error", e)
            } finally {
                _loading.value = false
            }
        }
    }
}