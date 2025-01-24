package com.example.testapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.testapp.common.util.NetworkUtil

abstract class BaseFragment : Fragment() {
    private var isNetworkAvailable = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isNetworkAvailable = NetworkUtil.isInternetAvailable(requireContext())

        // Show no internet toast if there's no network
        if (!isNetworkAvailable) {
            NetworkUtil.showNoInternetToast(requireView())
        }
    }
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            NetworkUtil.resetToastFlag()
            val isConnected = NetworkUtil.isInternetAvailable(requireContext())

            if (isConnected && !isNetworkAvailable) {
                // Network was restored, update the fragment accordingly
                isNetworkAvailable = true
                onNetworkRestored() // Notify the fragment that the network is back
            } else if (!isConnected && isNetworkAvailable) {
                // Network is lost, update the fragment accordingly
                isNetworkAvailable = false
                onNetworkLost() // Notify the fragment that the network is lost
            }

        }
    }
    open fun onNetworkRestored() {
        // Default implementation is empty
        // In FragmentHome, you can override this method to refresh UI, e.g., re-fetch data
    }

    // Override this in the fragment to handle network lost
    open fun onNetworkLost() {
        // Default implementation is empty
    }
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(networkReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(networkReceiver)
    }

}