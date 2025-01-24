package com.example.testapp.presentation.searching

import UniversalListAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.BaseFragment
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.common.util.NetworkUtil
import com.example.testapp.databinding.WindowUniversalListBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.createAnnouncement.viewModel.FragmentSubCategoryVM
import com.example.testapp.presentation.home.viewModel.AnnouncementListViewModel
import com.example.testapp.presentation.searching.viewModel.FragmentUniversalListVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
@AndroidEntryPoint
class FragmentUniversalList(): BaseFragment() {
    private var binding : WindowUniversalListBinding by Delegates.notNull()
    private val viewModel: FragmentSubCategoryVM by viewModels()
    private lateinit var universalListAdapter: UniversalListAdapter
    private val viewModelAnnouncement: AnnouncementListViewModel by viewModels()

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
        initAdapter()
        observeFavouriteList()
        universalListAdapter.updateStyle(FragmentUniversalListVM.ItemRepresentationStyle.styleMosaic)
        updateLayoutManager(FragmentUniversalListVM.ItemRepresentationStyle.styleMosaic)
        binding.listTypeSwitcher.setImageResource(R.drawable.vicon_item_type_mosaic)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        if (NetworkUtil.isInternetAvailable(requireContext())){
            getAnnoucementList()
        }
        if (categoryId != null) {
            getAnnoucementList()
        }
        if (query != null) {
            getItemsByQuery()
            binding.searchView.setQuery(query,false )
        }
        initSearchViewClickers()

        binding.listTypeSwitcher.setOnClickListener {
            showPopupMenu(it)
        }
        binding.listPropertyFilter.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace<FragmentFilter>(
                    containerViewId= R.id.fragment_container_view_tag,
                ).addToBackStack("replacement")
            }
        }

    }

    override fun onNetworkLost() {
        super.onNetworkLost()
        NetworkUtil.showNoInternetToast(requireView())
    }

    override fun onNetworkRestored() {
        super.onNetworkRestored()
        getAnnoucementList()
    }
   private fun initAdapter(){
        universalListAdapter = UniversalListAdapter(layoutInflater,
           FragmentUniversalListVM.ItemRepresentationStyle.styleGallery,
           onItemClicked = {itemId->
               activity?.supportFragmentManager?.commit {
                   replace<FragmentAnnouncementDetail>(
                       containerViewId= R.id.fragment_container_view_tag,
                       args = bundleOf("itemId" to itemId)
                   ).addToBackStack("replacement")
               }
           },
            onFavouriteClicked = { itemId ->
                viewModelAnnouncement.changeFavouriteStatus("ru", itemId)
            }
           )
   }
    private fun observeFavouriteList() {
        lifecycleScope.launch {
            viewModelAnnouncement.currentFavourites.collect { favouriteList ->
                universalListAdapter.updateFavouriteList(favouriteList)
            }
        }
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
    private fun showPopupMenu(anchor:View){
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.inflate(R.menu.popup_item_style)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.style_gallery -> {
                    universalListAdapter.updateStyle(FragmentUniversalListVM.ItemRepresentationStyle.styleGallery)
                    updateLayoutManager(FragmentUniversalListVM.ItemRepresentationStyle.styleGallery)
                    binding.listTypeSwitcher.setImageResource(R.drawable.vicon_item_type_gallery)
                    true
                }
                R.id.style_mozaik -> {
                    universalListAdapter.updateStyle(FragmentUniversalListVM.ItemRepresentationStyle.styleMosaic)
                    updateLayoutManager(FragmentUniversalListVM.ItemRepresentationStyle.styleMosaic)
                    binding.listTypeSwitcher.setImageResource(R.drawable.vicon_item_type_mosaic)
                    true
                }
                R.id.style_list -> {
                    universalListAdapter.updateStyle(FragmentUniversalListVM.ItemRepresentationStyle.styleList)
                    updateLayoutManager(FragmentUniversalListVM.ItemRepresentationStyle.styleList)
                    binding.listTypeSwitcher.setImageResource(R.drawable.vicon_item_type_list)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    @SuppressLint("NotifyDataSetChanged", "WrongConstant")
    private fun updateLayoutManager(style: FragmentUniversalListVM.ItemRepresentationStyle) {
        val recyclerView = binding.list

        when (style) {
            FragmentUniversalListVM.ItemRepresentationStyle.styleGallery,
            FragmentUniversalListVM.ItemRepresentationStyle.styleList -> {
                // Use LinearLayoutManager for gallery and list styles
                val orientation =  LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), orientation, false)
            }

            FragmentUniversalListVM.ItemRepresentationStyle.styleMosaic -> {
                // Use GridLayoutManager for mosaic style
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // Default span count for mosaic
            }
        }

        // Notify adapter of layout updates
        universalListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun getAnnoucementList(){
        val announcementRecyclerView = binding.list
        categoryId?.let { viewModel.getItemsByCategory(categoryId = it, offset = 0, lang = "ru") }
        announcementRecyclerView.adapter = universalListAdapter
        lifecycleScope.launch {
            viewModel.getItemsByCategroy.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        //toggleShimmer(false)
                       // announcementAdapter.setAnnouncementItems(resource.data)
                        universalListAdapter.setAnnouncements(resource.data)
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
        query?.let { viewModel.getItemsByQuery(query = it, offset = 0, lang = "ru", sorting = null) }
        announcementRecyclerView.adapter = universalListAdapter
        lifecycleScope.launch {
            viewModel.getItemsByQuery.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        //toggleShimmer(false)
                        universalListAdapter.setAnnouncements(resource.data)
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