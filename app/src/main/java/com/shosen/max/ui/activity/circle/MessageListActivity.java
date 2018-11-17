package com.shosen.max.ui.activity.circle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.MyRessMessBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.MyReMessPresenter;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.ui.activity.circle.adapter.MyRessAdapter;
import com.shosen.max.utils.SPUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人消息列表
 *
 * @author
 */
public class MessageListActivity extends BaseActivity
        implements MessageContract.MyMessageView, OnLoadMoreListener, MyRessAdapter.MyRessItemClickListener {


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
    @BindView(R.id.no_message_image)
    ImageView noMessageImage;
    @BindView(R.id.rc_messages)
    RecyclerView rcMessages;
    @BindView(R.id.refresh_wrapper)
    SmartRefreshLayout refreshWrapper;

    private MyReMessPresenter mPresenter;

    private List<MyRessMessBean> mData;

    private MyRessAdapter mAdapter;

    private int currentPage = 0;

    private int fromPage;

    public static final int FROM_MY_CENTER = 500;//从个人中心来

    public static final int FROM_NEW_MESSAGE = 501;//从新消息中来

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyReMessPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        if (getIntent() != null) {
            fromPage = getIntent().getIntExtra("fromPage", FROM_MY_CENTER);
        }
        tvHeadTitle.setText("消息");
        tvRight.setVisibility(View.GONE);
        tvRight.setText("清空");
        tvRight.setTextColor(ContextCompat.getColor(this, R.color.black_text_color));
        noMessageImage.setVisibility(View.GONE);

        rcMessages.setLayoutManager(new LinearLayoutManager(this));
        rcMessages.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new MyRessAdapter(this, mData);
        mAdapter.setmClick(this);
        rcMessages.setAdapter(mAdapter);
        refreshWrapper.setEnableLoadMore(true);
        refreshWrapper.setEnableRefresh(false);
        refreshWrapper.setOnLoadMoreListener(this);
        if (fromPage == FROM_NEW_MESSAGE) {
            SPUtils.getInstance(Contstants.SP_NEW_MESSAGE).put(Contstants.NEW_MESSAGE_COUNT, "0");
        }
        if (fromPage == FROM_MY_CENTER) {
            mPresenter.getMyReMesss(1);
        } else if (fromPage == FROM_NEW_MESSAGE) {
            mPresenter.getNoticeMess();
            refreshWrapper.setEnableLoadMore(false);
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_message_list;
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                break;
            default:
                break;
        }
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(mContext, message);
    }

    @Override
    public void showMineMessageList(List<MyRessMessBean> mData, int pageNum) {
        if (pageNum == 1) {
            if (mData == null || mData.size() == 0) {
                noMessageImage.setVisibility(View.VISIBLE);
                rcMessages.setVisibility(View.GONE);
                return;
            }
            noMessageImage.setVisibility(View.GONE);
            rcMessages.setVisibility(View.VISIBLE);
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

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshWrapper.finishLoadMore(1 * 1000);
        mPresenter.getMyReMesss(currentPage + 1);
    }

    @Override
    public void itemClick(View view, int position, MyRessMessBean bean) {
        FriendCircleBean circleBean = new FriendCircleBean(bean);
        Intent intent = new Intent(this, MessageDetailActivity.class);
        intent.putExtra("circleBean", circleBean);
        startActivity(intent);
    }
}
