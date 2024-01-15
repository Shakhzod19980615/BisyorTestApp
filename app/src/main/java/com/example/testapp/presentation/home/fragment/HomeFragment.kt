package com.example.testapp.presentation.home.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowHomeBinding
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter
import com.example.testapp.presentation.home.adapter.CategoryTabAdapter
import com.example.testapp.presentation.home.viewModel.AnnouncementListViewModel
import com.example.testapp.presentation.home.viewModel.CategoryTabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.window_home) {
    private val viewModelTab: CategoryTabViewModel by viewModels()
    private val viewModelAnnouncement: AnnouncementListViewModel by viewModels()
    private var binding : WindowHomeBinding by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowHomeBinding.inflate(inflater)
        return binding.root

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAnnoucementList()
        getCategoryList()


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
                        (Resource.Error("Couldn't reach server. Check your internet connection.", null))
                        Toast.makeText(context, "An unexpected error occured", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        (Resource.Loading("Loading"))
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getAnnoucementList(){
        val announcementRecyclerView = view?.findViewById<RecyclerView>(R.id.list)
        announcementRecyclerView?.apply {
            layoutManager = GridLayoutManager(requireContext(),2)

        }
        val announcementAdapter = AnnouncementListAdapter(layoutInflater)
        announcementRecyclerView?.adapter = announcementAdapter
        lifecycleScope.launch {
            viewModelAnnouncement.announcementItems.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        announcementAdapter.setAnnouncementItems(resource.data)
                    }
                    is Resource.Error -> {
                        (Resource.Error("Couldn't reach server. Check your internet connection.", null))
                        Toast.makeText(context, "An unexpected error occured", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        (Resource.Loading("Loading"))
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        }

    }
}
