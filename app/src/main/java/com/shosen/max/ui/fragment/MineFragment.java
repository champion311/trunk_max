package com.shosen.max.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseFragment;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.User;
import com.shosen.max.presenter.MineFragmentPresenter;
import com.shosen.max.presenter.contract.MineFragmentContract;
import com.shosen.max.ui.activity.circle.CircleActivity;
import com.shosen.max.ui.activity.LoginActivity;
import com.shosen.max.ui.activity.MyAllowanceActivity;
import com.shosen.max.ui.activity.MyOrderActivity;
import com.shosen.max.ui.activity.OrderCarActivity;
import com.shosen.max.ui.activity.PartnerActivity;
import com.shosen.max.ui.activity.SettingActivity;
import com.shosen.max.ui.activity.UserInfoActivity;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.SpanUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.CircleImageView;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment implements MineFragmentContract.View {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_my_signature)
    TextView tvMySignature;
    @BindView(R.id.tv_coop_order)
    TextView tvCoopOrder;
    @BindView(R.id.tv_coop_parnter)
    TextView tvCoopParnter;
    @BindView(R.id.fl_my_partner)
    RelativeLayout flMyPartner;
    @BindView(R.id.rl_my_max)
    RelativeLayout rlMyMax;
    @BindView(R.id.rl_my_order)
    RelativeLayout rlMyOrder;
    @BindView(R.id.rl_my_invite)
    RelativeLayout rlMyInvite;
    @BindView(R.id.my_allowance)
    RelativeLayout myAllowance;
    @BindView(R.id.tv_quit_login)
    TextView tvQuitLogin;
    @BindView(R.id.tv_coop_order_alter)
    TextView tvCoopOrderAlter;
    @BindView(R.id.tv_coop_parnter_alter)
    TextView tvCoopParnterAlter;
    @BindView(R.id.fl_my_partner_alter)
    RelativeLayout flMyPartnerAlter;

    private Unbinder unbinder;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    public static final int SETTING_REQUEST_CODE = 100;

    private MineFragmentPresenter mPresenter;

    private String remainNum;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new MineFragmentPresenter(getActivity());
        setPresenter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, contentView);
        initTopHeader(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }

    public void initTopHeader(View view) {
        FrameLayout flTop = (FrameLayout) view.findViewById(R.id.fl_top_wrapper);
        if (flTop != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) flTop.getLayoutParams();
            layoutParams.topMargin =
                    StatusBarUtil.getStatusBarHeight(getActivity());
            flTop.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void initEventAndData() {
        //refreshUI();
        mPresenter.getDictionaryData("1");
        mPresenter.getDictionaryData("2");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isHidden()) {
            StatusBarUtil.setLightMode(getActivity());
            StatusBarUtil.setColorNoTranslucent(getActivity(), Color.WHITE);
        } else {
            StatusBarUtil.setDarkMode(getActivity());
            StatusBarUtil.setColorNoTranslucent(getActivity(), getActivity().getResources().getColor(R.color.seleted_text_color));
        }
    }

    @OnClick({R.id.rl_my_order, R.id.tv_quit_login, R.id.rl_my_invite, R.id.my_allowance,
            R.id.iv_avatar, R.id.iv_setting, R.id.fl_my_partner,
            R.id.rl_my_max, R.id.fl_my_partner_alter})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_order:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_quit_login:
                //已经隐藏
                break;
            case R.id.rl_my_invite:
