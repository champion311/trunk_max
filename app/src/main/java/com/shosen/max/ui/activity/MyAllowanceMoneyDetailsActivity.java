package com.shosen.max.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.presenter.MyAllowanceListPresenter;
import com.shosen.max.presenter.MyAllowancePresenter;
import com.shosen.max.presenter.contract.MyAllowanceListContract;
import com.shosen.max.ui.adapter.MyAllowanceAdapter;
import com.shosen.max.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAllowanceMoneyDetailsActivity extends BaseActivity implements MyAllowanceListContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rc_my_allowance_details)
    RecyclerView rcMyAllowanceDetails;

    private MyAllowanceListPresenter mPresenter;

    private List<RewardListBean> mData;

    private MyAllowanceAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new MyAllowanceListPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        tvHeadTitle.setText("明细");
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        rcMyAllowanceDetails.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        mAdapter = new MyAllowanceAdapter(this, mData);
        rcMyAllowanceDetails.setAdapter(mAdapter);
        mPresenter.getListData();
        //关闭默认动画
        ((SimpleItemAnimator) rcMyAllowanceDetails.getItemAnimator()).
                setSupportsChangeAnimations(false);

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_my_allowance_money_details;
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void dataSuccess(List<RewardListBean> rewardListBeanList) {
        if (rewardListBeanList == null || rewardListBeanList.size() == 0) {
            return; //noData;
        }
        if (mData != null) {
            mData.clear();
        }
        mData.addAll(rewardListBeanList);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void dataError(String message) {
        if (mData != null) {
            mData.clear();
        }
    }
}
