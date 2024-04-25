package com.example.torchvisionapp.model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.torchvisionapp.BR;

public class FileItem extends BaseObservable {

    int icon;
    String name;

    @BindingAdapter({"iconImg"})
    public static void LoadIcon(ImageView imageView, int iconSrc) {
        Glide.with(imageView.getContext())
                .load(iconSrc)
                .into(imageView);
    }

    public FileItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Bindable
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
