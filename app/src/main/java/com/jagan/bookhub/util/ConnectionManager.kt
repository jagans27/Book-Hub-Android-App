package com.jagan.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

//checks connectivity
class ConnectionManager {

    fun checkConnectivity(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        return if (activeNetwork?.isConnected != null) {
            activeNetwork.isConnected
        } else {
            false
        }
    }
}