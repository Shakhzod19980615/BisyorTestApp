package com.example.testapp.presentation.createAnnouncement.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.databinding.WindowCreateEditAnnouncementBinding
import com.example.testapp.presentation.authoration.verificationCode.fragment.FragmentVerificationCode
import com.example.testapp.presentation.createAnnouncement.adapter.CreateAnnouncementAdapter
import com.example.testapp.presentation.createAnnouncement.adapter.CreateAnnouncementImage
import com.example.testapp.presentation.createAnnouncement.adapter.UploadState
import com.example.testapp.presentation.createAnnouncement.viewModel.CreateEditAnnouncementVM
import com.example.testapp.presentation.home.fragment.FragmentHome
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
    private val categoryTitle : String? by lazy {
        arguments?.getString("categoryName")
    }
    // Runnable to hide the delete icon
        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowCreateEditAnnouncementBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    parentFragmentManager.commit {
                        replace<FragmentHome>(R.id.fragment_container_view_tag)
                    }
                }

            })
        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.isUserActive.collect { isUserActive ->
                setActiveSegment(isUserActive)
                binding.clickerStore.isVisible = !isUserActive
            }
        }
        binding.clickerCategory.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace<FragmentCategoryPicker>(
                    containerViewId= R.id.fragment_container_view_tag,
                ).addToBackStack("replacement")
            }
        }
        if(categoryTitle != null) {
            binding.textCategory.setText(categoryTitle.toString())
        }
        //binding.textCategory.setText(categoryTitle.toString())
        binding.clickerPickImage.isVisible = viewModel.selectedImages.value.size < 8
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
        imagesAdapter = CreateAnnouncementAdapter (
            images = selectedImages,
            onDeleteClick = { image ->
                viewModel.removeImage(image)
            }
        )
        binding.listImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imagesAdapter
        }
        lifecycleScope.launch {
            viewModel.selectedImages.collect { images ->
                imagesAdapter.submitList(images)
                selectedImages.clear()
                selectedImages.addAll(images)
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