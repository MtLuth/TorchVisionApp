package com.example.torchvisionapp.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torchvisionapp.DeleteFile;
import com.example.torchvisionapp.FileItemClick;
import com.example.torchvisionapp.ItemClickListener;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.FileItemLayoutBinding;
import com.example.torchvisionapp.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileItemViewHolder> {
    private ArrayList<FileItem> fileList;
    private Context context;

    FileItemClick clickListener;
    DeleteFile deleteFileListener;

    public void setFileList(ArrayList<FileItem> fileList) {
        this.fileList = fileList;
    }

    public void setClickListener(FileItemClick clickListener) {
        this.clickListener = clickListener;
    }

    public void setDeleteFileListener(DeleteFile deleteFileListener){
        this.deleteFileListener = deleteFileListener;
    }

    public FileAdapter(ArrayList<FileItem> fileList, Context context) {
        this.fileList = fileList;
        this.context = context;
    }

    @NonNull
    @Override
    public FileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FileItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.file_item_layout,
                parent,
                false
        );
        return new FileItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FileItemViewHolder holder, int position) {
        FileItem file = fileList.get(position);
        holder.binding.setFileItem(file);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class FileItemViewHolder extends RecyclerView.ViewHolder {
        FileItemLayoutBinding binding;

        public FileItemViewHolder(@NonNull FileItemLayoutBinding fileItemLayoutBinding) {
            super(fileItemLayoutBinding.getRoot());
            this.binding = fileItemLayoutBinding;

            fileItemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.fileClick(v, getAdapterPosition());

                    }
                }
            });
            fileItemLayoutBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteFileListener != null) {
                        deleteFileListener.deleteFile(fileList.get(getAdapterPosition()).getPath(), getAdapterPosition());
                    }
                }
            });
        }

    }
}

