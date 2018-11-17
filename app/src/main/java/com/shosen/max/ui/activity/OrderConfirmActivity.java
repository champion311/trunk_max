package com.shosen.max.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.presenter.OrderCarPresenter;
import com.shosen.max.presenter.contract.OrderCarContract;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener, OrderCarContract.View {


    //申明对象
    CityPickerView mPicker;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_cellphone)
    TextView tvCellphone;
    @BindView(R.id.et_cellphone)
    EditText etCellphone;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_pay_continue)
    TextView tvPayContinue;
    @BindView(R.id.tv_zone_arrow)
    TextView tvZoneArrow;
    @BindView(R.id.tv_invite_cellphone)
    TextView tvInviteCellphone;
    @BindView(R.id.et_invite_cellphone)
    EditText etInviteCellphone;

    private String provinceText;

    private String cityText;

    private OrderCarPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new OrderCarPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        tvUserProtocol.setSelected(true);
        tvHeadTitle.setText("确认预订");
        mPicker = new CityPickerView();
        mPicker.init(this);
        CityConfig cityConfig = new
                CityConfig.Builder().province("广东省").city("广州市").district("市辖区").
                setLineColor(getResources().getString(R.color.diver_color)).
                title("选择省市").titleBackgroundColor(getResources().getString(R.color.white)).
                drawShadows(false).
                build();
        mPicker.setConfig(cityConfig);
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                //ToastUtils.show(OrderConfirmActivity.this, province.getName() + city.getName() + district.getName());
                if (province != null && city.getName() != null) {
                    provinceText = province.getName();
                    cityText = city.getName();
                    tvZoneArrow.setText(province.getName() + " " + city.getName() + " " + district);
                }
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.acitivity_certain_order;
    }

    @OnClick({R.id.iv_back, R.id.tv_zone_arrow, R.id.tv_user_protocol,
            R.id.tv_pay_continue, R.id.et_cellphone, R.id.et_invite_cellphone})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_zone_arrow:
                if (mPicker != null) {
                    mPicker.showCityPicker();
                }
                break;
            case R.id.tv_user_protocol:
                tvUserProtocol.setSelected(!tvUserProtocol.isSelected());
                break;
            case R.id.tv_pay_continue:
                if (!LoginUtils.isLogin) {
                    return;
                }
                String orderName = etName.getText().toString().trim();
                String contactPhone = etCellphone.getText().toString().trim();
                String zoneText = tvZoneArrow.getText().toString().trim();
                String invitePhone = etInviteCellphone.getText().toString().trim();
                if (!tvUserProtocol.isSelected()) {
                    ToastUtils.show(this, "请同意用户协议");
                    return;
                }
                if (TextUtils.isEmpty(orderName)) {
                    ToastUtils.show(this, "请输入姓名");
                    return;
                }
                if (!RegexUtils.isMobileSimple(contactPhone)) {
                    ToastUtils.show(this, "请输入正确的联系电话");
                    return;
                }
                if (TextUtils.isEmpty(zoneText)) {
                    ToastUtils.show(this, "请选择地区");
                    return;
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookPhone", LoginUtils.getUser().getPhone());
                //map.put("bookMoney", "25000");
                map.put("bookProvince", provinceText);
                map.put("bookCity", cityText);
                map.put("contactPhone", contactPhone);
                if (RegexUtils.isMobileSimple(invitePhone)) {
                    map.put("invitorPhone", invitePhone);
                }
                mPresenter.bookOrder(map);
                break;
            case R.id.et_cellphone:
                etCellphone.setFocusable(true);
                etCellphone.requestFocus();
                etCellphone.setFocusableInTouchMode(true);
                break;
            case R.id.et_invite_cellphone:
                etInviteCellphone.setFocusable(true);
                etInviteCellphone.requestFocus();
                etInviteCellphone.setFocusableInTouchMode(true);
                break;
        }
    }


    @Override
    public void payBack(OrderResponse response) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("bookId", response.getId());
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }
}
