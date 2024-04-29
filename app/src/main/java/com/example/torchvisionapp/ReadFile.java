package com.example.torchvisionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityReadFileBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class ReadFile extends AppCompatActivity {
    ActivityReadFileBinding binding;

    Button btnShare;

    TextView txtContent;
    String uri;

    private static final int REQUEST_CODE_PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_read_file
        );

        btnShare = binding.btnShare;
        txtContent = binding.contentFile;
        Intent i = getIntent();
        uri = i.getStringExtra("uri");
        Log.i("Path123", uri);
        readText(uri);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile(uri);
                Toast.makeText(getApplicationContext(), "aâa", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void readText(String path) {
                Uri uri = Uri.fromFile(new File(path));
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        reader.close();
                        txtContent.setText(stringBuilder.toString());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    private void shareFile(String path) {
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, file.getName());
        intent.putExtra(Intent.EXTRA_TEXT, txtContent.getText());

        startActivity(Intent.createChooser(intent, "Choose a Platform"));
    }
}
