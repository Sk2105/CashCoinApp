package com.android.cashcoinapp;


import android.content.Context;

import android.widget.Toast;

public class ToastClass {
    static Context context;

    public ToastClass(Context context) {
        ToastClass.context = context;
    }

    public static void toast( String s) {
        Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

}
