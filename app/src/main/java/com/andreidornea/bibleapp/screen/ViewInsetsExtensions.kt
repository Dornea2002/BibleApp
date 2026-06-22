package com.andreidornea.bibleapp.screen

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.applySystemBarsInsets(
    applyTop: Boolean = true,
    applyBottom: Boolean = true,
    includeIme: Boolean = true
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val imeBottom = if (includeIme)
            insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        else 0

        v.updatePadding(
            left = systemBars.left,
            top = if (applyTop) systemBars.top else 0,
            right = systemBars.right,
            bottom = if (applyBottom)
                maxOf(systemBars.bottom, imeBottom)
            else 0
        )
        insets
    }
}