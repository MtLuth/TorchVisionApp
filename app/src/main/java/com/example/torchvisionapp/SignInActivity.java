package com.example.torchvisionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    Button btnSignIn, btnContinueWithGuest;
    TextView txtSignUp;

    //    sign in
    private EditText emailTextView, passwordTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        initial instance for firebaseauth
        mAuth = FirebaseAuth.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        btnSignIn = binding.btnSignIn;

        emailTextView = findViewById(R.id.editEmail);
        passwordTextView = findViewById(R.id.editPassword);

        emailTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(getDrawable(R.drawable.edit_text_actived));
                } else {
                    v.setBackground(getDrawable(R.drawable.input_text_custom));
                }
            }
        });

        passwordTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(getDrawable(R.drawable.edit_text_actived));
                } else {
                    v.setBackground(getDrawable(R.drawable.input_text_custom));
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        btnContinueWithGuest = binding.btnContinueWithGuest;
        btnContinueWithGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainScreenAppActivity.class);
                startActivity(i);
                finish();
            }
        });

        txtSignUp = binding.textSignUp;
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void loginUserAccount() {
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your email !!", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your password !!", Toast.LENGTH_LONG).show();
            return;
        }

        // sign in existing user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login successfully !!", Toast.LENGTH_LONG).show();

                    // if sign in is successful => navigate to home activity
                    Intent i = new Intent(getApplicationContext(), MainScreenAppActivity.class);
                    i.putExtra("email", emailTextView.getText());
                    startActivity(i);
                } else {
                    // sign in failed
                    Toast.makeText(getApplicationContext(), "Login failed, please check your email or password !!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}