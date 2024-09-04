package com.goo.vapps.bluetooth.chat.Classes;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.goo.vapps.bluetooth.chat.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Admob {


    public static InterstitialAd mInterstitialAd;

    public static void adMob1(Context context,final AdView mAdView,
                              boolean isConnected){

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        AdRequest adRequest = new AdRequest.Builder().build();


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {

            }
        });



        mAdView.loadAd(adRequest);


            if (isConnected==true) {

                mAdView.setVisibility(View.VISIBLE);
            }else {
                mAdView.setVisibility(View.GONE);
            }







    }








    public static void LoadInterestitialAd(Context context){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,context.getString(R.string.interstitial_full_screen), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.e("TAG", "onAdLoaded");

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.e("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }

}
