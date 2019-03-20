package com.example.max.appleexchange;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginFragment extends Fragment {
    private MainActivity mainActivity;
    private Toolbar toolbar;

    private Button register;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_login,container,false);

        View v = inflater.inflate(R.layout.fragment_login,container,false);
        register=(Button)v.findViewById(R.id.button_singup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RegistrationFragment()).commit();
            }
        });


        return v;


    }

}