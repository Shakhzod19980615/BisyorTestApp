package com.example.testapp.presentation.announcementDetail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowAnnouncementDetailBinding
import com.example.testapp.presentation.announcementDetail.viewModel.AnnouncementDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentAnnouncementDetail : Fragment(R.layout.window_announcement_detail) {

    private val announcementDetailViewModel: AnnouncementDetailViewModel by viewModels()
    private var binding : WindowAnnouncementDetailBinding by Delegates.notNull()
    private val itemId : Int? by lazy {
        arguments?.getInt("itemId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowAnnouncementDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private suspend fun initVm(){
        itemId?.let {
            announcementDetailViewModel.getAnnouncementDetails(it)
        }
       announcementDetailViewModel.announcementDetail.collect{resources ->
           when(resources){
               is Resource.Error -> {
                   Toast.makeText(requireContext(),resources.message,Toast.LENGTH_SHORT).show()
               }
               is Resource.Loading -> {

               }
               is Resource.Success -> {
                   binding.price.text = resources.data.price
                   binding.title.text = resources.data.title
                   binding.content.text = resources.data.description
                   binding.itemId.text = resources.data.id.toString()
                   binding.announcedDate.text = resources.data.date
                   binding.viewedTotal.text = resources.data.viewedTotal.toString()
                   binding.clickerCategory.text = resources.data.categoryName

               }
           }
       }
    }
}