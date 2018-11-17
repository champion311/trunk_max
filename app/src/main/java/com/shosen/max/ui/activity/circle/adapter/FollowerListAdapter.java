package com.shosen.max.ui.activity.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.ui.activity.circle.FollowerListActivity;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerListAdapter extends RecyclerView.Adapter<FollowerListAdapter.FollowerListHolder> {


    private List<FriendInvitationBean> mData;

    private Context mContext;

    private FollowerListOnItemClick onItemClick;

    private int fromPage;

    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
    }

    public void setOnItemClick(FollowerListOnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public FollowerListAdapter(List<FriendInvitationBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FollowerListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_follow_friends, viewGroup, false);
        return new FollowerListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerListHolder followerListHolder, int i) {
        FriendInvitationBean bean = mData.get(i);
        GlideUtils.loadImage(mContext, bean.getHeadImg(), followerListHolder.ivAvatar);
        followerListHolder.tvName.setText(bean.getName());
        followerListHolder.tvFocus.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.followerListOnclick(v, i, bean);
            }
        });
        if (fromPage == FollowerListActivity.FROM_OTHERS_FOCUS) {
            followerListHolder.tvFocus.setVisibility(View.GONE);
        } else {
            followerListHolder.tvFocus.setVisibility(View.VISIBLE);
        }

        if (fromPage != FollowerListActivity.FROM_MY_FANS) {
            //非我的粉丝
            boolean isFocus = "1".equals(bean.getMarkStatus());
            followerListHolder.tvFocus.setSelected(!isFocus);
            String focusText = isFocus ? "已关注" : "+关注";
            followerListHolder.tvFocus.setText(focusText);
        } else {
            //我的粉丝 是否互相关注
            boolean isFocusEachOther = "1".equals(bean.getEachOther());
            followerListHolder.tvFocus.setSelected(!isFocusEachOther);
            String focusText = isFocusEachOther ? "互相关注" : "+关注";
            followerListHolder.tvFocus.setText(focusText);
        }
        followerListHolder.rlWrapper.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.followerListOnclick(v, i, bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class FollowerListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_focus)
        TextView tvFocus;
        @BindView(R.id.rl_wrapper)
        RelativeLayout rlWrapper;

        public FollowerListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface FollowerListOnItemClick {
        void followerListOnclick(View view, int position, FriendInvitationBean bean);
    }


}
