package com.example.uzbengtranslator.activity

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.uzbengtranslator.R
import com.example.uzbengtranslator.databinding.ActivitySplashBinding
import com.example.uzbengtranslator.receiver.ConnectionBroadcastReceiver

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    lateinit var connectionBroadcastReceiver: ConnectionBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLightStatusBar()
        initViews()
    }

    private fun initViews() {
        val topToCenter = AnimationUtils.loadAnimation(this, R.anim.top_to_center)
        val bottomToCenter = AnimationUtils.loadAnimation(this, R.anim.bottom_to_center)
        binding.ivSplash.startAnimation(topToCenter)
        binding.tvSplash.startAnimation(bottomToCenter)
        bottomToCenter.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                notifyInternetConnection()
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })
    }

    private fun notifyInternetConnection() {
        connectionBroadcastReceiver = ConnectionBroadcastReceiver { isInternetOk ->
            if (isInternetOk) {
                callMainActivity()
                finish()
            } else {
                binding.apply {
                    ivSplash.setImageResource(R.drawable.ic_internet_off)
                    tvSplash.text = "Internet bilan muammo"
                }
            }
        }
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectionBroadcastReceiver, intentFilter)
    }

    private fun callMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectionBroadcastReceiver)
    }

    //setting light mode status bar
    private fun setLightStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val view = View(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags: Int = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            window.statusBarColor = Color.WHITE
        }
    }
}