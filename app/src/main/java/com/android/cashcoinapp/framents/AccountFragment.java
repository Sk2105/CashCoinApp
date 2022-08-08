package com.android.cashcoinapp.framents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cashcoinapp.FirebaseData;
import com.android.cashcoinapp.R;
import com.android.cashcoinapp.SignInActivity;
import com.android.cashcoinapp.ToastClass;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


public class AccountFragment extends Fragment {

    Context context;
    ImageView img;
    TextView name, email;
    Button signOut;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        new ToastClass(getContext());
        img = view.findViewById(R.id.img);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        signOut = view.findViewById(R.id.signOut);
        // Sign Out Button
        signOut.setOnClickListener(v -> {
            try {
                FirebaseData.logOut();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getContext().startActivity(new Intent(getContext(), SignInActivity.class));
                getActivity().finish();
            }
        });
        // Set Account Atrebuite..
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        Glide.with(getContext()).load(account.getPhotoUrl()).into(img);
        name.setText(account.getDisplayName());
        email.setText(account.getEmail());

        return view;
    }
}