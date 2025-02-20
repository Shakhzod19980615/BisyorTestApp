package com.example.testapp.presentation.chat.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.common.util.MyUtil
import com.example.testapp.data.request.chat.MessageRequest
import com.example.testapp.databinding.WindowMessangerBinding
import com.example.testapp.domain.model.chat.ChatSortedByDateModel
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.chat.adapter.GroupedChatAdapter
import com.example.testapp.presentation.chat.viewModel.FragmentMessangerVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentMessanger : Fragment(R.layout.window_messanger) {
    private var binding : WindowMessangerBinding by Delegates.notNull()
    private val viewModel : FragmentMessangerVM by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupedChatAdapter
    private var isKeyboardOpen = false
    private val chatId: Int?
        get() = arguments?.getInt("chatId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowMessangerBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavBar()
        //setupRecyclerView()

        adapter = GroupedChatAdapter(layoutInflater)
        binding.iconBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        // Fetch initial messages
        chatId?.let { id ->
            viewModel.getMessage(MessageRequest(lang = "ru", chatId = id, offset = 0))
        }
        observeVM()
        binding.editTextContainer.addOnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, _, oldBottom ->
            if (oldBottom == 0 || bottom == 0) return@addOnLayoutChangeListener // Ignore first layout event or invalid values

            if (bottom < oldBottom) {
                // Keyboard is OPEN
                binding.sendImage.visibility = View.GONE
                binding.sendFile.visibility = View.GONE
            } else if (bottom > oldBottom) {
                // Keyboard is CLOSED
                binding.sendImage.visibility = View.VISIBLE
                binding.sendFile.visibility = View.VISIBLE
            }
            recyclerView.postDelayed({
                recyclerView.scrollToPosition(0)
            }, 100)
        }

        binding.editMessage.setOnTouchListener { _, _ ->
           Log.d("EditMessage", "EditMessage touched")
           // Hide the buttons when editMessage is touched
           binding.sendFile.visibility = View.GONE
           binding.sendImage.visibility = View.GONE

           // Return false so the EditText still gets focus and other touch behavior
           false
       }

        binding.sendMessage.setOnClickListener {
            sendMessage()
        }

    }
    /*private fun setupRecyclerView() {
        adapter = GroupedChatAdapter(layoutInflater)
        binding.listMessage.adapter = adapter
    }*/
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun observeVM(){

        lifecycleScope.launch {
            viewModel.message.collect{resource->

                when(resource){
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),resource.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        adapter.setLoadingState(true)
                    }
                    is Resource.Success-> {

                        adapter.setLoadingState(false)
                        if (resource.data.items.item_title.isEmpty()){
                            binding.clickerUserAnnouncements.visibility = View.GONE
                        }else{
                            binding.clickerUserAnnouncements.visibility = View.VISIBLE
                            binding.itemId.text = resource.data.items.item_id.toString()
                            binding.itemDesc.text = resource.data.items.item_title
                            Glide.with(binding.root).load(resource.data.items.item_image)
                                .into(binding.itemImg)
                            binding.clickerUserAnnouncements.setOnClickListener {
                                activity?.supportFragmentManager?.commit {
                                    replace<FragmentAnnouncementDetail>(
                                        containerViewId= R.id.fragment_container_view_tag,
                                        args = bundleOf("itemId" to resource.data.items.item_id)
                                    ).addToBackStack("replacement")
                                }
                            }
                        }
                        binding.userName.text = resource.data.liveUser.userFIO
                        binding.userPhonenumber.text =
                            MyUtil.hidePhone(resource.data.liveUser.phone)

                        Glide.with(binding.root).load(resource.data.liveUser.avatar)
                            .into(binding.avatar)
                        recyclerView = binding.listMessage

                        val messageList = resource.data.messages.messagesList
                            .groupBy{MyUtil.formatDateLocalized(requireContext(), it.date_cr, "HH:mm dd.MM.yyyy")}
                            .map{ChatSortedByDateModel(date = it.key, messages = it.value)}
                        recyclerView.adapter = adapter
                        adapter.setItems(messageList)
                        recyclerView.post {
                            recyclerView.scrollToPosition(0)
                        }



                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.sendMessage.collect{resource->
                when(resource){
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),resource.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {}

                    is Resource.Success-> {
                        // Fetch updated messages
                        chatId?.let { id ->
                            viewModel.getMessage(MessageRequest(lang = "ru", chatId = id, offset = 0))
                        }
                    }
                }

            }
        }
    }
    private fun sendMessage(){
        val text = binding.editMessage.text.toString()
        if (text.isNotEmpty()){
           // val request = SendTextMessageRequest("ru", chatId?:0, text)
            viewModel.sendMessage("ru", chatId?:0, text)
            binding.editMessage.text.clear()
            recyclerView.post {
                recyclerView.scrollToPosition(0)
            }
    }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavBar()
    }
}