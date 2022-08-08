package com.android.cashcoinapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;



public class SignInActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    FirebaseAuth mAuth;
    ProgressDialog progressBar;
    public static final int RC_SIGN_IN = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        new ToastClass(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Sign In");
        gsoCall();
    }

    public void signIn(View view) {
        progressBar.show();
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);

    }

    public void gsoCall() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.webClintKey)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }


    private void handleSignInResult(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),
                null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> gotoProfile()).addOnFailureListener(e -> ToastClass.toast("Failed"));
    }


    private void gotoProfile() {

        FirebaseData.getDocumentReference().get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getData() == null) {
                progressBar.dismiss();
                FirebaseData.createDataBase();
            } else {
                progressBar.dismiss();

            }
            intentCall();
        });


    }

    private void intentCall() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            intentCall();
        }
    }


}
