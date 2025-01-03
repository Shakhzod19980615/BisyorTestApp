package com.example.testapp.presentation.chat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.databinding.WindowChatContainerBinding
import com.example.testapp.presentation.announcementDetail.fragment.FragmentAnnouncementDetail
import com.example.testapp.presentation.chat.adapter.UserChatsAdapter
import com.example.testapp.presentation.chat.viewModel.FragmentChatContainerVM
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentChatContainer: Fragment(R.layout.window_chat_container) {
    private var binding : WindowChatContainerBinding by Delegates.notNull()
    private val viewModel: FragmentChatContainerVM by viewModels()
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserChatsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WindowChatContainerBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tab_layout)
        recyclerView = view.findViewById(R.id.list_message)
        adapter = UserChatsAdapter(layoutInflater,
            onItemClicked = {itemId->
                activity?.supportFragmentManager?.commit {
                    replace<FragmentMessanger>(
                        containerViewId= R.id.fragment_container_view_tag,
                        args = bundleOf("itemId" to itemId)
                    ).addToBackStack("replacement")
                }

            })
        recyclerView.adapter = adapter
        // Set default tab to the first tab (position 0)
        view.post {
            val defaultTab = tabLayout.getTabAt(0)
            defaultTab?.select()

            // Explicitly load data for the first tab
            getChatItemList(1)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0->getChatItemList(1)
                    1->getChatItemList(2)
                    2->getChatItemList(3)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }

    private fun getChatItemList(tabIndex: Int){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val request = ChatRequest("ru", 0)
        viewModel.getChats(Pair(tabIndex,request) )
        lifecycleScope.launch {
            viewModel.allChats.collect{resource->
                when(resource){
                    is Resource.Success->{
                        adapter.setItems(resource.data)
                    }
                    is Resource.Error->{
                        showAlertDialog(resource.message.toString())
                    }
                    is Resource.Loading->{

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