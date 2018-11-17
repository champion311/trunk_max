package com.shosen.max.others.span;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.loader.ImageLoader;
import com.shosen.max.R;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.RegexUtils;


import java.io.File;

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideUtils.loadImage(activity, Uri.
                fromFile(new File(path)), GlideUtils.imagePicker, GlideUtils.defaultTransOptions, imageView);

    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        if (RegexUtils.isURL(path)) {
            GlideUtils.loadImage(activity, path, GlideUtils.imagePickerPriView, GlideUtils.defaultTransOptions, imageView);
        } else {
            GlideUtils.loadImage(activity, Uri.
                    fromFile(new File(path)), GlideUtils.imagePickerPriView, GlideUtils.defaultTransOptions, imageView);
        }

    }

    @Override
    public void clearMemoryCache() {

    }
}
