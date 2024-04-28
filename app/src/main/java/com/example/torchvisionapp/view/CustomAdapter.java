package com.example.torchvisionapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.FileItemLayoutBinding;
import com.example.torchvisionapp.model.FileItem;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<FileItem>{
    List<FileItem> itemList;
    Context context;

    public CustomAdapter(@NonNull Context context, List<FileItem> itemList) {
        super(context, 0, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        @SuppressLint("ViewHolder") FileItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.file_item_layout,
                parent,
                false
        );
        holder = new ViewHolder(binding);
        convertView = binding.getRoot();
        holder.binding.setFileItem(itemList.get(position));

        convertView.setTag(holder);

        return convertView;
    }

    private static class ViewHolder {
        FileItemLayoutBinding binding;

        public ViewHolder(FileItemLayoutBinding binding) {
            this.binding = binding;
        }
    }
}