//                if (!LoginUtils.isLogin) {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                    return;
//                }
//                startActivity(new Intent(getActivity(), MyInvitationActivity.class));
                //CrashReport.testJavaCrash();
                ActivityUtils.startActivity(CircleActivity.class);
                break;
            case R.id.my_allowance:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyAllowanceActivity.class));
                break;
            case R.id.iv_avatar:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.iv_setting:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivityForResult(new Intent(getActivity(), SettingActivity.class), SETTING_REQUEST_CODE);
                break;
            case R.id.fl_my_partner:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), PartnerActivity.class));
                break;
            case R.id.fl_my_partner_alter:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), PartnerActivity.class));
                break;
            case R.id.rl_my_max:
                if (!LoginUtils.isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), OrderCarActivity.class));
                break;
            default:
                break;
        }
    }

    public void refreshUI() {
        if (LoginUtils.isLogin) {
            GlideUtils.loadImage(this, LoginUtils.mUser.getHeadimg(), ivAvatar);
            if (!TextUtils.isEmpty(LoginUtils.mUser.getName())) {
                tvUserName.setText(LoginUtils.mUser.getName());
            } else {
                tvUserName.setText(LoginUtils.mUser.getPhone());
            }
            tvQuitLogin.setText("退出登录");
            tvMySignature.setText(LoginUtils.mUser.getSign());
        } else {
            ivAvatar.setImageResource(R.drawable.default_avater);
            tvUserName.setText("请登录");
            tvQuitLogin.setText("登录");
            tvMySignature.setText("");
        }
        //初始化字体
        Typeface customFort =
                Typeface.createFromAsset(getActivity().getAssets(), "libian_sc2.ttf");
        tvCoopOrder.setTypeface(customFort);
        tvCoopParnter.setTypeface(customFort);
        tvCoopOrderAlter.setTypeface(customFort);
        tvCoopParnterAlter.setTypeface(customFort);
        if (!LoginUtils.isLogin || "0".equals(LoginUtils.getUser().getOrderNum())) {
            //还不是合伙人
            flMyPartnerAlter.setVisibility(View.GONE);
            flMyPartner.setVisibility(View.VISIBLE);
            tvCoopOrder.setText("超级合伙人");
            if (LoginUtils.isLogin) {
                remainNum = LoginUtils.getUser().getRemainNum();
            }
            if (!TextUtils.isEmpty(remainNum)) {
                SpannableStringBuilder builder =
                        SpanUtils.makeColorSpan(getActivity(),
                                "剩余", "个席位", remainNum);
                tvCoopParnter.setText(builder);
            }
        } else if (!TextUtils.isEmpty(LoginUtils.getUser().getOrderNum())) {
            //已经是合伙人
            flMyPartnerAlter.setVisibility(View.VISIBLE);
            flMyPartner.setVisibility(View.GONE);
            String orderNum = LoginUtils.getUser().getOrderNum();
            SpannableStringBuilder builder =
                    SpanUtils.makeColorSpan(getActivity(), "恭喜你成为第", "位", orderNum);
            tvCoopOrderAlter.setText(builder);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SETTING_REQUEST_CODE:
                refreshUI();
                break;
            default:
                break;
        }
    }

    /**
     * 每一次切换到这个页面进行更新
     */
    @Override
    public void onResume() {
        super.onResume();
        if (LoginUtils.isLogin) {
            mPresenter.selectUserByPhone(LoginUtils.getUser().getPhone());
        } else {
            //没有登陆的时候
            //refreshUI();
            mPresenter.getRemainOwner();
        }
    }

    /**
     * 更新用户信息成功
     *
     * @param updateUser
     */
    @Override
    public void updateUserData(User updateUser) {
        //更新User数据
        User user = LoginUtils.mUser;
        user.updateUserData(updateUser);
        LoginUtils.putUser(user);
        refreshUI();
    }

    /**
     * 更新用户信息失败
     *
     * @param message
     */
    @Override
    public void updateDataFailed(String message) {
        refreshUI();
    }

    /**
     * 未登陆的时候获取remainNum
     *
     * @param remainNum
     */
    @Override
    public void getRemainNumSuccess(String remainNum) {
        this.remainNum = remainNum;
        refreshUI();
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(getActivity(), message);
    }

    @Override
    public void showDictionaryData(String type, List<DictionaryBean> mData) {
        //DO NOTHING
    }
}
