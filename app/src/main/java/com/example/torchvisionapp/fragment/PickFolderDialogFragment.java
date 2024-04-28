package com.example.torchvisionapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.torchvisionapp.TextConverter;
import com.example.torchvisionapp.databinding.PickFolderLayoutBinding;
import com.example.torchvisionapp.model.FileItem;
import com.example.torchvisionapp.view.CustomAdapter;
import com.example.torchvisionapp.view.FileAdapter;

import java.io.File;
import java.util.ArrayList;

public class PickFolderDialogFragment extends DialogFragment {
    ArrayList<FileItem> listFolder;
    ListView listView;
    CustomAdapter adapter;
    String selectedFolderName = "";
    public PickFolderDialogFragment(ArrayList<FileItem> listFolder) {
        this.listFolder = listFolder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        PickFolderLayoutBinding binding = PickFolderLayoutBinding.inflate(getLayoutInflater());
        listView = binding.listView;
        Log.i("a", ""+listFolder.size());
        adapter = new CustomAdapter(getContext(), listFolder);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFolderName = listFolder.get(position).getPath();
                Log.i("folder", selectedFolderName);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedFolderName == ""){
                    Toast.makeText(getContext(), "save to root", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), selectedFolderName, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }
}
