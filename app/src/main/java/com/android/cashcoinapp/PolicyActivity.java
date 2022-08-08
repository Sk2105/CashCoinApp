package com.android.cashcoinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class PolicyActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        webView = findViewById(R.id.View);
        webView.loadUrl("https://www.app-privacy-policy.com/live.php?token=Za5GjoyDWubVA6LO4CKr1XuevMlhyJaA");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}