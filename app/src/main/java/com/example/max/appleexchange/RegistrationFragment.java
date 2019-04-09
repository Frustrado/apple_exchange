package com.example.max.appleexchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.max.appleexchange.patterns.patternClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegistrationFragment extends Fragment {
    private FirebaseAuth mAuth;

    private FirebaseFirestore mDatabase;

    private EditText email;
    private EditText password;
    private Button register;
    private EditText name;
    private EditText phonenr;
    private EditText password2;
    private Button login;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration,container,false);
        email= v.findViewById(R.id.editTextEmail);
        password=v.findViewById(R.id.editTextPassword);
        register=v.findViewById(R.id.button_singin);
        name=v.findViewById(R.id.editTextName);
        phonenr=v.findViewById(R.id.editTextPhonenr);
        password2=v.findViewById(R.id.editTextPassword2);
        login=v.findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail(email)&&isEmpty(name)&&validateEquation(password,password2)&&validatePassword(password)&&isEmpty(phonenr))
                createAccount(email,password);
                else
                    Toast.makeText(getActivity(), "Wprowadz poprawne dane", Toast.LENGTH_SHORT).show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmpty(name);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty(name);
            }
        });

        phonenr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmpty(phonenr);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty(phonenr);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(password);
                validateEquation(password,password2);
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(password);

            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEquation(password,password2);
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEquation(password,password2);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(email);
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(email);
            }
        });


        return v;
    }

    public void createAccount(final EditText email, final EditText password){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeNewUser(task.getResult().getUser().getUid(),name.getText().toString(),phonenr.getText().toString());
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getActivity(), "Rejestracja przebiegła pomyślnie", Toast.LENGTH_SHORT).show();
                            //change fragment view
                            changeFragment();
                            email.getText().clear();
                            password.getText().clear();
                            password2.getText().clear();
                            name.getText().clear();
                            phonenr.getText().clear();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            password.getText().clear();
                            password2.getText().clear();
                            Toast.makeText(getActivity(), "Wprowadzono błędne dane", Toast.LENGTH_SHORT).show();
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

    private void changeFragment(){
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean validatePassword(EditText password){
        String passwordInput=password.getText().toString().trim();
        if(passwordInput.isEmpty()){
            password.setError("Pole nie może być puste");
            return false;
        } else if(!patternClass.PASSWORD_PATTERN.matcher(passwordInput).matches()){
            password.setError("Haslo za slabe(min.6znaków,bez spacji,duza litera,cyfra");
            return false;
        }

        else{
            password.setError(null);
            return true;
        }

    }

    private boolean validateEquation(EditText password1,EditText password2){
        String passwordInput1=password1.getText().toString().trim();
        String passwordInput2=password2.getText().toString().trim();
        if(passwordInput1.equals(passwordInput2)){
            return true;
        } else {
            password2.setError("Hasła muszą być takie same");
            return false;
        }

    }

    private boolean validateEmail(EditText email){
        String emailInput=email.getText().toString().trim();
        if(emailInput.isEmpty()){
            email.setError("Pole nie może być puste");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            email.setError("Wprowadz poprawny email");
            return false;
        }

        else{
            email.setError(null);
            return true;
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


}



