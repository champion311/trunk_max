package com.shosen.max.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;

public class GlideUtils {
    //默认图片加载
    public static RequestOptions defaultReqOptions = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.default_avater)
            .error(R.drawable.default_avater)
            .priority(Priority.HIGH);
    //图片选择器加载形式
    public static RequestOptions imagePicker = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.imagepicker_default_image)
            .error(R.drawable.imagepicker_default_image)
            .priority(Priority.NORMAL).diskCacheStrategy(DiskCacheStrategy.ALL);
    //图片预览加载形式
    public static RequestOptions imagePickerPriView = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.imagepicker_default_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public static DrawableTransitionOptions
            defaultTransOptions = new DrawableTransitionOptions().crossFade();

    public static void loadImage(Activity activity, String url, RequestOptions options,
                                 DrawableTransitionOptions transitionOptions,
                                 ImageView target) {
        Glide.with(activity).load(url).apply(options).transition(transitionOptions).into(target);
    }

    public static void loadImage(Activity activity, String url, ImageView target) {
        Glide.with(activity).load(url).apply(defaultReqOptions).transition(defaultTransOptions).into(target);
    }

    public static void loadImage(Fragment fragment, String url, ImageView target) {
        Glide.with(fragment).load(url).apply(defaultReqOptions).into(target);
    }

    public static void loadImage(Context mContext, String url, ImageView target) {
        Glide.with(mContext).load(url).apply(defaultReqOptions).into(target);
    }

    public static void loadImage(Activity activity, Uri uri, RequestOptions options,
                                 DrawableTransitionOptions transitionOptions,
                                 ImageView target) {
        Glide.with(activity).load(uri).apply(options).transition(transitionOptions).into(target);
    }

    public static void loadImage(Context mContext, String url, RequestOptions options,
                                 DrawableTransitionOptions transitionOptions,
                                 ImageView target) {
        Glide.with(mContext).load(url).apply(options).transition(transitionOptions).into(target);
    }

}
