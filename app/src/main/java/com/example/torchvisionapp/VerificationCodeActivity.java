package com.example.torchvisionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.torchvisionapp.databinding.ActivityMainScreenAppBinding;
import com.example.torchvisionapp.databinding.ActivityVerificationCodeBinding;

public class VerificationCodeActivity extends AppCompatActivity {

    ActivityVerificationCodeBinding binding;
    Button btnVerify;
    TextView textSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        btnVerify = binding.btnContinue;
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textSignIn = binding.textSignIn;
        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}