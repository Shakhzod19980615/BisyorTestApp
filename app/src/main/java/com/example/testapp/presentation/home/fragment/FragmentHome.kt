package com.example.testapp.presentation.home.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowHomeBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter
import com.example.testapp.presentation.home.adapter.CategoryTabAdapter
import com.example.testapp.presentation.home.viewModel.AnnouncementListViewModel
import com.example.testapp.presentation.home.viewModel.CategoryTabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentHome: Fragment(R.layout.window_home) {
    private val viewModelTab: CategoryTabViewModel by viewModels()
    private val viewModelAnnouncement: AnnouncementListViewModel by viewModels()
    private var binding : WindowHomeBinding by Delegates.notNull()
    private lateinit var shimmerLayout: LinearLayout
    private lateinit var contentLayout: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowHomeBinding.inflate(inflater)
        shimmerLayout = binding.root.findViewById(R.id.shimmer_layout)
        contentLayout = binding.root.findViewById(R.id.list)
        return binding.root

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategoryList()
        getAnnoucementList()

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getCategoryList(){
        val categoryRecyclerView = view?.findViewById<RecyclerView>(R.id.list_tab_categories)
        categoryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),LinearLayoutManager.HORIZONTAL,false
            )
        }

        val categoryTabAdapter = CategoryTabAdapter(layoutInflater,
            onTabClicked = {
                viewModelAnnouncement.getAnnouncementList(categoryId = it)
            })

        viewModelTab.getAllCategories()
        categoryRecyclerView?.adapter = categoryTabAdapter
        lifecycleScope.launch {
            viewModelTab.categoryItems.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        categoryTabAdapter.setCategoryItems(resource.data)
                        val firstCategoryId = resource.data.first().categoryId
                        viewModelAnnouncement.getAnnouncementList(categoryId = firstCategoryId)
                    }
                    is Resource.Error -> {
                       showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        contentLayout.visibility = View.GONE
                        shimmerLayout.visibility = View.VISIBLE
                    }

                    else -> {}
                }
            }
        }
    }

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
        announcementRecyclerView.adapter = announcementAdapter
        lifecycleScope.launch {
            viewModelAnnouncement.announcementItems.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        toggleShimmer(false)
                        announcementAdapter.setAnnouncementItems(resource.data)
                    }
                    is Resource.Error -> {
                        toggleShimmer(false)
                        showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        toggleShimmer(true)
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
    private fun toggleShimmer(isLoading: Boolean) {
        if (isLoading) {
            shimmerLayout.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            shimmerLayout.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE
        }
    }
    private fun replaceFragment(fragment: Fragment){
        activity?.supportFragmentManager?.commit {
            replace(R.id.fragment_container_view_tag,
                fragment)
        }
    }
}
