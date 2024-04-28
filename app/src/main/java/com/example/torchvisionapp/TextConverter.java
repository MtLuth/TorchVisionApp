package com.example.torchvisionapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.torchvisionapp.databinding.ActivityTextConverterBinding;
import com.example.torchvisionapp.databinding.DialogAddFileBinding;
import com.example.torchvisionapp.databinding.DialogAddFolderBinding;
import com.example.torchvisionapp.databinding.PickFolderLayoutBinding;
import com.example.torchvisionapp.fragment.PickFolderDialogFragment;
import com.example.torchvisionapp.model.DocumentFile;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.FileAdapter;
import com.example.torchvisionapp.viewmodel.FileExplorer;
import com.example.torchvisionapp.viewmodel.TextConverterViewModel;
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

    private FileAdapter folderAdapter;
    private ArrayList<FileItem> folderList;
    TextView actionCancel, actionSave;
    TextRecognizer textRecognizer;
    Uri imageUri;
    TextConverterViewModel viewModel;
    public String mFolderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_converter);

        viewModel = new ViewModelProvider(this).get(TextConverterViewModel.class);

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
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        Log.i("path", path);
        folderList = viewModel.loadFolderList(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        Log.i("countaaa", ""+folderList.size());
    }

    private void addClickListener() {
        actionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recgText.setText("");
                actionSave.setVisibility(View.INVISIBLE);
                actionCancel.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                imageUri = data.getData();

                recognizeText();

                actionSave.setVisibility(View.VISIBLE);
                actionCancel.setVisibility(View.VISIBLE);
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
    public void ChoicesFormatFile(){
        String[] choices = {"docx", "pdf", "txt"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TextConverter.this);
        builder
                .setTitle("Choice format file")
                .setPositiveButton("Save", (dialog, which) -> {
                    int selectedFormatIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    String selectedFormat = choices[selectedFormatIndex];

                    Toast.makeText(this,"" + selectedFormat, Toast.LENGTH_SHORT).show();
                    showAddFileDialog(selectedFormat);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setSingleChoiceItems(choices, 0, (dialog, which) -> {

                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<FileItem> showExistingFolders() {
        String path = this.getFilesDir().getPath();
        ArrayList<FileItem> fileItems = new ArrayList<>();
        FileExplorer explorer = new FileExplorer(TextConverter.this);
        ArrayList<File> folderList = explorer.loadExistingFolderFromPath(path);
        Log.i("path", path);
        if (folderList != null) {
            for (File file : folderList) {
                if (file.isDirectory()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setName(file.getName());
                    fileItem.setIcon(R.drawable.iconfolder_actived);
                    int count = explorer.countNumberOfFileInDirectory(file.getPath());
                    fileItem.setStatus(count+" files");

                    fileItems.add(fileItem);
                }
            }
        }
        return fileItems;
    }
    private void showAddFileDialog(String format) {
        DialogAddFileBinding dialogAddFileBinding = DialogAddFileBinding.inflate(getLayoutInflater());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TextConverter.this);
        dialogBuilder.setView(dialogAddFileBinding.getRoot());

        final EditText editTextFileName = dialogAddFileBinding.editTextFileName;
        Button btnSave = dialogAddFileBinding.btnSave;
        Button btnCancel = dialogAddFileBinding.btnCancel;

        final AlertDialog alertDialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editTextFileName.getText().toString().trim();
                if (!fileName.isEmpty()) {
                    saveAsFile(fileName, format);
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
    private void saveAsFile(String fileName, String format) {
        DocumentFile docs = new DocumentFile(getApplicationContext());
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        docs.saveAsFile(recgText.getText().toString(), fileName, format, directory.getPath());
    }
    @Override
    public void onSettingItemClick(View v, int pos) {

    }

    @Override
    public void onFileItemClick(View v, int pos) {

    }

    private void saveFile() {
        PickFolderDialogFragment pickFolderDialogFragment = new PickFolderDialogFragment(folderList);
        pickFolderDialogFragment.show(getSupportFragmentManager(), "dialog_tag");

        ChoicesFormatFile();
    }


}