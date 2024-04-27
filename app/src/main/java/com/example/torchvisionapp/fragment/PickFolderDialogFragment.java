package com.example.torchvisionapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.PickFolderLayoutBinding;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.CustomAdapter;
import com.example.torchvisionapp.view.FileAdapter;

import java.io.File;
import java.util.ArrayList;

public class PickFolderDialogFragment extends DialogFragment {
    ArrayList<FileItem> listFolder;
    FileAdapter myAdapter;
    ListView listView;
    public PickFolderDialogFragment(ArrayList<FileItem> listFolder, FileAdapter fileAdapter) {
        this.listFolder = listFolder;
        this.myAdapter = fileAdapter;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        PickFolderLayoutBinding binding = PickFolderLayoutBinding.inflate(getLayoutInflater());
        listView = binding.listView;

        CustomAdapter adapter = new CustomAdapter(listFolder, getContext());
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        Toast.makeText(getContext(), ""+listFolder.size(), Toast.LENGTH_SHORT).show();
        return builder.create();
    }

}
