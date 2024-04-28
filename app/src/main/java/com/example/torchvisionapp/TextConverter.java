package com.example.torchvisionapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityTextConverterBinding;
import com.example.torchvisionapp.databinding.PickFolderLayoutBinding;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.FileAdapter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class TextConverter extends AppCompatActivity implements ItemClickListener{
    ActivityTextConverterBinding binding;
    ImageView camera,gallery;
    EditText recgText;

    TextView actionCancel, actionSave;
    TextRecognizer textRecognizer;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_converter);

        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_text_converter
        );

        camera = binding.btnCamera;
        gallery = binding.btnImportGallery;

        actionSave = binding.actionSave;
        actionSave.setVisibility(View.INVISIBLE);
        actionCancel = binding.actionCancel;
        actionCancel.setVisibility(View.INVISIBLE);

        recgText = binding.recordText;

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(TextConverter.this)
                        .cameraOnly()	//User can only select image from Gallery
                        .start();	//Default Request Code is ImagePicker.REQUEST_CODE
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(TextConverter.this)
                        .galleryOnly()	//User can only select image from Gallery
                        .start();	//Default Request Code is ImagePicker.REQUEST_CODE
            }
        });
        addClickListener();
    }

    private void addClickListener() {
        actionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
                showAddFolderDialog();
//                String[] choices = {"docx", "pdf"};
//                AlertDialog.Builder builder = AlertDialog.Builder(TextConverter.this);
//                builder
//                        .setTitle("Choice format file")
//                        .setPositiveButton("Save", (dialog, which) -> {
//
//                        })
//                        .setNegativeButton("Cancel", (dialog, which) -> {
//
//                        })
//                        .setSingleChoiceItems(choices, 0, (dialog, which) -> {
//
//                        });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                imageUri = data.getData();

                Toast.makeText(this, "Image selected", Toast.LENGTH_LONG).show();
                recognizeText();

                actionSave.setVisibility(View.VISIBLE);
                actionCancel.setVisibility(View.VISIBLE);
//            } else {
                Toast.makeText(this, "Image not selected", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void recognizeText() {
        if (imageUri != null){
            try {
                InputImage inputImage = InputImage.fromFilePath(TextConverter.this,imageUri);

                Task<Text> result = textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {

                                String recognizeText = text.getText();
                                Log.i("Output", recognizeText);
                                recgText.setText(recognizeText);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TextConverter.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showAddFolderDialog() {

    }

    @Override
    public void onSettingItemClick(View v, int pos) {

    }

    @Override
    public void onFileItemClick(View v, int pos) {

    }
}