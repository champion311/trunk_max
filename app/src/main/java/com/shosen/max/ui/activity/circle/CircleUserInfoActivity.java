package com.shosen.max.ui.activity.circle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.bean.RelationBean;
import com.shosen.max.presenter.CircleUserInfoPresenter;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.ui.activity.circle.adapter.FollowerFriendsAdapter;
import com.shosen.max.ui.activity.circle.adapter.FriendCircleAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.CancelButtonClick;
import com.shosen.max.widget.dialog.CancelOrderDialog;
import com.shosen.max.widget.dialog.CommonDialog;
import com.shosen.max.widget.dialog.ReportReasonDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CircleUserInfoActivity extends BaseActivity implements
        MessageContract.CircleUserInfoView, FollowerFriendsAdapter.
        FollowerFriendsItemClickListener,
        FriendCircleAdapter.CircleMessClickListener, OnLoadMoreListener, CancelButtonClick {
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
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_secret_message)
    TextView tvSecretMessage;
    @BindView(R.id.tv_secret_focus)
    TextView tvSecretFocus;
    @BindView(R.id.rc_follow_friends)
    RecyclerView rcFollowFriends;
    @BindView(R.id.rc_current_messages)
    RecyclerView mRecycle;
    @BindView(R.id.tv_follower_friends_more)
    TextView tvFollowerFriendsMore;
    @BindView(R.id.refresh_wrapper)
    SmartRefreshLayout refreshWrapper;

    private String userId;

    private String headImg;

    private String userName;


    private CircleUserInfoPresenter mPresenter;

    private FriendCircleAdapter mAdapter;

    private List<FriendCircleBean> mData;

    private int currentPage;//当前的页码数

    private boolean isFocus = false;
    //TODO 待修改

    private ReportReasonDialog reasonDialog;

    private CommonDialog reportDialog;

    private CommonDialog shareDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        mPresenter = new CircleUserInfoPresenter(this);
        setPresenter(mPresenter);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra("userId");
            headImg = getIntent().getStringExtra("headImg");
            userName = getIntent().getStringExtra("userName");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        if (LoginUtils.isLogin &&
                LoginUtils.getUser().getUid().equals(userId)) {
            tvHeadTitle.setText("个人主页");
        } else {
            tvHeadTitle.setText("好友主页");
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(R.drawable.right_dot);
        }
        if (!TextUtils.isEmpty(userName)) {
            tvName.setText(userName);
        }
        if (!TextUtils.isEmpty(headImg)) {
            GlideUtils.loadImage(this, headImg, ivHeader);
        }
        if (!TextUtils.isEmpty(userId)) {
            mPresenter.getSelfMess(userId, 1);
            mPresenter.getSelfFollow(userId, 1);
            if (!LoginUtils.isMe(userId)) {
                mPresenter.isRelation(userId);
            }
        }
        mData = new ArrayList<>();
        mAdapter = new FriendCircleAdapter(this, mData);
        mAdapter.setMessClickListener(this);
        mRecycle.setAdapter(mAdapter);
        rcFollowFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.setHasFixedSize(true);
        mRecycle.setNestedScrollingEnabled(true);
        mRecycle.addItemDecoration(new SpaceItemDecoration());
        mRecycle.setAdapter(mAdapter);
        mRecycle.setHasFixedSize(true);
        mRecycle.setNestedScrollingEnabled(true);
        refreshWrapper.setEnableRefresh(false);
        refreshWrapper.setEnableLoadMore(true);
        refreshWrapper.setOnLoadMoreListener(this);

        if (LoginUtils.isMe(userId)) {
            tvSecretFocus.setVisibility(View.GONE);
            tvSecretMessage.setVisibility(View.GONE);
        }
