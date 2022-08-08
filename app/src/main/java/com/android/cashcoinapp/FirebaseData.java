package com.android.cashcoinapp;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class FirebaseData {
    public static final String USER_BALANCE = "Balance";
    public static final String USERS = "Users";
    public static final String SPINNING = "Spinning";
    public static final String CLAIM = "Claim";
    public static final String WHEEL_SPIN = "Wheel Spin";
    private static Context context;

    public static final String DAILY_REWARD = "DailyReward";

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(new Date());
    }

    public FirebaseData(Context context) {
        FirebaseData.context = context;
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();

    }

    public static DocumentReference getDocumentReference() {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        return fireStore.collection(USERS).document(getCurrentUser());
    }

    public static String getCurrentUser() {
        return Objects.requireNonNull(getAuth().getCurrentUser()).getUid();
    }

    public static void updateBalance(int win) {
        getDocumentReference().get().addOnSuccessListener(documentSnapshot -> {
            int i = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString(USER_BALANCE))) + win;
            getDocumentReference().update(USER_BALANCE, String.valueOf(i)).addOnSuccessListener(unused -> ToastClass.toast("You Earn +" + win + " Coins"));
        });
    }

    public static void createDataBase() {
        Map<String, String> map = new HashMap<>();
        map.put(USER_BALANCE, "50");
        getDocumentReference().set(map).addOnSuccessListener(unused -> ToastClass.toast("LogIn SuccessFull"));
    }

    public static void logOut() {
        getAuth().signOut();
        ToastClass.toast("SignOut SuccessFull");

    }
}
