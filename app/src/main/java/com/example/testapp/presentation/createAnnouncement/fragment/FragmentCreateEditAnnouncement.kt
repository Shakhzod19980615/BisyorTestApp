package com.example.testapp.presentation.createAnnouncement.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.databinding.WindowCreateEditAnnouncementBinding
import com.example.testapp.presentation.createAnnouncement.adapter.CreateAnnouncementAdapter
import com.example.testapp.presentation.createAnnouncement.adapter.CreateAnnouncementImage
import com.example.testapp.presentation.createAnnouncement.adapter.UploadState
import com.example.testapp.presentation.createAnnouncement.viewModel.CreateEditAnnouncementVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentCreateEditAnnouncement: Fragment(R.layout.window_create_edit_announcement) {
    private var binding:WindowCreateEditAnnouncementBinding by Delegates.notNull()
    private val viewModel: CreateEditAnnouncementVM by viewModels()
    private val selectedImages = mutableListOf<CreateAnnouncementImage>()
    private lateinit var imagesAdapter: CreateAnnouncementAdapter
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        viewModel.addImages(uris)
    }
    private val handler = Handler(Looper.getMainLooper())

    // Runnable to hide the delete icon
        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowCreateEditAnnouncementBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.isUserActive.collect { isUserActive ->
                setActiveSegment(isUserActive)
                binding.clickerStore.isVisible = !isUserActive
            }
        }

        binding.textUser.setOnClickListener {
            viewModel.setActiveSegment(true)
        }

        binding.textStore.setOnClickListener {
            viewModel.setActiveSegment(false)
        }
        // Initial state
        viewModel.setActiveSegment(true)
        binding.clickerPickImage.setOnClickListener {
            pickImages()
        }
    }
    private fun pickImages() {
        if (viewModel.selectedImages.value.size < 9) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(requireContext(), "You can only upload up to 8 images", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupRecyclerView() {
        imagesAdapter = CreateAnnouncementAdapter ()
        binding.listImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imagesAdapter
        }
        lifecycleScope.launch {
            viewModel.selectedImages.collect { images ->
                imagesAdapter.submitList(images)
                binding.clickerPickImage.isVisible = images.size < 8
            }
        }
    }

    private fun setActiveSegment(isUserActive: Boolean) {
        if (isUserActive) {
            binding.textUser.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.textUser.background = ContextCompat.getDrawable(requireContext(), R.drawable.bck_active_segment_button)

            binding.textStore.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.textStore.background = null

            val params = binding.viewStatusChanger.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = -1
            binding.viewStatusChanger.layoutParams = params
        } else {
            binding.textUser.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.textUser.background = null

            binding.textStore.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.textStore.background = ContextCompat.getDrawable(requireContext(), R.drawable.bck_active_segment_button)

            val params = binding.viewStatusChanger.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = -1
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            binding.viewStatusChanger.layoutParams = params
        }
    }
}