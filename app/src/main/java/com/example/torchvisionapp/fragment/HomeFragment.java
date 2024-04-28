package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.torchvisionapp.ItemClickListener;
import com.example.torchvisionapp.databinding.DialogAddFolderBinding;
import com.example.torchvisionapp.databinding.PickFolderLayoutBinding;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.FileAdapter;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.TextConverter;
import com.example.torchvisionapp.databinding.FragmentHomeBinding;
import com.google.android.gms.dynamic.SupportFragmentWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ItemClickListener {
    ImageView btnCamera, btnAddFolder;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ImageView folderImageView;
    private RecyclerView recyclerView, recyclerViewPickFolder;
    private FileAdapter folderAdapter;
    private ArrayList<FileItem> folderList;
    PickFolderLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PickFolderLayoutBinding.inflate(getLayoutInflater());

        folderList = showExistingFolders();
        folderAdapter = new FileAdapter(folderList, getContext());
        folderAdapter.setClickListener(this);

        for (FileItem i: folderList
             ) {
            Log.i("item1", i.getName());
        }

        PickFolderDialogFragment pickFolderDialogFragment = new PickFolderDialogFragment(folderList, folderAdapter);
        pickFolderDialogFragment.show(getActivity().getSupportFragmentManager(), "aaa");

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

        //show folder in HomePage
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(folderAdapter);

        return view;
    }
    private void openCameraActivity() {
        Intent intent = new Intent(requireContext(), TextConverter.class);
        cameraLauncher.launch(intent);
    }
    private void showAddFolderDialog() {
        // Tạo AlertDialog
        DialogAddFolderBinding dialogAddFolderBinding = DialogAddFolderBinding.inflate(getLayoutInflater());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setView(dialogAddFolderBinding.getRoot());

        final EditText editTextFolderName = dialogAddFolderBinding.editTextFolderName;
        Button btnSave = dialogAddFolderBinding.btnSave;
        Button btnCancel = dialogAddFolderBinding.btnCancel;

        final AlertDialog alertDialog = dialogBuilder.create();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Test", Toast.LENGTH_SHORT).show();
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
                folderList.add(new FileItem(R.drawable.iconfolder_actived, folderName, "0 files"));
                folderAdapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Folder created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to create folder", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Folder already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<FileItem> showExistingFolders() {
        File[] files = requireContext().getFilesDir().listFiles();
        ArrayList<FileItem> fileItems = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setName(file.getName());
                    fileItem.setIcon(R.drawable.iconfolder_actived);
                    fileItem.setStatus("0 file");

                    fileItems.add(fileItem);
                }
            }
        }
        return fileItems;
    }

    @Override
    public void onSettingItemClick(View v, int pos) {

    }

    @Override
    public void onFileItemClick(View v, int pos) {
        Toast.makeText(getContext(), ""+folderList.get(pos).getName(), Toast.LENGTH_SHORT).show();

        String folderName = folderList.get(pos).getName();
        String folderPath = requireContext().getFilesDir() + "/" + folderName;

        Intent intent = new Intent(requireContext(), HomeFragment.class);
        intent.putExtra("folder_path", folderPath);
        startActivity(intent);
    }
}