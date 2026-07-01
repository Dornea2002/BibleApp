package com.andreidornea.bibleapp.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andreidornea.bibleapp.R
import com.andreidornea.bibleapp.databinding.HomeFragmentBinding
import com.andreidornea.bibleapp.model.YoutubeVideo
import com.andreidornea.bibleapp.ui.widget.YoutubeVideoWidget

class HomeFragment : Fragment(R.layout.home_fragment) {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileInitials.apply {
            text = "SD"
        }

        binding.dailyVerseText.apply {
            text = "Fiindcă atât de mult a iubit Dumnezeu lumea, că a dat pe singurul Lui Fiu, pentru ca oricine crede în El să nu piară, ci să aibă viața veșnică"
        }

        binding.dailyVerseReference.apply {
            text = "Ioan 3:16"
        }

        val videp = YoutubeVideo(
            "hk2kzCj1L6w",
            "The Story of David",
            "BibleProject"
        )

        YoutubeVideoWidget.bind(this, binding.youtubeVideoWidget, videp)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}