//        tvSecretFocus.setSelected(!isFocus);
//        String focusText = isFocus ? "已关注" : "+关注";
//        tvSecretFocus.setText(focusText);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_circle_user_info;
    }

    @OnClick({R.id.iv_back, R.id.tv_secret_focus,
            R.id.tv_secret_message, R.id.tv_follower_friends_more, R.id.iv_header,
            R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_secret_focus:
                //关注按钮
                if (isFocus) {
                    mPresenter.cancelFocus(userId);
                } else {
                    mPresenter.invokeFocus(userId);
                }
                break;
            case R.id.tv_follower_friends_more:
                //更多的关注者
                Intent intent = new Intent(this, FollowerListActivity.class);
                if (LoginUtils.isLogin &&
                        LoginUtils.getUser().getUid().equals(userId)) {
                    intent.putExtra(FollowerListActivity.FROM_PAGE, FollowerListActivity.FROM_MY_FOCUS);
                } else {
                    intent.putExtra(FollowerListActivity.FROM_PAGE, FollowerListActivity.FROM_OTHERS_FOCUS);
                }
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            case R.id.iv_header:
                showReportDialog();
                break;
            case R.id.iv_right:
                showShareDialog();
                break;
            default:
                break;

        }
    }

    public void showReportDialog() {
        if (LoginUtils.isMe(userId)) {
            return;
        }
        if (reportDialog == null) {
            reportDialog = new CommonDialog(this, R.style.CancelDialog);
            reportDialog.setLeft("举报", R.drawable.report_icon, v -> {
                showReportReasonDialog();
                if (reportDialog != null) {
                    reportDialog.dismiss();
                }
            });
            reportDialog.setRight("拉黑", R.drawable.dismiss_icon, v -> {
                mPresenter.defriendAndComplain(userId, "0", "");
                if (reportDialog != null) {
                    reportDialog.dismiss();
                }
            });
        }
        reportDialog.show();
    }

    public void showReportReasonDialog() {
        if (reasonDialog == null) {
            reasonDialog = new ReportReasonDialog(this, R.style.CancelDialog);
            reasonDialog.setCancelClick(this);
        }
        reasonDialog.show();
    }

    public void showShareDialog() {
        if (LoginUtils.isMe(userId)) {
            return;
        }
        if (shareDialog == null) {
            shareDialog = new CommonDialog(this, R.style.CancelDialog);
            shareDialog.setLeft("微信", R.drawable.we_chat, v -> {

            });
            shareDialog.setRight("微信朋友圈", R.drawable.we_chat_circle, v -> {

            });
        }
        shareDialog.show();
    }


    /**
     * 举报原因选择回调
     *
     * @param reason
     */
    @Override
    public void cancelClick(String reason) {
        //ToastUtils.showAlertToast(this, reason);
        mPresenter.defriendAndComplain(userId, "1", reason);
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    /**
     * 处理关注列表返回结果
     *
     * @param friendInvitationBeans
     */
    @Override
    public void showSelfFollowers(List<FriendInvitationBean> friendInvitationBeans) {
        if (friendInvitationBeans != null) {
            FollowerFriendsAdapter adapter = new FollowerFriendsAdapter(
                    friendInvitationBeans, this);
            adapter.setClickListener(this);
            rcFollowFriends.setAdapter(adapter);
        }
    }

    /**
     * 处理消息返回结果
     *
     * @param mData
     * @param pageNum
     */
    @Override
    public void showSelfMess(List<FriendCircleBean> mData, int pageNum) {
        if (pageNum == 1) {
            if (mData == null || mData.size() == 0) {
                return;
            }
            this.mData.clear();
            this.mData.addAll(mData);
            mAdapter.notifyDataSetChanged();
            currentPage = 1;
        } else {
            if (mData == null || mData.size() == 0) {
                ToastUtils.show(this, getString(R.string.no_more_data));
                return;
            }
            this.mData.addAll(mData);
            mAdapter.notifyDataSetChanged();
            currentPage += 1;
        }
    }

    /**
     * 更新点赞状态
     *
     * @param status
     * @param position
     */
    @Override
    public void updatePraiseStatus(String status, int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        if (RegexUtils.isMatch("^[0-9]+", mData.get(position).getMarkCount())) {
            int currentMarkCount = Integer.valueOf(mData.get(position).getMarkCount());
            if ("1".equals(status)) {
                currentMarkCount += 1;
            } else if ("0".equals(status)) {
                currentMarkCount -= 1;
                if (currentMarkCount < 0) {
                    currentMarkCount = 0;
                }
            }
            mData.get(position).setMarkCount(String.valueOf(currentMarkCount));
        }
        mData.get(position).setMarkStatus(status);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * 关注返回结果
     *
     * @param isFocus
     * @param follow
     */
    @Override
    public void showFocusBack(boolean isFocus, String follow) {
        this.isFocus = isFocus;
        tvSecretFocus.setSelected(!isFocus);
        String focusText = isFocus ? "已关注" : "+关注";
        tvSecretFocus.setText(focusText);
    }

    /**
     * 处理相互关系
     *
     * @param bean
     */
    @Override
    public void handleRelation(RelationBean bean) {
        if (bean == null) {
            return;
        }
        boolean isFocusA2B = "1".equals(bean.getaFollowb());
        isFocus = isFocusA2B;
        boolean isFocusB2A = "1".equals(bean.getbFollowa());
        tvSecretFocus.setSelected(!isFocusA2B);
        String focusText = isFocus ? "已关注" : "+关注";
        tvSecretFocus.setText(focusText);
    }

    /**
     * 点击关注的好友
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onFollowerItemClick(View view, int position, FriendInvitationBean bean) {
        Intent intent = new Intent(this, CircleUserInfoActivity.class);
        intent.putExtra("userId", bean.getId());
        intent.putExtra("headImg", bean.getHeadImg());
        intent.putExtra("userName", bean.getName());
        startActivity(intent);
    }

    /**
     * 消息点击
     *
     * @param view
     * @param position
     * @param circleBean
     */
    @Override
    public void onItemClick(View view, int position, FriendCircleBean circleBean) {
        if (circleBean == null) {
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_message_count:
                intent = new Intent(this, MessageListActivity.class);
                intent.putExtra("fromPage", MessageListActivity.FROM_NEW_MESSAGE);
                startActivity(intent);
                break;
            case R.id.fl_wrapper:
                intent = new Intent(this, MessageDetailActivity.class);
                intent.putExtra("circleBean", circleBean);
                startActivity(intent);
                break;
            case R.id.tv_praise_button:
                String status = "0";
                if ("1".equals(circleBean.getMarkStatus())) {
                    status = "0";
                } else {
                    status = "1";
                }
                mPresenter.addPraise(status, circleBean.getId(), position);
                break;
//            case R.id.tv_message_del:
//                //删除message
//                mPresenter.delMessage(circleBean.getId(), position);
            case R.id.iv_head_image:
                Intent intent2 = new Intent(this, CircleUserInfoActivity.class);
                intent2.putExtra("userId", circleBean.getUserId());
                intent2.putExtra("headImg", circleBean.getHeadImg());
                intent2.putExtra("userName", circleBean.getName());
                startActivity(intent2);
                break;
            default:
                break;
        }

    }

    /**
     * 加载更多时
     *
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore(1 * 1000);
        mPresenter.getSelfMess(userId, currentPage + 1);
    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = DensityUtils.dip2px(CircleUserInfoActivity.this, 4);
        }
    }
}
