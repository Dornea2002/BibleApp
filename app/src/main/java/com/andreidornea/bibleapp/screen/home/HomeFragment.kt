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
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreidornea.bibleapp.R
import com.andreidornea.bibleapp.databinding.HomeFragmentBinding
import com.andreidornea.bibleapp.databinding.YoutubeMetadataCardBinding
import com.andreidornea.bibleapp.model.widget.YoutubeMetadata
import com.andreidornea.bibleapp.ui.widget.YoutubeMetadataWidget
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.home_fragment) {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeWidgetAdapter: HomeWidgetAdapter

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

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView(){
        homeWidgetAdapter = HomeWidgetAdapter(this)
        binding.widgetsRecyclerView.apply {
            adapter = homeWidgetAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.widgets.collect { widgets ->
                        homeWidgetAdapter.submitList(widgets)
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