package com.example.torchvisionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.torchvisionapp.databinding.ActivitySendCodeVerifyBinding;

public class SendCodeVerifyActivity extends AppCompatActivity {
    ActivitySendCodeVerifyBinding binding;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code_verify);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_code_verify);

        btnVerify = binding.btnVerifycode;
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerificationCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}