package com.example.torchvisionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.torchvisionapp.databinding.ActivityContactUsBinding;

public class ContactUs extends AppCompatActivity {

    EditText txtSubject, txtBody;

    Button sendContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ActivityContactUsBinding binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_contact_us
        );

        txtSubject = binding.editSubject;
        txtBody = binding.editBody;

        sendContactUs = binding.btnSend;

        sendContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);

            }
        });
    }

    private void sendEmail(View v) {
        String emailsubject = txtSubject.getText().toString();
        String emailBody = txtBody.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"taihk2@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        intent.setType("message/rfc822");

        // startActivity with intent with chooser as Email client using createChooser function
        startActivity(intent);

        txtSubject.setText("");
        txtBody.setText("");
    }

}