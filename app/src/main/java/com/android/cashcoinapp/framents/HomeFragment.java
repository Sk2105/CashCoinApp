package com.android.cashcoinapp.framents;


import android.content.Intent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Toast;


import com.android.cashcoinapp.RewardAdClass;
import com.android.cashcoinapp.FirebaseData;
import com.android.cashcoinapp.PolicyActivity;
import com.android.cashcoinapp.R;
import com.android.cashcoinapp.ToastClass;
import com.android.cashcoinapp.WheelActivity;
import com.google.android.gms.ads.AdListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

import java.util.Map;


public class HomeFragment extends Fragment {
    Button dailyClaim, spinBtn, policy, rules;
    boolean spin;
    DocumentReference reference;
    RewardedInterstitialAd ad;
    AdRequest adRequest;
    AdView adView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container,
                false);
        new ToastClass(getContext());

        spin = false;
        adView = view.findViewById(R.id.adView);
        loadAd();
        adRequest = new AdRequest.Builder().build();
        RewardAdClass rewardAdClass = new RewardAdClass(getContext(), ad,
                "ca-app-pub-3397903282571414/7099485888",
                adRequest);
        spinBtn = view.findViewById(R.id.spinBtn);
        dailyClaim = view.findViewById(R.id.claimBtn);
        policy = view.findViewById(R.id.policy);
        rules = view.findViewById(R.id.rules);
        policy.setOnClickListener(v -> {
            requireContext().startActivity(new Intent(getContext(), PolicyActivity.class));
            requireActivity().finish();
        });
        rules.setOnClickListener(v -> Toast.makeText(getContext(), "Show Rules",
                Toast.LENGTH_SHORT).show());
        dailyClaim.setOnClickListener(v -> rewardAdClass.interAds(this::checkDailyClaim));

        String time = FirebaseData.getCurrentTime();

        reference =
                FirebaseData.getDocumentReference().collection(FirebaseData.DAILY_REWARD).document(time);
        spinBtn.setOnClickListener(v -> {
            if (!spin) {
                rewardAdClass.interAds(this::addCoins);
            }
        });
        return view;
    }

    private void checkDailyClaim() {
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getData() == null) {
                setData();
            } else {
                Boolean b = documentSnapshot.getBoolean(FirebaseData.CLAIM);
                if (Boolean.TRUE.equals(b)) {
                    ToastClass.toast("You Already Claimed");
                } else {
                    setData();
                }

            }
        });
    }

    public void setData() {
        Map<String, Boolean> map = new HashMap<>();
        map.put(FirebaseData.CLAIM, true);
        reference.set(map).addOnSuccessListener(unused -> FirebaseData.updateBalance(100))
                .addOnFailureListener(e -> ToastClass.toast("Failed"));
    }


    public void addCoins() {
        if (!spin) {
            requireContext().startActivity(new Intent(getContext(),
                    WheelActivity.class));
        } else {
            ToastClass.toast("Failed");
        }

    }

    public void loadAd() {
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });

    }


}