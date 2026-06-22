package com.andreidornea.bibleapp.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applySystemBarsInsets()
    }
}