package com.example.testapp.presentation.announcementDetail.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.databinding.WindowAnnouncementDetailBinding
import com.example.testapp.presentation.announcementDetail.adapter.ImagePagerAdapter
import com.example.testapp.presentation.announcementDetail.viewModel.AnnouncementDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVm()
        initClickers()
    }
    private  fun initVm(){
        configureImages()
        itemId.let {
            announcementDetailViewModel.getAnnouncementDetails(it?:-1)
        }
        lifecycleScope.launch{
            announcementDetailViewModel.announcementDetail.collect { resources ->
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
                        binding.clickerCoordinates.text = resources.data.address
                        if(resources.data.properties.isEmpty()){
                            binding.dynamicLay.visibility = View.GONE
                        }else{
                            binding.dynamicLay.visibility = View.VISIBLE
                            for(m in resources.data.properties){
                                val lay : View = layoutInflater.inflate(R.layout.view_item_dynamic_property,
                                    binding.dynamicLay,false)
                                lay.findViewById<TextView>(R.id.title).text = m.title
                                lay.findViewById<TextView>(R.id.value).text = m.value.toString()
                                binding.dynamicLay.addView(lay)
                            }
                            resources.data.properties

                        }

                    }
                }
            }
        }

    }
    private fun initClickers(){
       binding.toolbar.setNavigationOnClickListener {
           activity?.onBackPressed()
       }
        binding.iconShare.setOnClickListener {
            lifecycleScope.launch {
                announcementDetailViewModel.announcementDetail.collect{resources->
                    if (resources is Resource.Success){
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            val shareContent = "${resources.data.title}\n${"https://bisyor.uz/obyavlenie/"+ resources.data.link}"
                            putExtra(Intent.EXTRA_TEXT, shareContent , )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                    }else{
                        Toast.makeText(requireContext(), "No data to share", Toast.LENGTH_SHORT).show()
                    }
                }
            }
           }

    }
    @SuppressLint("SuspiciousIndentation")
    private  fun configureImages(){
        val dot1: View =binding.root.findViewById(R.id.dot1)
        val viewPager: ViewPager = binding.viewPager
        val dotContainer: LinearLayout = binding.layDots
        val imagesList =mutableListOf<String>()
        val imagePagerAdapter = ImagePagerAdapter(requireContext(), imagesList)
        viewPager.adapter = imagePagerAdapter
            lifecycleScope.launch {
            announcementDetailViewModel.announcementDetail.collect{resources->
                if (resources is Resource.Success){
                    imagesList.addAll(resources.data.images)
                    imagePagerAdapter.notifyDataSetChanged()
                    addDots(dotContainer, imagesList.size)
                    updateDots(dotContainer, 0)
                }
            }
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Do nothing or perform additional actions if needed
            }

            override fun onPageSelected(position: Int) {
                // Update dot indicator based on the current page
               // dot1.isSelected = position == 0
                updateDots(dotContainer, position)

                // Update more dots as needed
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Do nothing or perform additional actions if needed
            }
        })

    }
    private fun addDots(dotContainer: LinearLayout, count: Int) {
        dotContainer.removeAllViews()
        for (i in 0 until count) {
            val dot = ImageView(requireContext())
            //val dot1: View =binding.root.findViewById(R.id.dot1)
            dot.setImageResource(R.drawable.dot_indicator)  // Replace with your dot drawable
            dotContainer.addView(dot)
        }
    }

    private fun updateDots(dotContainer: LinearLayout, position: Int) {
         val selectedDotColor = ContextCompat.getColor(requireContext(), R.color.selected_dot_color)
         val unselectedDotColor = ContextCompat.getColor(requireContext(), R.color.white_inactive)
        for (i in 0 until dotContainer.childCount) {
            val dot = dotContainer.getChildAt(i) as? ImageView
            //dot?.isSelected = i == position
            dot?.apply {
                isSelected = i == position
                setColorFilter(if (isSelected) selectedDotColor else unselectedDotColor)
            }
        }
    }
}