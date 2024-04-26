package com.example.torchvisionapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityTextConverterBinding;
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


public class TextConverter extends AppCompatActivity {
    ActivityTextConverterBinding binding;
    ImageView camera,gallery;
    EditText recgText;
    TextRecognizer textRecognizer;
    Uri imageUri;

    private ArrayList<FileItem> fileList;

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
        fileList = new ArrayList<>();

        showExistingFiles();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                imageUri = data.getData();

                Toast.makeText(this, "Image selected", Toast.LENGTH_LONG).show();

                recognizeText();
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

    private void showAddFileDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TextConverter.this);
        LayoutInflater inflater = LayoutInflater.from(TextConverter.this);
        View dialogView = inflater.inflate(R.layout.dialog_add_file, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editTextFileName);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editText.getText().toString().trim();
                if (!fileName.isEmpty()) {
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(TextConverter.this, "Please enter file name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void showExistingFiles() {
        File[] files = TextConverter.this.getFilesDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setName(file.getName());
                    fileItem.setIcon(R.drawable.icon_image_to_text);
                    fileItem.setStatus("0 file");

                    fileList.add(fileItem);
                }
            }
        }
    }
}