package com.example.testapp.presentation.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.BaseFragment
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.data.request.favourite.UserSubscriptionsRequest
import com.example.testapp.databinding.WindowOwnFavouritesBinding
import com.example.testapp.databinding.WindowSearchContainerBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.chat.fragment.FragmentChatContainer.Companion.ALL_Messages
import com.example.testapp.presentation.favourite.adapter.SubscriptionsAdapter
import com.example.testapp.presentation.favourite.viewModel.FragmentFavouriteVM
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter
import com.example.testapp.presentation.home.viewModel.AnnouncementListViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.bisyor.corelib.common.extension.showAlertDialog
import kotlin.properties.Delegates
@AndroidEntryPoint
class FragmentFavourites :BaseFragment() {
    private var binding: WindowOwnFavouritesBinding by Delegates.notNull()
    private val viewModel: FragmentFavouriteVM by viewModels()
    //private val viewModelAnnouncement: AnnouncementListViewModel by activityViewModels()
    private lateinit var announcementAdapter: AnnouncementListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var subcriptionAdapter: SubscriptionsAdapter
    companion object {
        const val Announcements = 0
        const val Searches = 1
        const val Sellers = 2
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowOwnFavouritesBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupSwipeRefresh()

        view.post {
            /*val defaultTab = binding.tabLayout.getTabAt(ALL_Messages)
            defaultTab?.select()*/

            // Explicitly load data for the first tab
            getAnnouncements()
        }
        observeFavouriteList()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    Announcements -> {getAnnouncements()}
                    Searches -> {getSubscriptions()}
                    Sellers -> {getSubscriptions()}
        }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    fun init(){
        recyclerView = binding.itemsList
        announcementAdapter = AnnouncementListAdapter(layoutInflater,
            onItemClicked = {itemId->
                activity?.supportFragmentManager?.commit {
                    replace<FragmentAnnouncementDetail>(
                        containerViewId= R.id.fragment_container_view_tag,
                        args = bundleOf("itemId" to itemId)
                    ).addToBackStack("FragmentHome")
                }
            },
            onFavouriteClicked = { itemId ->
                viewModel.changeFavouriteStatus("ru", itemId)

            },
        )
        subcriptionAdapter = SubscriptionsAdapter(layoutInflater)
    }
    fun getAnnouncements() {
        recyclerView.adapter = announcementAdapter
        viewModel.getFavouriteItems("ru", 0)
        lifecycleScope.launch {
            viewModel.favouriteItems.collectLatest  { resource ->
                when (resource) {
                    is Resource.Success -> {
                        binding.swiper.isRefreshing = false // Stop refreshing animation
                        announcementAdapter.setAnnouncementItems(resource.data)
                    }
                    is Resource.Error -> {
                        binding.swiper.isRefreshing = false // Stop refreshing animation
                        requireContext().showAlertDialog(resource.message)
                    }
                    is Resource.Loading -> {
                        binding.swiper.isRefreshing = true
                    }

                }
            }
        }
    }
    fun getSubscriptions() {
        recyclerView.adapter = subcriptionAdapter
        viewModel.getSubscribedUsers("ru", 0)
        lifecycleScope.launch {
            viewModel.subscribedUsers.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        binding.swiper.isRefreshing = false // Stop refreshing animation
                        subcriptionAdapter.setSubscriptionsItems(resource.data)
                    }

                    is Resource.Error -> {
                        binding.swiper.isRefreshing = false // Stop refreshing animation
                        requireContext().showAlertDialog(resource.message)
                    }

                    is Resource.Loading -> {
                        binding.swiper.isRefreshing = true
                    }
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged", "RepeatOnLifecycleWrongUsage")
    private fun observeFavouriteList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentFavourites.collectLatest { favouriteList ->
                    announcementAdapter.updateFavouriteList(favouriteList)
                    //Log.d("FragmentHome", "Favourite list updated: $favouriteList")
                }
            }
        }
    }
    private fun setupSwipeRefresh() {
        binding.swiper.setOnRefreshListener {
            // Refresh data when the user swipes
            when (binding.tabLayout.selectedTabPosition) {
                Announcements -> getAnnouncements()
                Searches -> {}
                Sellers -> {}
            }
        }
    }

}