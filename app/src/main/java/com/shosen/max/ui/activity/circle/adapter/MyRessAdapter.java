package com.shosen.max.ui.activity.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.bean.MyRessMessBean;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRessAdapter extends RecyclerView.Adapter<MyRessAdapter.MyRessViewHolder> {


    private Context mContext;

    private List<MyRessMessBean> mData;

    private MyRessItemClickListener mClick;

    public void setmClick(MyRessItemClickListener mClick) {
        this.mClick = mClick;
    }

    public MyRessAdapter(Context mContext, List<MyRessMessBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyRessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_message_list, viewGroup, false);
        return new MyRessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRessViewHolder myRessViewHolder, int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        MyRessMessBean bean = mData.get(position);
        GlideUtils.loadImage(mContext, bean.getHeadImg(), myRessViewHolder.ivMessageAvater);
        myRessViewHolder.tvMessageName.setText(bean.getUserName());

        if (!TextUtils.isEmpty(bean.getPicture())) {
            myRessViewHolder.ivImageContent.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions();
            options.centerCrop().placeholder(R.drawable.default_place_holder).error(R.drawable.imagepicker_default_image);
            GlideUtils.loadImage(mContext, bean.getPicture(), options, GlideUtils.defaultTransOptions,
                    myRessViewHolder.ivImageContent);
        } else {
            myRessViewHolder.ivImageContent.setVisibility(View.GONE);
        }
        if ("666".equals(bean.getContent())) {
            myRessViewHolder.tvMessageContent.setText("");
            myRessViewHolder.ivAddpriaseIcon.setVisibility(View.VISIBLE);
        } else {
            myRessViewHolder.ivAddpriaseIcon.setVisibility(View.GONE);
            myRessViewHolder.tvMessageContent.setText(SpanUtils.getUrlDecodeContent(bean.getContent()));
        }
        myRessViewHolder.itemView.setOnClickListener(v -> {
            if (mClick != null) {
                mClick.itemClick(myRessViewHolder.itemView, position, bean);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class MyRessViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_message_avater)
        CircleImageView ivMessageAvater;
        @BindView(R.id.tv_message_name)
        TextView tvMessageName;
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;
        @BindView(R.id.iv_image_content)
        ImageView ivImageContent;
        @BindView(R.id.tv_text_content)
        TextView tvTextContent;
        @BindView(R.id.iv_addpriase_icon)
        ImageView ivAddpriaseIcon;

        public MyRessViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface MyRessItemClickListener {
        void itemClick(View view, int position, MyRessMessBean bean);
    }
}
