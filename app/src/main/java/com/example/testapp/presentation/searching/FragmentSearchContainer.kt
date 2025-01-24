package com.example.testapp.presentation.searching

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.BaseFragment
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.common.util.NetworkUtil
import com.example.testapp.databinding.WindowSearchContainerBinding
import com.example.testapp.presentation.createAnnouncement.adapter.CategoryPickerAdapter
import com.example.testapp.presentation.createAnnouncement.adapter.SubCategoryAdapter
import com.example.testapp.presentation.createAnnouncement.viewModel.FragmentSubCategoryVM
import com.example.testapp.presentation.home.viewModel.CategoryTabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentSearchContainer : BaseFragment()   {
    private var binding : WindowSearchContainerBinding by Delegates.notNull()
    private val viewModelTab: CategoryTabViewModel by viewModels()
    private val viewModel: FragmentSubCategoryVM by viewModels()
    private var categoryId :Int = 0
    private val categoryHistory = ArrayDeque<Int>() // Tracks category navigation history

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowSearchContainerBinding.inflate(inflater)
        return binding.root

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(NetworkUtil.isInternetAvailable(requireContext())){
            getCategoryList()
        }
        initSearchViewClickers()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onNetworkRestored() {
        super.onNetworkRestored()
        getCategoryList()
    }

    override fun onNetworkLost() {
        super.onNetworkLost()
        NetworkUtil.showNoInternetToast(requireView())
    }
    private fun initSearchViewClickers(){
       /* binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).isFocusable = false
        binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setOnClickListener {
            parentFragmentManager.commit {
                replace<FragmentSearchDialog>(
                    containerViewId = R.id.fragment_container_view_tag
                ).addToBackStack("FragmentSearchDialog")
            }
        }*/
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                parentFragmentManager.commit {
                    replace<FragmentSearchDialog>(
                        containerViewId = R.id.fragment_container_view_tag
                    ).addToBackStack("FragmentSearchDialog")
                }
            }
        }

    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getCategoryList() {
        categoryHistory.clear()
        binding.clickerBack.apply {
            visibility = View.GONE // Make it invisible and remove it from layout space
            layoutParams = layoutParams.apply {
                height = 0 // Set height to 0
            }
        }
        val categoryRecyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val categoryPickerAdapter = CategoryPickerAdapter(layoutInflater){selectedCategoryId->
            /*parentFragmentManager.commit {
                replace<FragmentSubCategories>(
                    containerViewId = R.id.fragment_container_view_tag,
                    args = bundleOf("categoryId" to categoryId)
                ).addToBackStack("FragmentCreateEditAnnouncement")
            }*/
            categoryHistory.addLast(selectedCategoryId)
           /* // Push the current categoryId to the history stack before navigating
            categoryId?.let { categoryHistory.addLast(it) }

            // Update to the new categoryId
            categoryId = selectedCategoryId*/
            getSubCategories()
        }

        viewModelTab.getAllCategories()
        categoryRecyclerView?.adapter = categoryPickerAdapter
        categoryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            lifecycleScope.launch {
                viewModelTab.categoryItems.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            categoryPickerAdapter.setCategoryItems(resource.data)
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE

                        }
                        is Resource.Error -> {
                            showAlertDialog(resource.message)
                        }
                        is Resource.Loading -> {
                            binding.recyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        else -> {}
                    }
                }
            }
        }

    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @SuppressLint("SuspiciousIndentation")
    private fun getSubCategories() {
        binding.clickerBack.apply {
            visibility = View.VISIBLE // Show the view
            layoutParams = layoutParams.apply {
                height = (32 * resources.displayMetrics.density).toInt() // Set height in dp
            }
        }
        binding.clickerBack.setOnClickListener {
            if (categoryHistory.isEmpty() || categoryHistory.size == 1) {
                getCategoryList()
            } else {
                 categoryHistory.removeLast()
                categoryId = categoryHistory.last()
                    categoryId.let { viewModel.getSubCategories(categoryId = it, "ru") }
            }
        }
        val subCategoryRecyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        subCategoryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
        }
        categoryId = categoryHistory.last()
        categoryId.let { viewModel.getSubCategories(categoryId = it,"ru") }
        val subCategoryAdapter = SubCategoryAdapter(layoutInflater,onItemClick ={categoryItem->
            if (categoryItem.hasChild){
                categoryHistory.addLast(categoryItem.categoryId)
                categoryId = categoryHistory.last()
                categoryId.let { viewModel.getSubCategories(categoryId = it,"ru") }
            }else{
                parentFragmentManager.commit {
                    replace<FragmentUniversalList>(
                        containerViewId = R.id.fragment_container_view_tag,
                        args = bundleOf(
                            "categoryId" to categoryItem.categoryId,)
                    ).addToBackStack("FragmentCreateEditAnnouncement")
                }
            }

        }, showCount = true)
        subCategoryRecyclerView?.adapter = subCategoryAdapter
        lifecycleScope.launch {
            viewModel.subCategories.collect {resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data.let { data ->
                            if (data.isNotEmpty()) {
                                subCategoryAdapter.setSubCategoryItems(data)
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                            } else {
                                binding.recyclerView.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                        showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    else -> {}
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