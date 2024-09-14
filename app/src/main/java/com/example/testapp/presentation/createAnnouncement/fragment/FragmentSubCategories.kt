package com.example.testapp.presentation.createAnnouncement.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowSubCategoriesBinding
import com.example.testapp.presentation.createAnnouncement.adapter.SubCategoryAdapter
import com.example.testapp.presentation.createAnnouncement.viewModel.FragmentSubCategoryVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint

class FragmentSubCategories: Fragment(R.layout.window_sub_categories) {
    private var binding: WindowSubCategoriesBinding by Delegates.notNull()
    private val viewModel: FragmentSubCategoryVM by viewModels()
    private val subCategoryId : Int? by lazy {
        arguments?.getInt("categoryId")
    }
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
        binding.toolbar.setOnClickListener {
            parentFragmentManager.popBackStack("FragmentCreateEditAnnouncement", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        binding.backTitle.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        getSubCategories()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getSubCategories() {
      val subCategoryRecyclerView = view?.findViewById<RecyclerView>(R.id.subCategoryList)
        subCategoryRecyclerView?.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
        }
        val subCategoryAdapter = SubCategoryAdapter(layoutInflater){categoryItem->
                if (categoryItem.hasChild){
                    parentFragmentManager.commit {
                        replace<FragmentSubCategories>(
                            containerViewId = R.id.fragment_container_view_tag,
                            args = bundleOf("categoryId" to categoryItem.categoryId)
                        ).addToBackStack("FragmentCreateEditAnnouncement")
                    }
                }else{
                    parentFragmentManager.commit {
                        replace<FragmentCreateEditAnnouncement>(
                            containerViewId = R.id.fragment_container_view_tag,
                            args = bundleOf("categoryName" to categoryItem.title)
                        ).addToBackStack("FragmentCreateEditAnnouncement")
                    }
                }

        }

        subCategoryId?.let { viewModel.getSubCategories(categoryId = it,"uz") }

        subCategoryRecyclerView?.adapter = subCategoryAdapter
        lifecycleScope.launch {
            //subCategoryId?.let { viewModel.getSubCategories(categoryId = it,"uz") }
            viewModel.subCategories.collect {resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data.let { data ->
                            if (data.isNotEmpty()) {
                                subCategoryAdapter.setSubCategoryItems(data)
                                binding.subCategoryList.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                            } else {
                                binding.subCategoryList.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                        showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        binding.subCategoryList.visibility = View.GONE
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