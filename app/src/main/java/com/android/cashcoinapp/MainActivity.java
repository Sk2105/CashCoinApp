package com.android.cashcoinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.android.cashcoinapp.framents.AccountFragment;
import com.android.cashcoinapp.framents.HomeFragment;
import com.android.cashcoinapp.framents.MyWalletFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView coinId;
    GoogleSignInAccount account;

    FrameLayout frameLayout;
    BottomNavigationView navigationView;
    LinearLayout balance;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ToastClass(getApplicationContext());

        MobileAds.initialize(this);
        account = GoogleSignIn.getLastSignedInAccount(this);
        img = findViewById(R.id.profileImg);
        balance = findViewById(R.id.bl);
        frameLayout = findViewById(R.id.framelayout);
        coinId = findViewById(R.id.coinsId);
        setImg();
        getData(coinId);
        refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setOnRefreshListener(() -> {
            getData(coinId);
            setNavigationView(R.id.home, new HomeFragment(), 0);
            refreshLayout.setRefreshing(false);
            ToastClass.toast("Refresh..");
        });

        navigationView = findViewById(R.id.bottomNavigationBar);
        setNavigationView(R.id.home, new HomeFragment(), 1);
        balance.setOnClickListener(v -> setNavigationView(R.id.wallet, new MyWalletFragment(getApplicationContext()), 0));
        navigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.home:
                    setFragment(new HomeFragment(), 0);
                    break;
                case R.id.wallet:
                    setFragment(new MyWalletFragment(getApplicationContext()), 0);
                    break;
                case R.id.account:
                    setFragment(new AccountFragment(), 0);
                    break;
            }
            return true;
        });


    }

    private void setFragment(Fragment fragment, int i) {
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        if (i == 1) {
            manager.add(R.id.framelayout, fragment);
        } else {
            manager.replace(R.id.framelayout, fragment);
        }
        manager.commit();

    }


    public void setImg() {
        Glide.with(this).load(account.getPhotoUrl()).into(img);
    }

    public void imgClick(View view) {
        setNavigationView(R.id.account, new AccountFragment(), 0);
    }

    public void setNavigationView(int res, Fragment fragment, int i) {
        navigationView.setSelectedItemId(res);
        setFragment(fragment, i);
    }

    public void getData(TextView t) {
        FirebaseData.getDocumentReference().get().addOnSuccessListener(documentSnapshot -> {
            String a = documentSnapshot.getString(FirebaseData.USER_BALANCE);
            t.setText(a);
        }).addOnFailureListener(e -> ToastClass.toast("Something with wroung"));
    }


}