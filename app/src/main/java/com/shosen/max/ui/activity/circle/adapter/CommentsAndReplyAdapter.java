package com.shosen.max.ui.activity.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.CommentBean;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.HDateUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.widget.CircleImageView;
import com.shosen.max.widget.circle.VerticalCommentWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAndReplyAdapter extends RecyclerView.Adapter<CommentsAndReplyAdapter.CommentsAndReplyViewHolder> {


    private Context mContext;

    private List<CommentBean> mData;

    private OnCommentClickListener onCommentClickListener;

    private VerticalCommentWidget.OnReplyClickListener onReplyClickListener;

    private OnMenuItemClickListener replyMenuClickListener;//回复菜单点击

    private OnMenuItemClickListener commentMenuClickListener;//评论菜单点击

    public void setReplyMenuClickListener(OnMenuItemClickListener replyMenuClickListener) {
        this.replyMenuClickListener = replyMenuClickListener;
    }

    public void setCommentMenuClickListener(OnMenuItemClickListener commentMenuClickListener) {
        this.commentMenuClickListener = commentMenuClickListener;
    }

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    public void setOnReplyClickListener(VerticalCommentWidget.OnReplyClickListener onReplyClickListener) {
        this.onReplyClickListener = onReplyClickListener;
    }

    public CommentsAndReplyAdapter(Context mContext, List<CommentBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentsAndReplyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_message_comments, null);
        return new CommentsAndReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAndReplyViewHolder commentsAndReplyViewHolder, int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        CommentBean bean = mData.get(position);
        // build必须 根节点build为子回复集合设置额parentId
        bean.build(mContext);
        GlideUtils.loadImage(mContext, bean.getHeadImg(), commentsAndReplyViewHolder.ivMessageAvater);
        commentsAndReplyViewHolder.tvMessageName.setText(bean.getName());
        //commentsAndReplyViewHolder.tvMessageContent.setText(bean.getContent());
        if (RegexUtils.isMatch("^[0-9]+", bean.getCreateTime())) {
            long time = Long.valueOf(bean.getCreateTime()) / 1000;
            commentsAndReplyViewHolder.tvMessageTime.setText(HDateUtils.getDateTime(time));
        }
        //设置点赞
        boolean isPraised = "1".equals(bean.getMarkStatus());
        commentsAndReplyViewHolder.ivPriaseIcon.setSelected(isPraised);
        commentsAndReplyViewHolder.tvMessageContent.
                setText(SpanUtils.getUrlDecodeContent(bean.getContent()));
        //添加回复
        if (bean.getCommontList() == null || bean.getCommontList().size() == 0) {
            commentsAndReplyViewHolder.replysWrapper.setVisibility(View.GONE);
        } else {
            commentsAndReplyViewHolder.replysWrapper.setVisibility(View.VISIBLE);
            commentsAndReplyViewHolder.replysWrapper.addComments(bean.getCommontList());
            //回复菜单点击监听
            commentsAndReplyViewHolder.replysWrapper.setMenuItemClickListener(replyMenuClickListener);
            //回复点击监听
            commentsAndReplyViewHolder.replysWrapper.setOnReplyClickListener(onReplyClickListener);
        }
        commentsAndReplyViewHolder.tvMessageDel.setOnClickListener(v -> {

            if (onCommentClickListener != null) {
                onCommentClickListener.onCommentClick(v, position, bean);
            }
        });
        //评论删除
        if (LoginUtils.isLogin && LoginUtils.getUser().getUid().equals(bean.getUserId())) {
            commentsAndReplyViewHolder.tvMessageDel.setVisibility(View.VISIBLE);
        } else {
            commentsAndReplyViewHolder.tvMessageDel.setVisibility(View.GONE);
        }
        //点赞点击
        commentsAndReplyViewHolder.ivPriaseIcon.setOnClickListener(v -> {
            if (onCommentClickListener != null) {
                onCommentClickListener.onCommentClick(v, position, bean);
            }
        });
        //评论点击监听
        commentsAndReplyViewHolder.itemView.setOnClickListener(v -> {
            if (onCommentClickListener != null) {
                onCommentClickListener.onCommentClick(v, position, bean);
            }
        });
        //评论菜单点击监听
        setOnMenuItemClick(commentsAndReplyViewHolder.itemView, position, bean);
    }

    //TODO 待优化
    private void setOnMenuItemClick(View view, int position, CommentBean bean) {

        PopupMenu popupMenu = new PopupMenu(mContext, view);
        if (LoginUtils.isLogin && LoginUtils.getUser().getUid().equals(bean.getUserId())) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_replys_mine, popupMenu.getMenu());
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.menu_replys, popupMenu.getMenu());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.copy:
                        if (commentMenuClickListener != null) {
                            commentMenuClickListener.onMenuCopyItemClickListener(view, position, bean);
                        }
                        break;
                    case R.id.del:
                        if (commentMenuClickListener != null) {
                            commentMenuClickListener.onMenuDelItemClickListener(view, position, bean);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        view.setOnLongClickListener(v -> {
            popupMenu.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class CommentsAndReplyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_message_avater)
        CircleImageView ivMessageAvater;
        @BindView(R.id.tv_message_name)
        TextView tvMessageName;
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;
        @BindView(R.id.tv_message_del)
        TextView tvMessageDel;
        @BindView(R.id.iv_priase_icon)
        ImageView ivPriaseIcon;
        @BindView(R.id.replys_wrapper)
        VerticalCommentWidget replysWrapper;

        public CommentsAndReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnCommentClickListener {

        void onCommentClick(View view, int position, CommentBean bean);
    }


}
