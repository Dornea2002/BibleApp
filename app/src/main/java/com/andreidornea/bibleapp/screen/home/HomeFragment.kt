package com.andreidornea.bibleapp.screen.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andreidornea.bibleapp.R
import com.andreidornea.bibleapp.databinding.HomeFragmentBinding
import com.andreidornea.bibleapp.model.widget.YoutubeVideo
import com.andreidornea.bibleapp.ui.widget.YoutubeVideoWidget
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.home_fragment) {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            FirebaseFirestore.getInstance()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        /*        val video = YoutubeVideo(
                    "hk2kzCj1L6w",
                    "The Story of David",
                    "BibleProject"
                )

                YoutubeVideoWidget.bind(this, binding.youtubeVideoWidget, video)*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.dailyVerse.collect { verseResponse ->

                        verseResponse ?: return@collect
                        binding.dailyVerseWidget.dailyVerseText.text =
                            verseResponse.verse.details.text
                        binding.dailyVerseWidget.dailyVerseReference.text =
                            verseResponse.verse.details.reference
                    }

                }
                launch {
                    homeViewModel.youtubeVideo.collect { video ->
                        video ?: return@collect
                        if (video != null) {
                            YoutubeVideoWidget.bind(
                                this@HomeFragment,
                                binding.youtubeVideoWidget,
                                video
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}