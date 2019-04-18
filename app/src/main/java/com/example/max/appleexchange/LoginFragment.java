package com.example.max.appleexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;

public class LoginFragment extends Fragment {

    private Button login;
    private Button register;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login,container,false);
        mAuth = FirebaseAuth.getInstance();
        email=v.findViewById(R.id.editTextEmailLog);
        password=v.findViewById(R.id.editTextPasswordLog);
        register=(Button)v.findViewById(R.id.button_singup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RegistrationFragment()).commit();
            }
        });
        login=v.findViewById(R.id.button_singin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(email)&&isEmpty(password))
                    signInWithEmailAndPassword(email,password);
            }
        });
        navigationView=getActivity().findViewById(R.id.nav_view);


        return v;

    }



    private void signInWithEmailAndPassword(final EditText email, final EditText password) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            after_login_state();
                            email.getText().clear();
                            password.getText().clear();
                            changeFragment();
                            Toast.makeText(getActivity(), "Logowanie powiodło się",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Logowanie  się nie powiodło",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void after_login_state()
    {
        if(FirebaseAuth.getInstance()!=null) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
        }else{
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        }

    }

    private boolean isEmpty(EditText input)
    {
        String sInput=input.getText().toString().trim();
        if(sInput.isEmpty()){
            input.setError("Pole nie może być puste");
            return false;
        } else{
            input.setError(null);
            return true;
        }
    }

    private void changeFragment(){


        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
