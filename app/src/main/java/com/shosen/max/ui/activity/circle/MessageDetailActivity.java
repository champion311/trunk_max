package com.shosen.max.ui.activity.circle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.CommentBean;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.MessagePresenter;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.ui.activity.circle.adapter.CommentsAndReplyAdapter;
import com.shosen.max.ui.activity.circle.adapter.FriendCircleAdapter;
import com.shosen.max.ui.activity.circle.adapter.NineImageAdapter;
import com.shosen.max.ui.activity.circle.adapter.OnMenuItemClickListener;
import com.shosen.max.utils.ClipboardUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.HDateUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.CircleImageView;
import com.shosen.max.widget.circle.NineGridView;
import com.shosen.max.widget.circle.VerticalCommentWidget;
import com.shosen.max.widget.circle.popwindow.BottomInsertPop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 消息详情
 */
public class MessageDetailActivity extends BaseActivity
        implements TextView.OnEditorActionListener,
        MessageContract.View, VerticalCommentWidget.OnReplyClickListener,
        CommentsAndReplyAdapter.OnCommentClickListener, OnMenuItemClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.rc_comments)
    RecyclerView rcComments;
    @BindView(R.id.tv_to_comment)
    TextView tvToComment;
    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_praise_button)
    TextView tvPraiseButton;
    @BindView(R.id.tv_comment_text)
    TextView tvCommentText;
    @BindView(R.id.ng_image_content)
    NineGridView ngImageContent;
    private MessagePresenter mPresenter;

    private String messId;

    private FriendCircleBean circleBean;

    private List<CommentBean> mDatas;

    private CommentsAndReplyAdapter mAdapter;

    private BottomInsertPop insertPop;

    private String currentParentId;//添加回复或者评论时 parentId 添加评论是为0

    private boolean isPraised = false;

    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MessagePresenter(this);
        setPresenter(mPresenter);
        if (getIntent() != null) {
            circleBean = getIntent().getParcelableExtra("circleBean");
            if (circleBean != null) {
                messId = circleBean.getId();
            }
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("消息详情");
        mDatas = new ArrayList<>();
        mAdapter = new CommentsAndReplyAdapter(this, mDatas);
        mAdapter.setOnCommentClickListener(this);
        mAdapter.setOnReplyClickListener(this);
        mAdapter.setCommentMenuClickListener(this);
        mAdapter.setReplyMenuClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        rcComments.setLayoutManager(layoutManager);
        rcComments.setAdapter(mAdapter);
        rcComments.setHasFixedSize(true);
        mPresenter.getComments(messId);
        initMessDetails();

    }

    public void initMessDetails() {
        if (circleBean == null) {
            return;
        }
        GlideUtils.loadImage(this, circleBean.getHeadImg(), ivAvatar);
        tvName.setText(circleBean.getName());
        if (RegexUtils.isMatch("^[0-9]+", circleBean.getCreateTime())) {
            long time = Long.valueOf(circleBean.getCreateTime()) / 1000;
            tvTime.setText(HDateUtils.getDateTime(time));
            tvDes.setText(SpanUtils.getUrlDecodeContent(circleBean.getContent()));
        }
        tvCommentCount.setText(String.valueOf(circleBean.getComCount()));
        tvPraiseButton.setText(String.valueOf(circleBean.getMarkCount()));
        isPraised = "1".equals(circleBean.getMarkStatus());
        tvPraiseButton.setSelected(isPraised);

        String[] imageData;
        if (!TextUtils.isEmpty(circleBean.getPicture())) {
            imageData = circleBean.getPicture().split(FriendCircleAdapter.SEPERAOTR);
            NineImageAdapter imageAdapter = new
                    NineImageAdapter(mContext, Arrays.asList(imageData));
            ArrayList<ImageItem> imageItems = new ArrayList<>();
            for (String url : imageData) {
                ImageItem item = new ImageItem();
                item.path = url;
                imageItems.add(item);
            }
            ngImageContent.setAdapter(imageAdapter);
            ngImageContent.setImageClickListener((p, v) -> {
                //打开预览
                Intent intentPreview =
                        new Intent(mContext, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imageItems);
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, p);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                intentPreview.putExtra(ImagePicker.EXTRA_PREVIEW_FROM_WEB, true);
                ((Activity) mContext).startActivityForResult(intentPreview, Contstants.REQUEST_CODE_PREVIEW);
            });
        } else {
            ngImageContent.setVisibility(View.GONE);
        }
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_message_details;
    }

    @OnClick({R.id.iv_back, R.id.tv_to_comment, R.id.
            tv_comment_count, R.id.tv_praise_button, R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_to_comment:
                //添加评论 parentId为0
                currentParentId = "0";
                showBottomPop(view, null);
                break;
            case R.id.tv_comment_count:
                if (mDatas != null && mDatas.size() != 0) {
                    smoothMoveToPosition(rcComments, 0);
                }
                break;
            case R.id.tv_praise_button:
                String status = "0";
                if (circleBean != null) {
                    if (isPraised) {
                        status = "0";
                    } else {
                        status = "1";
                    }
                }
                mPresenter.addMessPraise(status, messId);
                break;
            case R.id.iv_avatar:
                Intent intent = new Intent(MessageDetailActivity.this, CircleUserInfoActivity.class);
                intent.putExtra("userId", circleBean.getUserId());
                intent.putExtra("headImg", circleBean.getHeadImg());
                intent.putExtra("userName", circleBean.getName());
                startActivity(intent);
                break;
            default:
                break;

        }
    }


    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;


    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView rcComments, final int position) {
        // 第一个可见位置
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        // 最后一个可见位置
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            rcComments.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < rcComments.getChildCount()) {
                int top = rcComments.getChildAt(movePosition).getTop();
                rcComments.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            rcComments.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }


    /**
     * @param view
     * @param bean bean ==o，否则为添加回复
     */
    public void showBottomPop(View view, CommentBean bean) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        insertPop = new BottomInsertPop(this);
        insertPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        insertPop.focusEditText(this);
        insertPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        if (insertPop.getEditText() != null) {
            if (bean == null) {
                insertPop.getEditText().setHint("请输入你的评论");
            } else {
                insertPop.getEditText().setHint("回复: " + bean.getName());
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            //TODO 发送评论
            if (!LoginUtils.isLogin) {
                return false;
            }
            String content = v.getText().toString().trim();
            mPresenter.addComments(content, messId, currentParentId,
                    LoginUtils.getUser().getUid());
            if (insertPop != null) {
                insertPop.dismiss();
                ClipboardUtils.hideSoftKeyBoard(mContext, insertPop.getEditText());
            }
            return true;
        }
        return false;
    }

    /**
     * 获取评论列表
     *
     * @param mData
     */
    @Override
    public void showCommentsList(List<CommentBean> mData) {
        if (mData == null) {
            return;
        }
        //TODO DIFFUTILS
        if (this.mDatas != null) {
            this.mDatas.clear();
            this.mDatas.addAll(mData);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加删除评论的回调
     *
     * @param type
     */
    @Override
    public void acitionCallBack(int type) {
        switch (type) {
            case MessagePresenter.ADD_COMMENT_SUCCESS:
                ToastUtils.show(this, "添加评论成功");
                mPresenter.getComments(messId);
                break;
            case MessagePresenter.DEL_COMMENT_SUCCESS:
                mPresenter.getComments(messId);
                ToastUtils.show(this, "删除评论成功");
                break;
        }
    }

    /**
     * 更新评论状态
     *
     * @param status
     * @param position
     */
    @Override
    public void updatePraiseStatus(String status, int position) {
        if (position == -1) {
            isPraised = "1".equals(status);
            tvPraiseButton.setSelected(isPraised);
            if (RegexUtils.isMatch("^[0-9]+", circleBean.getMarkCount())) {
                int currentMarkCount = Integer.valueOf(circleBean.getMarkCount());
                if ("1".equals(status)) {
                    currentMarkCount += 1;
                } else if ("0".equals(status)) {
                    currentMarkCount -= 1;
                    if (currentMarkCount < 0) {
                        currentMarkCount = 0;
                    }
                }
                tvPraiseButton.setText(String.valueOf(currentMarkCount));
            }
        } else {
            if (mDatas != null && mDatas.size() != 0) {
                mDatas.get(position).setMarkStatus(status);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (insertPop != null && insertPop.isShowing()) {
                insertPop.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 评论点击监听
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onCommentClick(View view, int position, CommentBean bean) {
        switch (view.getId()) {
            case R.id.tv_message_del:
                //点击删除
                mPresenter.delComments(bean.getId());
                break;
            case R.id.iv_priase_icon:
                //评论点赞
                String status;
                if ("1".equals(bean.getMarkStatus())) {
                    status = "0";
                } else {
                    status = "1";
                }
                mPresenter.addCommentPraise(status, bean.getId(), position);
                break;
            case R.id.iv_message_avater:
                Intent intent = new Intent(this, CircleUserInfoActivity.class);
                intent.putExtra("userId", bean.getUserId());
                intent.putExtra("headImg", bean.getHeadImg());
                intent.putExtra("userName", bean.getName());
                startActivity(intent);
                break;
            default:
                if (bean != null) {
                    currentParentId = bean.getId();
                }
                showBottomPop(view, bean);
        }


    }

    /**
     * 回复点击监听
     *
     * @param view     break;
     * @param position
     * @param bean
     */
    @Override
    public void onReplyClick(View view, int position, CommentBean bean) {
        if (bean != null) {
            currentParentId = bean.getId();
        }
        showBottomPop(view, bean);
    }

    /**
     * 错误回调
     *
     * @param message
     */
    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    /**
     * 点击了复制
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onMenuCopyItemClickListener(View view, int position, CommentBean bean) {
        if (bean != null) {
            ClipboardUtils.invokeClipManager(this, bean.getContent());
        }
    }

    /**
     * 删除评论
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onMenuDelItemClickListener(View view, int position, CommentBean bean) {
        if (bean != null) {
            mPresenter.delComments(bean.getId());
        }
    }
}

