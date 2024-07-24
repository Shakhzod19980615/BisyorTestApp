package com.example.testapp.presentation.createAnnouncement.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowCategoryPickerBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.createAnnouncement.adapter.CategoryPickerAdapter
import com.example.testapp.presentation.home.viewModel.CategoryTabViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
@AndroidEntryPoint
class FragmentCategoryPicker: Fragment(R.layout.window_category_picker) {
    private var binding: WindowCategoryPickerBinding by Delegates.notNull()
    private val viewModelTab: CategoryTabViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowCategoryPickerBinding.inflate(inflater)
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategoryList()
        setupToolbar()
    }
    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getCategoryList() {
        val categoryRecyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        val categoryPickerAdapter = CategoryPickerAdapter(layoutInflater){categoryModel->
            parentFragmentManager.commit {
                replace<FragmentSubCategories>(
                    containerViewId = R.id.fragment_container_view_tag,
                    args = bundleOf("itemId" to categoryModel.categoryId)
                ).addToBackStack("FragmentCreateEditAnnouncement")
            }
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