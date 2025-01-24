package com.example.testapp.presentation.announcementDetail.fragment

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.testapp.BaseFragment
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.common.util.NetworkUtil
import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.databinding.WindowAnnouncementDetailBinding
import com.example.testapp.presentation.announcementDetail.adapter.ImagePagerAdapter
import com.example.testapp.presentation.announcementDetail.viewModel.AnnouncementDetailViewModel
import com.example.testapp.presentation.chat.fragment.FragmentMessanger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
@AndroidEntryPoint
class FragmentAnnouncementDetail : BaseFragment() {

    private val announcementDetailViewModel: AnnouncementDetailViewModel by viewModels()
    private var binding : WindowAnnouncementDetailBinding by Delegates.notNull()
    private val itemId : Int? by lazy {
        arguments?.getInt("itemId")
    }
    private var userId:Int = 0
    private var isDescExpanded = false
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
        if (NetworkUtil.isInternetAvailable(requireContext())){
            initVm()
        }
        initClickers()
        configureContent()

    }

    override fun onNetworkRestored() {
        super.onNetworkRestored()
        initVm()
    }

    override fun onNetworkLost() {
        super.onNetworkLost()
        NetworkUtil.showNoInternetToast(requireView())
    }
    private  fun initVm(){
        configureImages()
        configureContent()
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
                        userId = resources.data.userId
                        binding.price.text = resources.data.price
                        binding.title.text = resources.data.title
                        binding.content.text = resources.data.description
                        binding.itemId.text = resources.data.id.toString()
                        binding.announcedDate.text = resources.data.date
                        binding.viewedTotal.text = resources.data.viewedTotal.toString()
                        binding.clickerCategory.text = resources.data.categoryName
                        binding.clickerCoordinates.text = resources.data.address
                        binding.userName.text = resources.data.userName
                        binding.userPhone.text = resources.data.phones?.get(0)
                        Glide.with(binding.root).load(resources.data.userAvatar).into(binding.userImage)
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
    @SuppressLint("ResourceType")
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
       /* binding.clickerCall.setOnClickListener {
            announcementDetailViewModel.announcementDetail.value.let {
                if (it.phones.isNullOrEmpty().not())
                    if (it.phones!!.size == 1) {
                        startActivity(Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:" + it.phones!![0])
                        })
                    } else {
                        val dialog = PhonesPickerDialog(it.phones!!)
                        dialog.show(childFragmentManager, "phones_picker")
                    }
            }
        }*/
        binding.clickerMessaging.setOnClickListener {
            announcementDetailViewModel.createChat(
                lang = "ru", userId = userId, itemId = itemId?:-1)

            /*announcementDetailViewModel.announcementDetail.value.let {
                lifecycleScope.launch {
                    announcementDetailViewModel.announcementDetail.collect { resources ->
                        when(resources){
                            is Resource.Error -> {
                                Toast.makeText(requireContext(),resources.message,Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {

                            }
                        }
                    }

                }
            }*/
            announcementDetailViewModel.createChatModel.value.let {
                lifecycleScope.launch {
                    announcementDetailViewModel.createChatModel.collect{ resource->
                        when(resource){
                            is Resource.Error -> {
                                Toast.makeText(requireContext(),resource.message,Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {

                            }is Resource.Success -> {
                            activity?.supportFragmentManager?.commit {
                                replace<FragmentMessanger>(
                                    containerViewId= R.id.fragment_container_view_tag,
                                    args = bundleOf("chatId" to resource.data.chatId)
                                ).addToBackStack("replacement")
                            }

                        }
                        }

                    }
                }
            }
        }
        binding.appBar.addOnOffsetChangedListener { _, verticalOffset ->
            val activeIconColor = ContextCompat.getColor(requireContext(), R.color.defaultTextBckColor)
            val inactiveIconColor =
                ContextCompat.getColor(requireContext(), R.color.inactiveIconColor)
            if (binding.collapsingToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(
                    binding.collapsingToolbar
                )
            ) {
                //TODO: - AppBar collapsed mode
                binding.iconLike.visibility = View.VISIBLE
                binding.toolbar.navigationIcon?.setColorFilter(inactiveIconColor, PorterDuff.Mode.SRC_IN)
                binding.iconShare.setColorFilter(inactiveIconColor)
                binding.iconMore.setColorFilter(inactiveIconColor)
                binding.layDots.visibility = View.GONE
            } else {
                //TODO: - AppBar expanded mode
                binding.iconLike.visibility = View.GONE
                binding.layDots.visibility = View.VISIBLE
                binding.toolbar.navigationIcon?.setColorFilter(activeIconColor,PorterDuff.Mode.SRC_IN)
                binding.iconShare.setColorFilter(activeIconColor)
                binding.iconMore.setColorFilter(activeIconColor)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configureContent(){
        binding.appBar.addOnOffsetChangedListener { _, verticalOffset ->
            val activeIconColor = ContextCompat.getColor(requireContext(), R.color.defaultTextBckColor)
            val inactiveIconColor =
                ContextCompat.getColor(requireContext(), R.color.inactiveIconColor)
            if (binding.collapsingToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(
                    binding.collapsingToolbar
                )
            ) {
                //TODO: - AppBar collapsed mode
                binding.iconLike.visibility = View.VISIBLE
                binding.toolbar.navigationIcon?.setColorFilter(inactiveIconColor, PorterDuff.Mode.SRC_IN)
                binding.iconShare.setColorFilter(inactiveIconColor)
                binding.iconMore.setColorFilter(inactiveIconColor)
                binding.layDots.visibility = View.GONE
            } else {
                //TODO: - AppBar expanded mode
                binding.iconLike.visibility = View.GONE
                binding.layDots.visibility = View.VISIBLE
                binding.toolbar.navigationIcon?.setColorFilter(activeIconColor,PorterDuff.Mode.SRC_IN)
                binding.iconShare.setColorFilter(activeIconColor)
                binding.iconMore.setColorFilter(activeIconColor)
            }
        }
        binding.clickerExpand.setOnClickListener {
           // print(isDescExpanded);
            if(announcementDetailViewModel.isExpandedText.value){
                binding.content.maxLines = Int.MAX_VALUE
                binding.clickerExpand.text = resources.getText(R.string.less)
                binding.clickerExpand.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.drawable.vicon_expand_less),
                    null
                )
            }else{
                binding.content.maxLines = 7
                binding.clickerExpand.text = resources.getText(R.string.more)
                binding.clickerExpand.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.drawable.vicon_more_bottom),
                    null
                )
            }
            announcementDetailViewModel.setActiveSegment(!announcementDetailViewModel.isExpandedText.value)
           // isDescExpanded = !isDescExpanded
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
           // val dot1: View =binding.root.findViewById(R.id.dot1)
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