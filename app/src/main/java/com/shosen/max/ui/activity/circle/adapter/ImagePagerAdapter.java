package com.shosen.max.ui.activity.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.GlideUtils;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private List<String> mData;

    private Context mContext;

    private int imageWidth;

    private int imageHeight;

    public ImagePagerAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.base_D8D8D8));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dip2px(mContext, DensityUtils.dip2px(mContext, 150)));
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_long_place_holder)
                .error(R.drawable.default_long_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideUtils.loadImage(mContext, mData.get(position), requestOptions
                , GlideUtils.defaultTransOptions, imageView);
        container.addView(imageView, layoutParams);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
