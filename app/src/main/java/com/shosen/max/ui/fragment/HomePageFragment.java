package com.shosen.max.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.presenter.HomePageFragmentPresenter;
import com.shosen.max.presenter.contract.HomePageContract;
import com.shosen.max.ui.activity.OrderCarActivity;
import com.shosen.max.ui.activity.QustionnaireActivity;
import com.shosen.max.ui.activity.circle.adapter.ImagePagerAdapter;
import com.shosen.max.ui.adapter.HomePageGridAdapter;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomePageFragment extends BaseFragment implements
        HomePageGridAdapter.HomePageGridOnClick, HomePageContract.View {


    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.iv_head_title)
    ImageView ivHeadTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.vp_image_grid)
    ViewPager vpImageGrid;
    @BindView(R.id.mGridView)
    RecyclerView mGridView;
    @BindView(R.id.v_to_order)
    View vToOrder;
    private HomePageGridAdapter mAdapter;

    private Unbinder unbinder;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    private HomePageFragmentPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        mPresenter = new HomePageFragmentPresenter(getActivity());
        setPresenter(mPresenter);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home_page, null);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
        initTopHeader(view);
    }

    @Override
    protected void initEventAndData() {
        StatusBarUtil.setDarkMode(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), getActivity().getResources().getColor(R.color.home_page_color));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mAdapter = new HomePageGridAdapter(getActivity());
        mAdapter.setInterface(this);
        mGridView.setLayoutManager(gridLayoutManager);
        mGridView.setAdapter(mAdapter);
        //mGridView.addItemDecoration(new SpaceItemDecoration());
        //取消滑动
        mGridView.setHasFixedSize(false);
        mGridView.setNestedScrollingEnabled(false);
        mPresenter.getBannerList(2);

    }

    public void initTopHeader(View view) {
        RelativeLayout rlTop = (RelativeLayout) view.findViewById(R.id.rl_top_header);
        if (rlTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(getActivity());
            rlTop.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isHidden()) {
            StatusBarUtil.setLightMode(getActivity());
            StatusBarUtil.setColorNoTranslucent(getActivity(), Color.WHITE);
        } else {
            StatusBarUtil.setDarkMode(getActivity());
            StatusBarUtil.setColorNoTranslucent(getActivity(), getActivity().getResources().getColor(R.color.home_page_color));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void OnGridClick(View view, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                mPresenter.signIn();
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    @Override
    public void showBannerList(List<String> data) {
        if (data != null) {
            vpImageGrid.setAdapter(new ImagePagerAdapter(data, getActivity()));
        }
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void singSuccess(String message) {
        //Test Only
        ToastUtils.show(getActivity(), message);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = DensityUtils.dip2px(getActivity(), 14);
        }
    }

    @OnClick({R.id.v_to_order})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.v_to_order:
                startActivity(new Intent(getActivity(), QustionnaireActivity.class));
                break;
        }
    }
}
