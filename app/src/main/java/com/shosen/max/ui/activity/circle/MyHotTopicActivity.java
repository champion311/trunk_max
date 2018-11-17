package com.shosen.max.ui.activity.circle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.presenter.MyHotTopicPresenter;
import com.shosen.max.presenter.contract.MessageContract;
import com.shosen.max.ui.activity.circle.adapter.FriendCircleAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.ClipboardUtils;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyHotTopicActivity extends BaseActivity
        implements OnLoadMoreListener, FriendCircleAdapter.CircleMessClickListener,
        MessageContract.MyHotTopView, OnRefreshListener, TextView.OnEditorActionListener {
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
    @BindView(R.id.ed_search_box)
    EditText edSearchBox;
    @BindView(R.id.rc_search_list)
    RecyclerView mRecycle;
    @BindView(R.id.refresh_wrapper)
    SmartRefreshLayout refreshWrapper;

    private MyHotTopicPresenter mPresenter;

    private FriendCircleAdapter mAdapter;

    private List<FriendCircleBean> mData;

    private int currentPage;//当前的页码数

    private String curContent = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyHotTopicPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("我的话题");
        mData = new ArrayList<>();
        mAdapter = new FriendCircleAdapter(this, mData);
        mAdapter.setMessClickListener(this);
        mRecycle.setAdapter(mAdapter);
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.setHasFixedSize(true);
        mRecycle.setNestedScrollingEnabled(true);
        mRecycle.addItemDecoration(new SpaceItemDecoration());
        mRecycle.setAdapter(mAdapter);
        mRecycle.setHasFixedSize(true);
        mRecycle.setNestedScrollingEnabled(true);
        refreshWrapper.setEnableRefresh(true);
        refreshWrapper.setEnableLoadMore(true);
        refreshWrapper.setOnLoadMoreListener(this);
        refreshWrapper.setOnRefreshListener(this);
        edSearchBox.setOnEditorActionListener(this);
        mPresenter.getMyHotTopic("", 1);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_top_seracrch;
    }


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
            case R.id.tv_message_del:
                //删除message
                mPresenter.delMessage(circleBean.getId(), position);
                break;
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

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    @Override
    public void updatePraiseStatus(String status, int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        //ToastUtils.show(getActivity(), status);
        mData.get(position).setMarkStatus(status);
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
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void delMessageCallBack(int position) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        if (position < 0 || position > mData.size() - 1) {
            return;
        }
        mAdapter.notifyItemRemoved(position);
        mData.remove(position);
    }

    @Override
    public void showSelfMess(String content, List<FriendCircleBean> mData, int pageNum) {
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
     * 搜索内容
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (!LoginUtils.isLogin) {
                return false;
            }
            curContent = v.getText().toString().trim();
            mPresenter.getMyHotTopic(curContent, 1);
            ClipboardUtils.hideSoftKeyBoard(mContext, edSearchBox);
            return true;
        }
        return false;
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(1 * 1000);
        mPresenter.getMyHotTopic(curContent, 1);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore(1 * 1000);
        mPresenter.getMyHotTopic(curContent, currentPage + 1);
    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = DensityUtils.dip2px(MyHotTopicActivity.this, 4);
        }
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
