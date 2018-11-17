package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.presenter.MyOrderListPresenter;
import com.shosen.max.presenter.contract.MyOrderContract;
import com.shosen.max.ui.adapter.MyOrderAdapter;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class MyOrderActivity extends BaseActivity implements MyOrderContract.View, MyOrderAdapter.MyOrderItemClick {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rc_myorderlist)
    RecyclerView rcMyorderlist;

    public static final int ORDER_DETAIL_REQUEST = 100;
    @BindView(R.id.fl_no_order)
    FrameLayout flNoOrder;

    private MyOrderListPresenter mPresenter;

    private List<OrderResponse> mData;

    private MyOrderAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyOrderListPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvHeadTitle.setText("我的订单");
        if (LoginUtils.getUser() != null) {
            mPresenter.getOrdersList(LoginUtils.getUser().getPhone());
        }
        rcMyorderlist.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        mAdapter = new MyOrderAdapter(this, mData);
        mAdapter.setOnItemClick(this);
        rcMyorderlist.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    public void showBookList(List<OrderResponse> mList) {
        if (mList == null || mList.size() == 0) {
            flNoOrder.setVisibility(View.VISIBLE);
            return;
        } else {
            flNoOrder.setVisibility(View.GONE);
        }
        if (mData != null) {
            mData.clear();
        }
        mData.addAll(mList);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showError(String errorMessage) {
        ToastUtils.show(this, errorMessage);
    }

    @Override
    public void OnMyOrderItemClick(int position, View view, OrderResponse response) {
        if (response == null) {
            return;
        }
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("bookId", response.getId());
        startActivityForResult(intent, ORDER_DETAIL_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (LoginUtils.isLogin) {
                mPresenter.getOrdersList(LoginUtils.getUser().getPhone());
            }
        }

    }
}
