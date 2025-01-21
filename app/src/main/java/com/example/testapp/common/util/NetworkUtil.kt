package com.example.testapp.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.testapp.R
import com.google.android.material.snackbar.Snackbar

object NetworkUtil {

    private var isToastShown = false
    private var toast: Toast? = null

    /**
     * Checks if the internet connection is available.
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    /**
     * Displays a custom toast for no internet connection.
     */
    fun showNoInternetToast(parentView: View) {
        if (isToastShown) return // Prevent showing duplicate toasts

        isToastShown = true
        val snackbar = Snackbar.make(parentView, "No connection", Snackbar.LENGTH_LONG)
        // Change the Snackbar's background color
        snackbar.view.background = parentView.context.getDrawable(R.drawable.snackbar_background)

        // Change the text color of the main message
        snackbar.setTextColor(parentView.context.getColor(R.color.white))

        snackbar.show()

        // Reset the toast flag after the duration (2 seconds for LENGTH_SHORT)
        Handler(Looper.getMainLooper()).postDelayed({
            resetToastFlag()
        }, 2000)
    }

    /**
     * Resets the toast flag to allow showing the toast again.
     */
     fun resetToastFlag() {
        isToastShown = false
    }
}
