package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.FragmentMyFavoriteBinding;

public class MyFavoriteFragment extends Fragment {
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
        FragmentMyFavoriteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.fragment_my_favorite,
                container,
                false);
        return binding.getRoot();
    }
}
