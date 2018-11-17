package com.shosen.max.ui.activity.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.shosen.max.R;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.NewMessageBean;
import com.shosen.max.bean.refreshEvent.AddPriaseEvent;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.HDateUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.SPUtils;
import com.shosen.max.widget.CircleImageView;
import com.shosen.max.widget.circle.NineGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendCircleAdapter extends RecyclerView.Adapter<FriendCircleAdapter.FriendCircleHolder> {


    private Context mContext;
    private List<FriendCircleBean> mData;

    public static final String SEPERAOTR = ",";

    private String newMessageCount = "0";//新消息提醒

    public void setNewMessageCount(String newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    private DrawableTransitionOptions mDrawableTransitionOptions;

    private CircleMessClickListener messClickListener;

    public void setMessClickListener(CircleMessClickListener messClickListener) {
        this.messClickListener = messClickListener;
    }

    public FriendCircleAdapter(Context mContext, List<FriendCircleBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        //未读消息数目
        newMessageCount = SPUtils.getInstance(Contstants.SP_NEW_MESSAGE).
                getString(Contstants.NEW_MESSAGE_COUNT, "0");
    }


    @NonNull
    @Override
    public FriendCircleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_circle_message, viewGroup, false);
        return new FriendCircleHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull FriendCircleHolder friendCircleHolder, int position) {
        FriendCircleBean bean = mData.get(position);
        if (bean == null) {
            return;
        }
        String[] imageData;
        if (!TextUtils.isEmpty(bean.getPicture())) {
            imageData = bean.getPicture().split(SEPERAOTR);
            NineImageAdapter imageAdapter = new
                    NineImageAdapter(mContext, Arrays.asList(imageData));
            ArrayList<ImageItem> imageItems = new ArrayList<>();
            for (String url : imageData) {
                ImageItem item = new ImageItem();
                item.path = url;
                imageItems.add(item);
            }
            friendCircleHolder.mNineGrid.setAdapter(imageAdapter);
            friendCircleHolder.mNineGrid.setImageClickListener((p, v) -> {
                //打开预览
                Intent intentPreview =
                        new Intent(mContext, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imageItems);
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, p);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                intentPreview.putExtra(ImagePicker.EXTRA_PREVIEW_FROM_WEB, true);
                ((Activity) mContext).startActivityForResult(intentPreview, Contstants.REQUEST_CODE_PREVIEW);
            });
            friendCircleHolder.mNineGrid.setVisibility(View.VISIBLE);
        } else {
            friendCircleHolder.mNineGrid.setVisibility(View.GONE);
        }
        friendCircleHolder.tvCircleName.setText(bean.getName());
        friendCircleHolder.tvCircleContent.setText(bean.getContent());
        friendCircleHolder.tvCircleTime.
                setText(HDateUtils.getDate(Long.valueOf(bean.getCreateTime()), "MM-dd"));
        friendCircleHolder.tvCommentButton.setText(String.valueOf(bean.getComCount()));
        friendCircleHolder.tvPraiseButton.setText(String.valueOf(bean.getMarkCount()));
        GlideUtils.loadImage(mContext, bean.getHeadImg(), friendCircleHolder.ivHeadImage);
        if (TextUtils.isEmpty(bean.getLocation())) {
            friendCircleHolder.tvLocation.setVisibility(View.GONE);
        } else {
            friendCircleHolder.tvLocation.setVisibility(View.VISIBLE);
            friendCircleHolder.tvLocation.setText(bean.getLocation());
        }
        //未读消息
        if (!"0".equals(newMessageCount) && position == 0) {
            friendCircleHolder.tvMessageCount.setVisibility(View.VISIBLE);
            friendCircleHolder.tvMessageCount.setText(newMessageCount + "条未读消息");
        } else {
            friendCircleHolder.tvMessageCount.setVisibility(View.GONE);
        }
        boolean isPraised = "1".equals(bean.getMarkStatus());
        friendCircleHolder.tvPraiseButton.setSelected(isPraised);
        //点击事件
        friendCircleHolder.tvPraiseButton.setOnClickListener(v -> {
            if (messClickListener != null) {
                messClickListener.onItemClick(v, position, bean);
            }
        });
        friendCircleHolder.tvMessageDel.setOnClickListener(v -> {
            if (messClickListener != null) {
                messClickListener.onItemClick(v, position, bean);
            }
        });
        friendCircleHolder.ivHeadImage.setOnClickListener(v -> {
            if (messClickListener != null) {
                messClickListener.onItemClick(v, position, bean);
            }
        });

        friendCircleHolder.flWrapper.setOnClickListener(v -> {
            if (messClickListener != null) {
                messClickListener.onItemClick(v, position, bean);
            }
        });

        friendCircleHolder.tvMessageCount.setOnClickListener(v -> {
            if (messClickListener != null) {
                messClickListener.onItemClick(v, position, bean);
            }
        });
        if (LoginUtils.isMe(bean.getUserId())) {
            friendCircleHolder.tvMessageDel.setVisibility(View.VISIBLE);
        } else {
            friendCircleHolder.tvMessageDel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FriendCircleHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            for (Object payload : payloads) {
                //点赞更新
                if (payload instanceof AddPriaseEvent) {
                    AddPriaseEvent event = (AddPriaseEvent) payload;
                    boolean isPraised = "1".equals(event.getMarkStatus());
                    holder.tvPraiseButton.setSelected(isPraised);
                    holder.tvPraiseButton.setText(event.getMarkCount());
                } else if (payload instanceof NewMessageBean) {
                    //新消息推送更新
                    NewMessageBean bean = (NewMessageBean) payload;
                    newMessageCount = bean.getNoReadNum();
                    if (!"0".equals(newMessageCount) && position == 0) {
                        holder.tvMessageCount.setVisibility(View.VISIBLE);
                        holder.tvMessageCount.setText(newMessageCount + "条未读消息");
                    } else {
                        holder.tvMessageCount.setVisibility(View.GONE);
                    }
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public class FriendCircleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_message_count)
        TextView tvMessageCount;
        @BindView(R.id.tv_circle_name)
        TextView tvCircleName;
        @BindView(R.id.tv_circle_time)
        TextView tvCircleTime;
        @BindView(R.id.tv_circle_content)
        TextView tvCircleContent;
        @BindView(R.id.mNineGrid)
        NineGridView mNineGrid;
        @BindView(R.id.tv_comment_button)
        TextView tvCommentButton;
        @BindView(R.id.tv_praise_button)
        TextView tvPraiseButton;
        @BindView(R.id.iv_head_image)
        CircleImageView ivHeadImage;
        @BindView(R.id.iv_rewrite)
        CircleImageView ivRewrite;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.fl_wrapper)
        FrameLayout flWrapper;
        @BindView(R.id.tv_message_del)
        TextView tvMessageDel;

        public FriendCircleHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CircleMessClickListener {
        void onItemClick(View view, int position, FriendCircleBean circleBean);
    }


}
