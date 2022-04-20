package com.example.easyads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InterstitialAds.getInstance().loadGoogleAdmobSplashInterstitial(
            context = this,
            googleAdsId = "ca-app-pub-3940256099942544/1033173712",
            isPurchased = true,
            shouldShowAd = true
        )

        findViewById<Button>(R.id.btn).setOnClickListener {
            InterstitialAds.getInstance().showSplashInterstitialAd(this)
        }
    }
}