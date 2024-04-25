package com.example.torchvisionapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torchvisionapp.ItemClickListener;
import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.SettingItemBinding;
import com.example.torchvisionapp.model.SettingItem;

import java.util.ArrayList;

public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemAdapter.ViewHolder> {

    ArrayList<SettingItem> settingItemArrayList;
    Context context;
    ItemClickListener clickListener;

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SettingItemAdapter(ArrayList<SettingItem> settingItemArrayList, Context context) {
        this.settingItemArrayList = settingItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SettingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.setting_item,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingItem item = settingItemArrayList.get(position);
        holder.settingItemBinding.setSettingItem(item);
    }

    @Override
    public int getItemCount() {
        return settingItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SettingItemBinding settingItemBinding;

        public ViewHolder(SettingItemBinding settingItemBinding) {
            super(settingItemBinding.getRoot());
            this.settingItemBinding = settingItemBinding;

            settingItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onSettingItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
