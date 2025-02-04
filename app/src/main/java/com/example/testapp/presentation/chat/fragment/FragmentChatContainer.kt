package com.example.testapp.presentation.chat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.BaseFragment
import com.example.testapp.R
import com.example.testapp.common.Resource
import com.example.testapp.common.util.NetworkUtil
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.databinding.WindowChatContainerBinding
import com.example.testapp.presentation.chat.adapter.UserChatsAdapter
import com.example.testapp.presentation.chat.viewModel.FragmentChatContainerVM
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentChatContainer: BaseFragment() {
    private var binding : WindowChatContainerBinding by Delegates.notNull()
    private val viewModel: FragmentChatContainerVM by viewModels()
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserChatsAdapter

    companion object {
        const val ALL_Messages = 0
        const val BUY_Messages = 1
        const val SELL_Messages = 2
    }
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
            onItemClicked = {chatId->
                activity?.supportFragmentManager?.commit {
                    replace<FragmentMessanger>(
                        containerViewId= R.id.fragment_container_view_tag,
                        args = bundleOf("chatId" to chatId)
                    ).addToBackStack("replacement")
                }

            })
        recyclerView.adapter = adapter
        // Set default tab to the first tab (position 0)
        view.post {
            val defaultTab = tabLayout.getTabAt(ALL_Messages)
            defaultTab?.select()

            // Explicitly load data for the first tab
            getChatList(ALL_Messages)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    ALL_Messages->getChatList(ALL_Messages)
                    BUY_Messages->getChatList(BUY_Messages)
                    SELL_Messages->getChatList(SELL_Messages)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }

    override fun onNetworkRestored() {
        super.onNetworkRestored()
        getChatList(ALL_Messages)
    }

    override fun onNetworkLost() {
        super.onNetworkLost()
        NetworkUtil.showNoInternetToast(requireView())
    }
    private fun getChatList(tabIndex: Int){
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
                        showAlertDialog(resource.message)
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