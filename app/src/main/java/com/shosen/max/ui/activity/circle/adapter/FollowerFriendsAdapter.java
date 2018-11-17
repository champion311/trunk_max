package com.shosen.max.ui.activity.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerFriendsAdapter extends
        RecyclerView.Adapter<FollowerFriendsAdapter.FollowFriendsHolder> {


    private List<FriendInvitationBean> mData;

    private Context mContext;

    private FollowerFriendsItemClickListener mClickListener;

    public void setClickListener(FollowerFriendsItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public FollowerFriendsAdapter(List<FriendInvitationBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FollowFriendsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_follower_friends, viewGroup, false);
        return new FollowFriendsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowFriendsHolder followFriendsHolder, int position) {
        if (mData == null) {
            return;
        }
        FriendInvitationBean bean = mData.get(position);

        RequestOptions requestOptions = new RequestOptions().centerCrop().
                placeholder(R.drawable.default_avater).
                error(R.drawable.default_avater);
        GlideUtils.loadImage(mContext, bean.getHeadImg(),
                requestOptions, GlideUtils.defaultTransOptions, followFriendsHolder.ivAvatar);
        followFriendsHolder.ivAvatar.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onFollowerItemClick(v, position, bean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class FollowFriendsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;

        public FollowFriendsHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface FollowerFriendsItemClickListener {
        void onFollowerItemClick(View view, int position, FriendInvitationBean bean);
    }
}
