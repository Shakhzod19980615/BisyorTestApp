package com.example.testapp.presentation.searching

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowUniversalListBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.createAnnouncement.viewModel.FragmentSubCategoryVM
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
@AndroidEntryPoint
class FragmentUniversalList(): Fragment(R.layout.window_universal_list) {
    private var binding : WindowUniversalListBinding by Delegates.notNull()
    private val viewModel: FragmentSubCategoryVM by viewModels()
    private val categoryId : Int? by lazy {
        arguments?.getInt("categoryId")
    }
    private val query : String? by lazy {
        arguments?.getString("query")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowUniversalListBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        if (categoryId != null) {
            getAnnoucementList()
        }
        if (query != null) {
            getItemsByQuery()
            binding.searchView.setQuery(query,false )
        }
        initSearchViewClickers()



    }
    private fun initSearchViewClickers(){
        binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).isFocusable = false
        binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity?.supportFragmentManager?.popBackStack()
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private fun getAnnoucementList(){
        val announcementRecyclerView = binding.list
        announcementRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val announcementAdapter = AnnouncementListAdapter(layoutInflater,
            onItemClicked = {itemId->
                // replaceFragment(FragmentAnnouncementDetail())
                activity?.supportFragmentManager?.commit {
                    replace<FragmentAnnouncementDetail>(
                        containerViewId= R.id.fragment_container_view_tag,
                        args = bundleOf("itemId" to itemId)
                    ).addToBackStack("replacement")
                }
            }
        )
        categoryId?.let { viewModel.getItemsByCategory(categoryId = it, offset = 0, lang = "ru") }
        announcementRecyclerView.adapter = announcementAdapter
        lifecycleScope.launch {
            viewModel.getItemsByCategroy.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        //toggleShimmer(false)
                        announcementAdapter.setAnnouncementItems(resource.data)
                        binding.totalCount.text = resource.data.size.toString()+ " обявлений"
                        binding.sortText.text = "Самые новые"

                    }
                    is Resource.Error -> {
                        //toggleShimmer(false)
                        showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        //toggleShimmer(true)
                    }

                }
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private fun getItemsByQuery(){
        val announcementRecyclerView = binding.list
        announcementRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val announcementAdapter = AnnouncementListAdapter(layoutInflater,
            onItemClicked = {itemId->
                // replaceFragment(FragmentAnnouncementDetail())
                activity?.supportFragmentManager?.commit {
                    replace<FragmentAnnouncementDetail>(
                        containerViewId= R.id.fragment_container_view_tag,
                        args = bundleOf("itemId" to itemId)
                    ).addToBackStack("replacement")
                }
            }
        )
        query?.let { viewModel.getItemsByQuery(query = it, offset = 0, lang = "ru", sorting = null) }
        announcementRecyclerView.adapter = announcementAdapter
        lifecycleScope.launch {
            viewModel.getItemsByQuery.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        //toggleShimmer(false)
                        announcementAdapter.setAnnouncementItems(resource.data)
                        binding.totalCount.text = resource.data.size.toString()+ " обявлений"
                        binding.sortText.text = "Самые новые"

                    }
                    is Resource.Error -> {
                        //toggleShimmer(false)
                        showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        //toggleShimmer(true)
                    }

                }
            }
        }

    }
    private fun showAlertDialog(message: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_universal_messaging, null)
        val alertDialog  = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            ?.setCancelable(true)
            ?.create()
        val btnDialogOk: Button = dialogView.findViewById(R.id.btn_ok)
        val dialogMessage : TextView = dialogView.findViewById(R.id.text_title)
        dialogMessage.text = message
        btnDialogOk.setOnClickListener {
            // Handle button click
            alertDialog?.dismiss()  // Dismiss the dialog on button click
        }
        alertDialog?.show()
    }
}