package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torchvisionapp.FolderAdapter;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.TextConverter;
import com.example.torchvisionapp.databinding.FragmentHomeBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    ImageView btnCamera, btnAddFolder;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ImageView folderImageView;

    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private List<String> folderList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo ActivityResultLauncher để start CameraActivity
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Xử lý kết quả nếu cần
                    }
                }
        );
    }
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.fragment_home,
                container,
                false);
        View view = binding.getRoot();

        btnCamera = binding.btnImageToText;
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraActivity();
            }
        });
        btnAddFolder = binding.btnAddFolder;
        btnAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFolderDialog();
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        folderList = new ArrayList<>();

        showExistingFolders();

        folderAdapter = new FolderAdapter(requireContext(), folderList);
        recyclerView.setAdapter(folderAdapter);

        return view;
    }
    private void openCameraActivity() {
        Intent intent = new Intent(requireContext(), TextConverter.class);
        cameraLauncher.launch(intent);
    }
    private void showAddFolderDialog() {
        // Tạo AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_folder, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextFolderName = dialogView.findViewById(R.id.editTextFolderName);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        final AlertDialog alertDialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = editTextFolderName.getText().toString().trim();
                if (!folderName.isEmpty()) {
                    createFolder(folderName);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Please enter folder name", Toast.LENGTH_SHORT).show();
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

    private void createFolder(String folderName) {
        File folder = new File(requireContext().getFilesDir(), folderName);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                folderList.add(folderName);
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
                    folderList.add(file.getName());
                }
            }
        }
    }
}
