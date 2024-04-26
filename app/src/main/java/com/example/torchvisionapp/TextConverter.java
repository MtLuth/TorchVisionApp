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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityTextConverterBinding;
import com.example.torchvisionapp.model.FileItem;
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
    ImageView saveFile, getImage, copy;
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

        getImage = binding.btnCamera;

        recgText = binding.recgText;

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(TextConverter.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        fileList = new ArrayList<>();

        showAddFieDialog();

        saveFile.setOnClickListener(new View.OnClickListener() {
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

    private void showAddFieDialog() {
        // Tạo AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_file, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextFileName = dialogView.findViewById(R.id.editTextFileName);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editTextFileName.getText().toString().trim();
                if (!fileName.isEmpty()) {
                    createFile(fileName);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Please enter file name", Toast.LENGTH_SHORT).show();
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

    private void createFile(String filename) {
        File folder = new File(requireContext().getFilesDir(), filename);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                fileList.add(new FileItem(R.drawable.iconfolder_actived, filename, "0 files"));
                folderAdapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Folder created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to create folder", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Folder already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExistingFolders() {
        File[] files = requireContext().getFilesDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setName(file.getName());
                    fileItem.setIcon(R.drawable.iconfolder_actived);
                    fileItem.setStatus("0 file");

                    fileList.add(fileItem);
                }
            }
        }
    }
}