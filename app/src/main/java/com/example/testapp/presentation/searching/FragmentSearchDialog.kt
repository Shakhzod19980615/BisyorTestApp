package com.example.testapp.presentation.searching

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.databinding.WindowSearchContainerBinding
import com.example.testapp.databinding.WindowSearchDialogBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.createAnnouncement.viewModel.FragmentSubCategoryVM
import kotlin.properties.Delegates

class FragmentSearchDialog: Fragment(R.layout.window_search_dialog) {
    private var binding : WindowSearchDialogBinding by Delegates.notNull()
    private val viewModel: FragmentSubCategoryVM by viewModels()
    private val categoryId : Int? by lazy {
        arguments?.getInt("categoryId")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowSearchDialogBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.edSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && v.text.isNotEmpty()) {
                // do something
                val query = binding.edSearch.text.toString().trim()
                activity?.supportFragmentManager?.commit {
                    replace<FragmentUniversalList>(
                        containerViewId= R.id.fragment_container_view_tag,
                        args = bundleOf("query" to query )
                    ).addToBackStack("replacement")
                }
                true
            } else {
                false
            }

        }
    }

}