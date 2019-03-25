package com.example.max.appleexchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.max.appleexchange.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegistrationFragment extends Fragment {
    private FirebaseAuth mAuth;
    //private DatabaseReference mDatabase;
    private FirebaseFirestore mDatabase;

    private EditText email;
    private EditText password;
    private Button register;
    private EditText name;
    private EditText phonenr;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_login,container,false);
        View v = inflater.inflate(R.layout.fragment_registration,container,false);
        email= v.findViewById(R.id.editTextEmail);
        password=v.findViewById(R.id.editTextPassword);
        register=v.findViewById(R.id.button_singin);
        name=v.findViewById(R.id.editTextName);
        phonenr=v.findViewById(R.id.editTextPhonenr);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(email.getText().toString(),password.getText().toString());
            }
        });

        return v;
    }

    public void createAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeNewUser(task.getResult().getUser().getUid(),name.getText().toString(),phonenr.getText().toString());
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            //Toast.makeText(getContext(), "Authentication failed.",
                                    //Toast.LENGTH_SHORT);
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }




    private void writeNewUser(String userId, String name, String phonenr)
    {
        Map<String,Object> user=new HashMap<>();
        user.put("name",name);
        user.put("phone",phonenr);
        mDatabase.collection("users").document(userId).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "UserDatabase:success");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "UserDatabase:failure");
                    }
                });

    }


}
