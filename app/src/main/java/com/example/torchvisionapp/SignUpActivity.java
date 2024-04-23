package com.example.torchvisionapp;

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

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    Button btnSignUp;
    TextView txtSignIn;
    EditText editEmail;
    Button btnContinueWithGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_sign_up
        );

        btnSignUp = binding.btnSignUp;

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SendCodeVerifyActivity.class);
                startActivity(i);
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
                }
                else {
                    v.setBackground(getDrawable(R.drawable.input_text_custom));
                }
            }
        });
    }
}