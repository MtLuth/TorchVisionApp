package com.example.torchvisionapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.PrivacyListBinding;

import java.util.List;

public class PrivacyListAdapter extends RecyclerView.Adapter<PrivacyListAdapter.PrivacyViewHolder> {
    List<String>  privacyList;
    Context context;

    public PrivacyListAdapter(List<String> privacyList, Context context) {
        this.privacyList = privacyList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrivacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PrivacyListBinding listBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.privacy_list,
                parent,
                false
        );
        return new PrivacyViewHolder(listBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivacyViewHolder holder, int position) {
        String content = privacyList.get(position);
        holder.privacyListBinding.setPrivacy(content);
    }

    @Override
    public int getItemCount() {
        return privacyList.size();
    }

    public class PrivacyViewHolder extends RecyclerView.ViewHolder {
        PrivacyListBinding privacyListBinding;
        public PrivacyViewHolder(PrivacyListBinding binding) {
            super(binding.getRoot());
            this.privacyListBinding = binding;
        }
    }
}
