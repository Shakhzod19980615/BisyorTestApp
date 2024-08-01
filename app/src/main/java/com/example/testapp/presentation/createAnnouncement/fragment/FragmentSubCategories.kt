package com.example.testapp.presentation.createAnnouncement.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowCategoryPickerBinding
import com.example.testapp.databinding.WindowSubCategoriesBinding
import com.example.testapp.presentation.createAnnouncement.adapter.SubCategoryAdapter
import com.example.testapp.presentation.createAnnouncement.viewModel.FragmentSubCategoryVM
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint

class FragmentSubCategories: Fragment(R.layout.window_sub_categories) {
    private var binding: WindowSubCategoriesBinding by Delegates.notNull()
    private val viewModel: FragmentSubCategoryVM by viewModels()
    private val categoryId : Int? by lazy {
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
        binding.toolbar.setNavigationOnClickListener {

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
        val subCategoryAdapter = SubCategoryAdapter(layoutInflater)

       categoryId?.let { viewModel.getSubCategories(categoryId = it,"uz") }

        subCategoryRecyclerView?.adapter = subCategoryAdapter
        lifecycleScope.launch {
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