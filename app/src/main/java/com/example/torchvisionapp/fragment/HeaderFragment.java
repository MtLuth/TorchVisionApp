package com.example.torchvisionapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.databinding.FragmentHeaderBinding;

public class HeaderFragment extends Fragment {

    String title;

    public HeaderFragment (String title) {
        this.title = title;
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
        FragmentHeaderBinding binding;
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(container.getContext()),
                                R.layout.fragment_header,
                                container,
                                false
                        );
        binding.textHeader.setText(title);
        if (title == "SETTINGS") {
            binding.btnSearch.setVisibility(View.INVISIBLE);
            binding.btnSort.setVisibility(View.INVISIBLE);
        }
        return binding.getRoot();
    }
}
