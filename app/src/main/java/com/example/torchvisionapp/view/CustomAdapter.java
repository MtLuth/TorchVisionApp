package com.example.torchvisionapp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.torchvisionapp.ItemClickListener;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.FileItemLayoutBinding;
import com.example.torchvisionapp.model.FileItem;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    List<FileItem> itemList;
    Context context;

    public CustomAdapter(List<FileItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        FileItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.file_item_layout,
                parent,
                false
        );
        holder = new ViewHolder(binding);
        convertView = binding.getRoot();
        holder.binding.setFileItem(itemList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), ""+itemList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        convertView.setTag(holder);

        return convertView;
    }

    private class ViewHolder {
        FileItemLayoutBinding binding;

        public ViewHolder(FileItemLayoutBinding binding) {
            this.binding = binding;
        }
    }
}
