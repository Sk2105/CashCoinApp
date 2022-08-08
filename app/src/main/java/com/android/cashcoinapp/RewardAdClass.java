package com.android.cashcoinapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

public class RewardAdClass {
    Context context;
    RewardedInterstitialAd ad;
    String adId;
    AdRequest adRequest;

    public RewardAdClass(Context context, RewardedInterstitialAd ad, String adId, AdRequest adRequest) {
        this.context = context;
        this.ad = ad;
        this.adId = adId;
        this.adRequest = adRequest;

    }

    public void interAds(InterfaceClass InterfaceClass) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RewardedInterstitialAd.load(context, adId, adRequest, new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                dialog.dismiss();
                ToastClass.toast("Something with wroung");
                ad = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                super.onAdLoaded(rewardedInterstitialAd);
                ad = rewardedInterstitialAd;
                dialog.dismiss();
                ad.setFullScreenContentCallback(new FullScreenContentCallback() {


                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        InterfaceClass.start();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        ad = null;
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });
                if (ad != null) {
                    dialog.dismiss();
                    ad.show((Activity) context, rewardItem -> {

                    });
                } else {
                    dialog.dismiss();
                    ToastClass.toast("Something with wroung");
                }

            }
        });

    }

}


