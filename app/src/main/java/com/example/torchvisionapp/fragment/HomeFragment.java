package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import com.example.torchvisionapp.DeleteFile;
import com.example.torchvisionapp.FileItemClick;
import com.example.torchvisionapp.ItemClickListener;
import com.example.torchvisionapp.ReadFile;
import com.example.torchvisionapp.TranslateActivity;
import com.example.torchvisionapp.databinding.DialogAddFolderBinding;
import com.example.torchvisionapp.databinding.PickFolderLayoutBinding;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.FileAdapter;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.TextConverter;
import com.example.torchvisionapp.databinding.FragmentHomeBinding;
import com.example.torchvisionapp.viewmodel.FileExplorer;
import com.google.android.gms.dynamic.SupportFragmentWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements ItemClickListener, FileItemClick, DeleteFile {
    ImageView btnCamera, btnAddFolder, btnTranslate;
    private ActivityResultLauncher<Intent> image_to_textLaucher;
    private RecyclerView recyclerView, recyclerViewPickFolder;
    private FileAdapter folderAdapter;
    private ArrayList<FileItem> fileItemArrayList;
    private String rootPath;
    PickFolderLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = PickFolderLayoutBinding.inflate(getLayoutInflater());
        rootPath = requireContext().getFilesDir().getPath();
        fileItemArrayList = new ArrayList<>();
        showExistingFolders();
        showAllFileTxt();
        Log.i("resultT", fileItemArrayList.size()+"");
        folderAdapter = new FileAdapter(fileItemArrayList, getContext());
        folderAdapter.setClickListener(this);
        folderAdapter.setDeleteFileListener(this);

        image_to_textLaucher = registerForActivityResult(
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
        btnTranslate = binding.btnTranslate;
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTranslateActivity(getContext());
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
        image_to_textLaucher.launch(intent);
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
        File folder = new File(rootPath+"/"+folderName);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                FileItem newFolder = new FileItem();
                newFolder.setPath(folder.getPath());
                newFolder.setName(folderName);
                newFolder.setIcon(R.drawable.iconfolder_actived);
                newFolder.setType("directory");
                newFolder.setStatus("0 files");
                fileItemArrayList.add(newFolder);
                folderAdapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Folder created successfully ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to create folder", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Folder already exists", Toast.LENGTH_SHORT).show();
        }
    }



    private void showExistingFolders() {
        String path = rootPath;
        FileExplorer explorer = new FileExplorer(getContext());
        ArrayList<File> folderList = explorer.loadExistingFolderFromPath(path);
        Log.i("path", path);
        if (folderList != null) {
            for (File file : folderList) {
                if (file.isDirectory()) {
                    FileItem fileItem = new FileItem();
                    fileItem.setName(file.getName());
                    fileItem.setIcon(R.drawable.iconfolder_actived);
                    fileItem.setPath(file.getPath());
                    fileItem.setType("directory");
                    int count = explorer.countNumberOfFileInDirectory(file.getPath());
                    fileItem.setStatus(count+" files");

                    fileItemArrayList.add(fileItem);
                }
            }
        }
    }

    private void showAllFileTxt() {
        String path = rootPath;
        FileExplorer explorer = new FileExplorer(getContext());
        ArrayList<File> fileArrayList = explorer.loadAllTextFiles(path);
        if (fileArrayList != null) {
            for (File file : fileArrayList) {
                FileItem fileItem = new FileItem();
                fileItem.setName(file.getName());
                fileItem.setIcon(R.drawable.icon_image_to_text);
                fileItem.setPath(file.getPath());
                long date = file.lastModified();
                Date status = new Date(date);
                fileItem.setStatus(status.toString());

                fileItemArrayList.add(fileItem);
            }
        }
    }

    @Override
    public void onSettingItemClick(View v, int pos) {

    }

    private void openTranslateActivity(Context context) {
        Intent i = new Intent(context, TranslateActivity.class);
        startActivity(i);
    }

    @Override
    public void fileClick(View v, int pos) {
        FileItem file = fileItemArrayList.get(pos);
        if (file.getType()=="directory") {
            Toast.makeText(getContext(), "Directory", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "txt", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), ReadFile.class);
            i.putExtra("uri", file.getPath());
            startActivity(i);
        }
    }

    @Override
    public void deleteFile(String path, int pos) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            Toast.makeText(getContext(), "complete", Toast.LENGTH_SHORT).show();
            fileItemArrayList.remove(pos);
            folderAdapter.setFileList(fileItemArrayList);
            recyclerView.setAdapter(folderAdapter);
        }
    }
}
