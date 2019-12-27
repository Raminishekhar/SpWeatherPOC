package com.tebs.spgroupweatherpoc.Utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo



class AppUtil {

    companion object {
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        }
    }
}