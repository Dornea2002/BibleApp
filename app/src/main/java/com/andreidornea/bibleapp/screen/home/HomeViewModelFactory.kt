package com.andreidornea.bibleapp.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModelFactory(
    private val firestore: FirebaseFirestore
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create
                (modelClass: Class<T>
    ): T {
       if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
           @Suppress("UNCHECKED_CAST")
           return HomeViewModel(firestore) as T
       }

        throw IllegalArgumentException(
        "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}