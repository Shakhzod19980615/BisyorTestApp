package com.example.testapp.presentation.createAnnouncement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapp.R
import com.example.testapp.databinding.WindowCategoryPickerBinding
import com.example.testapp.databinding.WindowSubCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.properties.Delegates

@AndroidEntryPoint

class FragmentSubCategories: Fragment(R.layout.window_sub_categories) {
    private var binding: WindowSubCategoriesBinding by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowSubCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.toolbar.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }*/
        setupToolbar()
    }
    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            // Handle the back button press to pop the stack to FragmentCreateEditAnnouncement
            parentFragmentManager.popBackStack("FragmentCreateEditAnnouncement", 0)
        }
    }
}