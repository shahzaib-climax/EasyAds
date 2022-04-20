package com.example.easyads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAds {


    private var mSplashInterstitialAd: InterstitialAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    private var appLovinSplashInterstitialAd: MaxInterstitialAd? = null
    private var appLovinInterstitialAd: MaxInterstitialAd? = null


    companion object {
        @Volatile
        private var instance: InterstitialAds? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: InterstitialAds().also { instance = it }
            }
    }

    //Splash ADs

    //load google admob interstitial only
    fun loadGoogleAdmobSplashInterstitial(
        context: Context,
        googleAdsId: String,
        isPurchased: Boolean,
        shouldShowAd: Boolean
    ) {
        if (isPurchased) {
            if (shouldShowAd) {
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(context, googleAdsId, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mSplashInterstitialAd = interstitialAd
                            Log.d("TAG", "onAdLoaded")
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            mSplashInterstitialAd = null
                            Log.d("TAG", "LoadAdError ${loadAdError.message}")

                        }
                    })
            }
        }

    }


    //loading applovin interstitial only
    fun loadAppLovinSplashInterstitialAd(activity: Activity, appLovinAdsId: String, isPurchased : Boolean, shouldShowAd : Boolean) {

        if (!isPurchased){
            if (shouldShowAd){
                //loading applovin interstitial Ad
                appLovinSplashInterstitialAd =
                    MaxInterstitialAd(appLovinAdsId, activity)
                appLovinSplashInterstitialAd!!.setListener(object : MaxAdListener {
                    override fun onAdLoaded(ad: MaxAd) {}
                    override fun onAdDisplayed(ad: MaxAd) {}
                    override fun onAdHidden(ad: MaxAd) {}
                    override fun onAdClicked(ad: MaxAd) {}
                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                    }

                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {}
                })
                // Load the first ad
                appLovinSplashInterstitialAd!!.loadAd()
            }
        }

    }


    //mediation interstitial
    fun loadMediationSplashInterstitial(
        activity: Activity,
        context: Context,
        googleAdsId: String,
        appLovinAdsId: String,
        priority: String,
        isPurchased : Boolean,
        shouldShowAd : Boolean
    ) {
        if (!isPurchased){
            if (shouldShowAd){
                if (priority == "admob/applovin") {
                    //load admob first then applovin
                    loadGoogleAdmobSplashInterstitial(context, googleAdsId, isPurchased, shouldShowAd)
                } else {
                    //load applovin first then admob
                    loadAppLovinSplashInterstitialAd(activity, appLovinAdsId, isPurchased, shouldShowAd)
                }
            }

        }

    }


    //showing splash AD
    fun showSplashInterstitialAd(activity: Activity) {
        if (mSplashInterstitialAd != null) {
            mSplashInterstitialAd!!.show(activity)
            mSplashInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {}

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}

                    override fun onAdShowedFullScreenContent() {
                        mSplashInterstitialAd = null
                    }
                }
        } else {
            if (appLovinSplashInterstitialAd != null) {
                if (appLovinSplashInterstitialAd!!.isReady) {
                    appLovinSplashInterstitialAd!!.showAd()
                }
            }
        }
    }


}