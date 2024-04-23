package com.example.torchvisionapp.model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.torchvisionapp.BR;

public class SettingItem extends BaseObservable {
    int icon;
    @BindingAdapter({"icon_img"})
    public static void loadIcon(ImageView imageView, int icon) {
        Glide.with(imageView.getContext())
                .load(icon)
                .into(imageView);
    }
    String title;

    public SettingItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public SettingItem() {
    }
}
