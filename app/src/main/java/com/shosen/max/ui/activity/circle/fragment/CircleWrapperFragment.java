package com.shosen.max.ui.activity.circle.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.ui.DataGenerator;
import com.shosen.max.ui.activity.LoginActivity;
import com.shosen.max.ui.activity.circle.CircleUserMineActivity;
import com.shosen.max.ui.activity.circle.SubmitActivity;
import com.shosen.max.ui.activity.circle.adapter.CircleFragmentAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CircleWrapperFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.fa_button_submit)
    FloatingActionButton faButtonSubmit;
    @BindView(R.id.tl_top_tab)
    TabLayout tlTopTab;
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
    private Unbinder unbinder;

    public static boolean isForeground = false;

    public static CircleWrapperFragment newInstance() {
        CircleWrapperFragment circleWrapperFragment = new CircleWrapperFragment();
        return circleWrapperFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_circle_home, null);
        unbinder = ButterKnife.bind(this, contentView);
        initTopHeader(contentView);
        return contentView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        StatusBarUtil.setLightMode(getActivity());
        StatusBarUtil.setColorNoTranslucent(getActivity(), Color.WHITE);
        if (!isHidden()) {
            if (!LoginUtils.isLogin) {
                ActivityUtils.startActivity(LoginActivity.class);
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void initEventAndData() {
        ivBack.setVisibility(View.GONE);
        ivRight.setImageResource(R.drawable.user_icon);
        ivRight.setVisibility(View.VISIBLE);
        tlTopTab.addOnTabSelectedListener(this);
        tvHeadTitle.setText("圈子");
        tlTopTab.addOnTabSelectedListener(this);
        for (int i = 0; i < 2; i++) {
            String text = "";
            switch (i) {
                case 0:
                    text = "圈子";
                    break;
                case 1:
                    text = "发现";
                    break;
            }
            tlTopTab.addTab(tlTopTab.newTab().setText(text));
        }
        vpContainer.setAdapter(new CircleFragmentAdapter(getChildFragmentManager(),
                DataGenerator.getCircleFragments()));
        vpContainer.addOnPageChangeListener(this);
    }

    public void initTopHeader(View view) {
        RelativeLayout flTop = (RelativeLayout) view.findViewById(R.id.rl_top_header);
        if (flTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) flTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(getActivity());
            flTop.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }

    @OnClick({R.id.fa_button_submit, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                ActivityUtils.startActivity(CircleUserMineActivity.class);
                break;
            case R.id.fa_button_submit:
                //TODO 发布状态
                ActivityUtils.startActivity(SubmitActivity.class);
                break;
            default:
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        vpContainer.setCurrentItem(position);
        if (position == 0) {
            faButtonSubmit.setVisibility(View.VISIBLE);
        } else {
            faButtonSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        tlTopTab.getTabAt(i).select();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
