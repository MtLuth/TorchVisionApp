package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.torchvisionapp.DeleteFile;
import com.example.torchvisionapp.FileItemClick;
import com.example.torchvisionapp.OnFragmentClick;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.ReadFile;
import com.example.torchvisionapp.databinding.FragmentMyFolderBinding;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.FileAdapter;
import com.example.torchvisionapp.viewmodel.FileExplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class FolderFragment extends Fragment implements FileItemClick, DeleteFile {
    ArrayList<FileItem> fileItemArrayList;
    FragmentMyFolderBinding binding;
    FileAdapter fileAdapter;
    FileExplorer  explorer;
    OnFragmentClick onFragmentClick;
    public String path;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileItemArrayList = new ArrayList<>();
        explorer = new FileExplorer(getContext());
        showFolder();
        showTextFile();
        fileAdapter = new FileAdapter(fileItemArrayList, getContext());
        fileAdapter.setClickListener(this);
        fileAdapter.setDeleteFileListener(this);
    }

    private void showFolder() {
        if (new File(path).exists()) {
            ArrayList<File> files = explorer.loadExistingFolderFromPath(path);
            for (File f: files) {
                FileItem newFileItem = new FileItem();
                newFileItem.setName(f.getName());
                newFileItem.setType("directory");
                newFileItem.setIcon(R.drawable.iconfolder_actived);
                newFileItem.setStatus(explorer.countNumberOfFileInDirectory(f.getPath())+" files");
                newFileItem.setPath(f.getPath());
                fileItemArrayList.add(newFileItem);
            }
        }

    }

    private void showTextFile() {
        if (new File(path).exists()) {
            ArrayList<File> files = explorer.loadAllTextFiles(path);
            for (File f: files) {
                FileItem newFileItem = new FileItem();
                newFileItem.setName(f.getName());
                newFileItem.setType("txt");
                newFileItem.setIcon(R.drawable.docs);
                long date = f.lastModified();
                Date status = new Date(date);
                newFileItem.setStatus(status.toString());
                newFileItem.setPath(f.getPath());
                fileItemArrayList.add(newFileItem);
            }
        }

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
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.fragment_my_folder, container, false);
        binding.recyclerViewMyFolder.setAdapter(fileAdapter);
        binding.recyclerViewMyFolder.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void fileClick(View v, int pos) {
        FileItem file = fileItemArrayList.get(pos);
        if (file.getType()=="directory") {
            String fPath = fileItemArrayList.get(pos).getPath();
            String fName = fileItemArrayList.get(pos).getName();
            if (onFragmentClick != null) {
                onFragmentClick.openFolder(fPath, fName);
            }
        } else {
            Intent i = new Intent(getContext(), ReadFile.class);
            i.putExtra("uri", file.getPath());
            startActivity(i);
        }
    }

    public void setOpenFolderOnFragment(OnFragmentClick onFragmentClick) {
        this.onFragmentClick = onFragmentClick;
    }

    @Override
    public void deleteFile(String path, int pos) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            fileItemArrayList.remove(pos);
            fileAdapter.setFileList(fileItemArrayList);
            fileAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
