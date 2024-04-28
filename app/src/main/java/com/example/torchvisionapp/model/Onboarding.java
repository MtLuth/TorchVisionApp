package com.example.torchvisionapp.model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.torchvisionapp.BR;

public class Onboarding extends BaseObservable{

    int headingRes;
    int imageRes;
    @BindingAdapter({"image_res"})
    public static void loadImage(ImageView imageView, int imageRes) {
        Glide.with(imageView.getContext())
                .load(imageRes)
                .into(imageView);
    }


    int pager;
    @BindingAdapter({"image_pager"})
    public static void loadPager(ImageView imageView, int imageRes) {
        Glide.with(imageView.getContext())
                .load(imageRes)
                .into(imageView);
    }

    public Onboarding(int imageRes, int headingRes, int pager) {
        this.imageRes = imageRes;
        this.headingRes = headingRes;
        this.pager = pager;
    }

    @Bindable
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
        notifyPropertyChanged(BR.imageRes);
    }

    @Bindable
    public int getHeadingRes() {
        return headingRes;
    }

    public void setHeadingRes(int headingRes) {
        this.headingRes = headingRes;
        notifyPropertyChanged(BR.headingRes);
    }

    @Bindable
    public int getPager() {
        return pager;
    }

    public void setPager(int pager) {
        this.pager = pager;
        notifyPropertyChanged(BR.pager);
    }
}
