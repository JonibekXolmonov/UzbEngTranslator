package com.example.uzbengtranslator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class ConnectionBroadcastReceiver(private var onConnectionChange: ((Boolean) -> Unit)) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onConnectionChange.invoke(isConnectingToInternet(context!!))
    }

    private fun isConnectingToInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}