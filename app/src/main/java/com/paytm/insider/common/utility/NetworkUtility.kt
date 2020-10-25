package com.paytm.insider.common.utility

import android.content.Context
import android.net.ConnectivityManager
import org.koin.core.KoinComponent

class NetworkUtility(private val context: Context) : KoinComponent {
    fun isDeviceConnectedToInternet(): Boolean {
        val connectivityManager =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE)) as ConnectivityManager?

        return connectivityManager?.activeNetworkInfo?.isConnected ?: false
    }
}