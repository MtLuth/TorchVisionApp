package com.example.torchvisionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    Button btnSignUp;
    TextView txtSignIn;
    EditText editEmail;
    EditText editPass;
    Button btnContinueWithGuest;

    //    sign up
    private EditText emailTextView, passwordTextView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //        initial instance for firebaseauth
        mAuth = FirebaseAuth.getInstance();

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_sign_up
        );

        btnSignUp = binding.btnSignUp;


        emailTextView = findViewById(R.id.editEmail);
        passwordTextView = findViewById(R.id.editPassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), SendCodeVerifyActivity.class);
//                startActivity(i);
                registerNewUser();
            }
        });

        btnContinueWithGuest = binding.btnContinueWithGuest;
        btnContinueWithGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainScreenAppActivity.class);
                startActivity(i);
            }
        });

        txtSignIn = binding.textSignIn;
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        });

        editEmail = binding.editEmail;
        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(getDrawable(R.drawable.edit_text_actived));
                } else {
                    v.setBackground(getDrawable(R.drawable.input_text_custom));
                }
            }
        });

        editPass = binding.editPassword;
        editPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(getDrawable(R.drawable.edit_text_actived));
                } else {
                    v.setBackground(getDrawable(R.drawable.input_text_custom));
                }
            }
        });
    }

    private void registerNewUser() {
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your email !!", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your password !!", Toast.LENGTH_LONG).show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registered successfully !!", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed !!" + " Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
