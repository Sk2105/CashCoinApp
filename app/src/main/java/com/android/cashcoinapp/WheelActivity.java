package com.android.cashcoinapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class WheelActivity extends AppCompatActivity {
    ImageView wheel_icon;
    Button spin;
    ImageView backBtn;
    DocumentReference reference;

    Boolean spinning;

    String res = "";
    RewardedInterstitialAd rewardedInterstitialAd;
    RewardAdClass rewardAdClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        spinning = false;
        new ToastClass(getApplicationContext());

        rewardAdClass = new RewardAdClass(this, rewardedInterstitialAd, "ca-app-pub-3397903282571414/3188161035"
                , new AdRequest.Builder().build());
        reference =
                FirebaseData.getDocumentReference().collection(FirebaseData.WHEEL_SPIN).document(FirebaseData.getCurrentTime());
        wheel_icon = findViewById(R.id.Wheel_icon);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> onBackPressed());
        spin = findViewById(R.id.spin);
        spin.setOnClickListener(v -> startAnimation());
    }


    public void startTime() {
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int min = (int) (millisUntilFinished / 1000) / 60;
                int sec = (int) (millisUntilFinished / 1000) % 60;
                spinning = true;
                String t = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                spin.setText(t);
                spin.setEnabled(false);
            }

            @Override
            public void onFinish() {
                spin.setText(getText(R.string.spin));
                spinning = false;
                spin.setEnabled(true);
            }
        }.start();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void startAnimation() {
        Random r = new Random();
        int d = r.nextInt(360);
        RotateAnimation rotate = new RotateAnimation(0, 1440 + d, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(4000);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endCall(d);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        wheel_icon.startAnimation(rotate);
    }


    private void endCall(int d) {
        if ((d >= 335 || d < 20) && d > 0) {
            res = "1";
        } else if (d >= 20 && d < 65) {
            res = "0";
        } else if (d >= 65 && d < 110) {
            res = "7";
        } else if (d >= 110 && d < 155) {
            res = "6";
        } else if (d >= 155 && d < 200) {
            res = "5";
        } else if (d >= 200 && d < 245) {
            res = "4";
        } else if (d >= 245 && d < 290) {
            res = "3";
        } else if (d >= 290 && d < 335) {
            res = "2";
        }
        rewardAdClass.interAds(() -> {
            startTime();
            setSpinLimit();
        });


    }

    public void addReward() {
        FirebaseData.updateBalance(Integer.parseInt(res));
    }

    public void setSpinLimit() {
        Map<String, String> map = new HashMap<>();
        map.put(FirebaseData.SPINNING, "1");
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getData() != null) {
                int d = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString(FirebaseData.SPINNING)));
                if (d >= 5) {
                    ToastClass.toast("You Spin 5 Times Daily");
                } else {
                    int i =
                            d + 1;
                    updateLimit(String.valueOf(i));
                }
            } else {
                setLimit(map);
            }
        });
    }

    private void updateLimit(String i) {
        reference.update(FirebaseData.SPINNING, i).addOnSuccessListener(unused -> addReward());

    }

    private void setLimit(Map<String, String> map) {
        reference.set(map).addOnSuccessListener(unused -> addReward()).addOnFailureListener(e -> ToastClass.toast("Something with wroung"));
    }


}