package com.example.test3.ui.glidetools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.test3.R;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

public class GlideEngine implements ImageEngine {

    //加载图片--本地
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    //加载网络图片适配长图方案
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView,
                          OnImageCompleteCallback callback) {
    }

    //加载网络图片适配长图方案---废弃，但必须写在这，不然报错。
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView) {
    }

    //加载相册目录

    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {

        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(new BitmapImageViewTarget(imageView)
                {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    //加载gif
    @Override
    public void loadAsGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {

        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    //加载图片列表图片

    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(imageView);
    }

    //初始化GlideEngine
    private GlideEngine(){}
    private static GlideEngine instance;

    public static GlideEngine createGlideEngine()
    {
        if (instance == null)
        {
            synchronized (GlideEngine.class)
            {
                if (instance == null)
                {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }
}
