package com.andreidornea.bibleapp.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class WidgetRepository(
    private val firestore: FirebaseFirestore
) {

    companion object {
        private val TAG =
            Companion::class.java.canonicalName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getMetadataID(
        categoryID: String,
        rotationDays: Int
    ): String?{

        val dataCategory = firestore
            .collection("widgetCategory")
            .document(categoryID)

        val snapshot = firestore
            .collection("metadata")
            .whereEqualTo("category", dataCategory)
            .get()
            .await()

        if(snapshot.isEmpty) {
            Log.w(TAG, "No metadata found for category: $categoryID")
            return null
        }

        val metadata = snapshot.documents
            .mapNotNull { document ->
                val metadatID =
                    document.getString("metadataID")
                val order =
                    document.getDouble("order")?.toInt()
                if(metadatID == null || order == null){
                    null
                } else {
                    VideoOrder(
                        metadataId = metadatID,
                        order = order
                    )
                }
            }.sortedBy { it.order }

        if(metadata.isEmpty()) {
            Log.w(TAG, "No valid metadata found for category: $categoryID")
            return null
        }

        Log.d(TAG, "Metadata: $metadata")

        val dayOfYear = LocalDate.now().dayOfYear
        val rotationIndex = (dayOfYear / rotationDays) % metadata.size

        val selectedItem =
            metadata[rotationIndex]

        Log.d(
            TAG,
            "Category: $categoryID | " +
                    "Day: $dayOfYear | " +
                    "Rotation days: $rotationDays | " +
                    "Index: $rotationIndex | " +
                    "Selected: ${selectedItem.metadataId} | " +
                    "Order: ${selectedItem.metadataId}"
        )

        return selectedItem.metadataId
    }

    private data class VideoOrder(
        val metadataId: String,
        val order: Int
    )
}