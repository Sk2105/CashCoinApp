package com.android.cashcoinapp.framents;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cashcoinapp.FirebaseData;
import com.android.cashcoinapp.R;
import com.android.cashcoinapp.ToastClass;


public class MyWalletFragment extends Fragment {
    TextView textView;
    Button redeem;

    public MyWalletFragment(Context context) {
        this.context = context;
    }

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        textView = view.findViewById(R.id.coinsId);
        new ToastClass(getContext());

        redeem = view.findViewById(R.id.redeem);
        getData(textView);
        redeem.setOnClickListener(v -> {
            if (Integer.parseInt(textView.getText().toString()) <= 500) {
                ToastClass.toast("Redeem min < 500");

            }
        });

        return view;

    }

    public void getData(TextView t) {
        FirebaseData.getDocumentReference().get().addOnSuccessListener(documentSnapshot -> {
            String a = documentSnapshot.getString("Balance");
            t.setText(a);
        });
    }

}