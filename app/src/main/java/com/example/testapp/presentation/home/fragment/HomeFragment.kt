package com.example.testapp.presentation.home.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowHomeBinding
import com.example.testapp.presentation.home.adapter.CategoryAdapter
import com.example.testapp.presentation.home.viewModel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.window_home) {
    private val viewModel: CategoryViewModel by viewModels()
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
        getCategoryItems()

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getCategoryItems(){
        val categoryRecyclerView = view?.findViewById<RecyclerView>(R.id.list_tab_categories)
        categoryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),LinearLayoutManager.HORIZONTAL,false
            )
        }

        val categoryAdapter = CategoryAdapter(layoutInflater)
        viewModel.getAllCategories()
        categoryRecyclerView?.adapter = categoryAdapter
        lifecycleScope.launch {
            viewModel.categoryItems.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        categoryAdapter.setCategoryItems(resource.data)
                    }
                    is Resource.Error -> {
                        // Handle error state
                        // You can use resource.message to get the error message
                    }
                    is Resource.Loading -> {
                        // Handle loading state
                    }
                }
            }
        }
    }
}
