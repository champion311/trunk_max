package com.shosen.max.ui.activity.circle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.presenter.FollowersFriendPresenter;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.ui.activity.circle.adapter.FollowerListAdapter;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FollowerListActivity extends BaseActivity implements
        MessageContract.FollowerFriendsView, OnLoadMoreListener,
        FollowerListAdapter.FollowerListOnItemClick {
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
    @BindView(R.id.rc_follower_list)
    RecyclerView rcFollowerList;
    @BindView(R.id.refresh_wrapper)
    SmartRefreshLayout refreshWrapper;
    @BindView(R.id.tv_recommend_text)
    TextView tvRecommendText;
    @BindView(R.id.no_friend_image)
    ImageView noFriendImage;
    @BindView(R.id.tv_alert_text)
    TextView tvAlertText;

    private FollowersFriendPresenter mPresenter;

    private String userId;

    private List<FriendInvitationBean> mData;

    private FollowerListAdapter adapter;

    private int currentPage = 1;

    public static final int FROM_FINDING = 501;

    public static final int FROM_MY_FOCUS = 502;

    public static final int FROM_OTHERS_FOCUS = 503;

    public static final int FROM_MY_FANS = 504;

    public static final String FROM_PAGE = "fromPage";

    private int fromPage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new FollowersFriendPresenter(this);
        setPresenter(mPresenter);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra("userId");
            fromPage = getIntent().getIntExtra("fromPage", FROM_FINDING);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        if (fromPage == FROM_FINDING) {
            mPresenter.getFriendInvitation(1);
        } else if (fromPage == FROM_MY_FANS) {
            mPresenter.getMyFans(1);
        } else {
            mPresenter.getSelfFollow(userId, 1);
        }
        mData = new ArrayList<>();
        adapter = new FollowerListAdapter(mData, this);
        adapter.setFromPage(fromPage);
        adapter.setOnItemClick(this);
        rcFollowerList.setAdapter(adapter);
        rcFollowerList.setLayoutManager(new LinearLayoutManager(this));
        refreshWrapper.setEnableRefresh(false);
        //TODO
        refreshWrapper.setEnableLoadMore(false);
        refreshWrapper.setOnLoadMoreListener(this);
        String title;
        switch (fromPage) {
            case FROM_FINDING:
                title = "好友推荐";
                break;
            case FROM_MY_FOCUS:
                title = "我的关注";
                tvRecommendText.setVisibility(View.GONE);
                break;
            case FROM_OTHERS_FOCUS:
                title = "他的关注";
                tvRecommendText.setVisibility(View.GONE);
                break;
            case FROM_MY_FANS:
                title = "我的粉丝";
                tvRecommendText.setVisibility(View.GONE);
                break;
            default:
                title = "好友推荐";
                break;
        }
        tvHeadTitle.setText(title);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_friends_followers;
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 通用回调
     *
     * @param mData
     * @param pageNum
     */
    @Override
    public void showSelfFollowers(List<FriendInvitationBean> mData, int pageNum) {
        if (mData == null || mData.size() == 0) {
            String text = "";
            if (fromPage == FROM_FINDING) {
                text = "暂无推荐";
            } else if (fromPage == FROM_MY_FOCUS) {
                text = "暂无关注";
            } else if (fromPage == FROM_MY_FANS) {
                text = "暂无粉丝";
            }
            noFriendImage.setVisibility(View.VISIBLE);
            tvAlertText.setVisibility(View.VISIBLE);
            tvAlertText.setText(text);
            rcFollowerList.setVisibility(View.GONE);
            return;
        }
        noFriendImage.setVisibility(View.GONE);
        tvAlertText.setVisibility(View.GONE);
        rcFollowerList.setVisibility(View.VISIBLE);
        for (FriendInvitationBean bean : mData) {
            if (fromPage == FROM_MY_FOCUS) {
                bean.setMarkStatus("1");
            } else if (fromPage == FROM_FINDING) {
                bean.setMarkStatus("0");
            }
        }
        if (pageNum == 1) {
            this.mData.clear();
            this.mData.addAll(mData);
            currentPage = 1;
        } else {
            this.mData.addAll(mData);
            currentPage += 1;
        }
        //ToastUtils.show(this, String.valueOf(currentPage));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
        String text = "";
        if (fromPage == FROM_FINDING) {
            text = "暂无推荐";
        } else if (fromPage == FROM_MY_FOCUS) {
            text = "暂无关注";
        } else if (fromPage == FROM_MY_FANS) {
            text = "暂无粉丝";
        }
        noFriendImage.setVisibility(View.VISIBLE);
        tvAlertText.setVisibility(View.VISIBLE);
        tvAlertText.setText(text);
        rcFollowerList.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.show(this, message);
    }

    /**
     * 关注回调
     *
     * @param isFocus
     * @param position
     */
    @Override
    public void showFocusBack(boolean isFocus, int position) {
        if (fromPage != FROM_MY_FANS) {
            if (isFocus) {
                mData.get(position).setMarkStatus("1");
            } else {
                mData.get(position).setMarkStatus("0");
            }
        } else {
            if (isFocus) {
                mData.get(position).setEachOther("1");
            } else {
                mData.get(position).setEachOther("0");
            }
        }
        adapter.notifyItemChanged(position);
    }


    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore(1 * 1000);
        if (fromPage == FROM_FINDING) {
            mPresenter.getFriendInvitation(currentPage + 1);
        } else if (fromPage == FROM_MY_FANS) {
            mPresenter.getMyFans(currentPage + 1);
        } else {
            mPresenter.getSelfFollow(userId, currentPage + 1);
        }
    }

    /**
     * 关注点击
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void followerListOnclick(View view, int position, FriendInvitationBean bean) {
        switch (view.getId()) {
            case R.id.tv_focus:
                if (fromPage != FROM_MY_FANS) {
                    if ("1".equals(bean.getMarkStatus())) {
                        mPresenter.cancelFocus(bean.getId(), position);
                    } else {
                        mPresenter.invokeFocus(bean.getId(), position);
                    }
                } else {
                    if ("1".equals(bean.getEachOther())) {
                        mPresenter.cancelFocus(bean.getUserId(), position);
                    } else {
                        mPresenter.invokeFocus(bean.getUserId(), position);
                    }
                }
                break;
            case R.id.rl_wrapper:
                String userId = bean.getId();
                String headImg = bean.getHeadImg();
                String userName = bean.getName();
                Intent intent = new Intent(this, CircleUserInfoActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("headImg", headImg);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}


