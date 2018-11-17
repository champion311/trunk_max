package com.shosen.max.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.User;
import com.shosen.max.bean.UserDetetail;
import com.shosen.max.constant.Contstants;
import com.shosen.max.presenter.UserInfoPresenter;
import com.shosen.max.presenter.contract.UserContract;
import com.shosen.max.ui.adapter.ItemGridAdapter;
import com.shosen.max.ui.adapter.SelectorDialogAdapter;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.FileUtils;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.TakePhotoUtils;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.CircleImageView;
import com.shosen.max.widget.dialog.SelectPhotoDialog;
import com.shosen.max.widget.dialog.SelectorDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class UserInfoActivity extends BaseActivity implements
        SelectorDialog.SelectorClick, UserContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.fl_avater)
    FrameLayout flAvater;
    @BindView(R.id.ed_user_name)
    EditText edUserName;
    @BindView(R.id.ed_signature)
    EditText edSignature;
    @BindView(R.id.ra_male)
    RadioButton raMale;
    @BindView(R.id.ra_female)
    RadioButton raFemale;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.tv_identity)
    TextView tvIdentity;
    @BindView(R.id.fl_identity)
    FrameLayout flIdentity;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.fl_zone_selector)
    FrameLayout flZoneSelector;
    @BindView(R.id.msignature_grid)
    RecyclerView msignatureGrid;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private SelectorDialog selectorDialog;

    private SelectPhotoDialog selectPhotoDialog;

    private CityPickerView mPicker;

    RxPermissions rxPermissions;

    private UserInfoPresenter mPresenter;

    private ItemGridAdapter itemGridAdapter;

    //标记头像是否发生变化
    private boolean isAvaterChanged = false;

    private String title;//身份

    private String provinceText;

    private String cityText;

    private List<DictionaryBean> titleDatas;//身份数据




    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new UserInfoPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
        rxPermissions = new RxPermissions(this);
        tvHeadTitle.setText("个人介绍");
        //获取爱好数据
        mPresenter.getDictionaryData("1");
        //获取身份(头衔title)数据
        mPresenter.getDictionaryData("2");
        //初始化爱好grid
        msignatureGrid.setLayoutManager(new GridLayoutManager(this, 3));
        msignatureGrid.addItemDecoration(new SpaceItemDecoration());
        initCityDialog();
        if (LoginUtils.isLogin) {
            User user = LoginUtils.mUser;
            edUserName.setText(user.getName());
            GlideUtils.loadImage(this, user.getHeadimg(), ivAvatar);
            edSignature.setText(user.getSign());
            if ("1".equals(user.getSex())) {
                raMale.setChecked(true);
            } else if ("2".equals(user.getSex())) {
                raFemale.setChecked(true);
            }
            tvZone.setText(user.getProvince() + " " + user.getCity());
        }
        //处理EditText焦点
        edUserName.setFocusable(true);
        edUserName.requestFocus();
        edUserName.setFocusableInTouchMode(true);
    }

    @OnClick({R.id.iv_back, R.id.fl_identity,
            R.id.fl_zone_selector, R.id.tv_submit, R.id.iv_avatar,
            R.id.ed_user_name, R.id.ed_signature})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_identity:
                if (selectorDialog != null) {
                    if (selectorDialog.isShowing()) {
                        selectorDialog.dismiss();
                    }
                    selectorDialog.show();
                }
                break;
            case R.id.fl_zone_selector:
                if (mPicker != null) {
                    mPicker.showCityPicker();
                }
                break;
            case R.id.iv_avatar:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Disposable di = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                            subscribe(grantded -> {
                                if (grantded) {
                                    showAvaterDialog();
                                } else {
                                    //权限被拒绝
                                }
                            });
                    //添加到CompositeDisposable集合当中
                    mPresenter.addSubscribe(di);
                }
                break;
            case R.id.tv_submit:
                //提交结果
                submitData();
                finish();
                break;
            case R.id.ed_user_name:
                edUserName.setFocusable(true);
                edUserName.requestFocus();
                edUserName.setFocusableInTouchMode(true);
                break;
            case R.id.ed_signature:
                edSignature.setFocusable(true);
                edSignature.requestFocus();
                edSignature.setFocusableInTouchMode(true);
                break;
            default:
                break;

        }
    }

    /**
     * 提交修改数据
     */
    public void submitData() {
        if (!LoginUtils.isLogin) {
            return;
        }
        HashMap<String, String> maps = new HashMap<>(16);
        String name = edUserName.getText().toString().trim();
        String sign = edSignature.getText().toString().trim();
        String gender = "1";
        if (raMale.isChecked()) {
            gender = "1";
        } else if (raFemale.isChecked()) {
            gender = "2";
        }
        //title, province,city
        maps.put("name", name);
        maps.put("sign", sign);
        maps.put("sex", gender);
        if (itemGridAdapter != null) {
            if (itemGridAdapter.getSelectedData() != null) {
                maps.put("tabs", itemGridAdapter.getSelectedData());
            }
        }
        if (!TextUtils.isEmpty(title)) {
            maps.put("title", title);
        }
        if (!TextUtils.isEmpty(provinceText)) {
            maps.put("province", provinceText);
        }
        if (!TextUtils.isEmpty(provinceText)) {
            maps.put("city", cityText);
        }
        maps.put("id", LoginUtils.getUser().getUid());
        maps.put("user.securityToken", LoginUtils.getUser().getSecurityToken());
        File submitFile = null;
        if (isAvaterChanged) {
            submitFile = TakePhotoUtils.getSavedBitmapFile(mContext);
        }
        mPresenter.updateUserInfo(submitFile, maps);
    }

    /**
     * 初始化城市弹窗
     */
    public void initCityDialog() {
        mPicker = new CityPickerView();
        mPicker.init(this);
        @SuppressLint("ResourceType") CityConfig cityConfig = new
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
                if (province != null && city.getName() != null) {
                    tvZone.setText(province.getName() + " " + city.getName());
                    provinceText = province.getName();
                    cityText = city.getName();
                }
            }
        });
    }

    /**
     * 显示头像弹窗
     */
    public void showAvaterDialog() {
        if (selectPhotoDialog == null) {
            selectPhotoDialog = new SelectPhotoDialog(this, R.style.CancelDialog);
            selectPhotoDialog.setOwnerActivity(this);
        } else {
            selectPhotoDialog.dismiss();
        }
        selectPhotoDialog.show();
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_user_info;
    }


    /**
     * 更新成功
     *
     * @param mUserDetail
     */
    @Override
    public void updateSuccess(UserDetetail mUserDetail) {
        finish();
    }


    /**
     * title头衔回调
     *
     * @param bean 字典实例
     */
    @Override
    public void selectorClick(DictionaryBean bean) {
        if (bean != null) {
            tvIdentity.setText(bean.getDicValue());
            title = bean.getDicCode();
        }
        if (selectorDialog != null) {
            selectorDialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (selectPhotoDialog != null) {
            if (selectPhotoDialog.isShowing()) {
                selectPhotoDialog.dismiss();
            }
        }
        Uri uri;
        switch (requestCode) {
            case Contstants.GALLERY_REQUEST_CODE:
                Log.d("TAG", data.toString());
                break;
            case Contstants.CAMERA_REQUEST_CODE:
                //调用相机返回结果
                if (getIntent() == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(mContext, "com.shosen.max.provider", TakePhotoUtils.getOutPutFile(this));
                } else {
                    uri = Uri.fromFile(TakePhotoUtils.getOutPutFile(this));
                }
                TakePhotoUtils.startPhotoZoom(this, uri);
                break;
            case Contstants.SELECT_PIC_NOUGAT:
                if (data == null) {
                    return;
                }
                //BuildConfig.APPLICATION_ID
                File imgUri = new File(FileUtils.getFilePathByUri(this, data.getData()));
                uri = FileProvider.getUriForFile
                        (this, "com.shosen.max.provider", imgUri);
                TakePhotoUtils.startPhotoZoom(this, uri);
                break;
            case Contstants.SELECT_PIC_UNDER_NOUGAT:
                if (data == null) {
                    return;
                }
                uri = data.getData();
                TakePhotoUtils.startPhotoZoom(this, uri);
                break;
            case Contstants.CROP_IMAGE_REQUEST_CODE:
                Uri inputUri = FileProvider.
                        getUriForFile(this,
                                "com.shosen.max.provider", TakePhotoUtils.getCropFile(this));
                //通过FileProvider创建一个content类型的Uri
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(inputUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ivAvatar.setImageBitmap(bitmap);
                FileUtils.saveBitmap(bitmap, TakePhotoUtils.getSavedBitmapFile(mContext));
                //头像发生改变
                isAvaterChanged = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void showDictionaryData(String type, List<DictionaryBean> mData) {
        if ("1".equals(type)) {
            //选择爱好
            itemGridAdapter = new ItemGridAdapter(this, mData, LoginUtils.getUser().getTabs());
            msignatureGrid.setAdapter(itemGridAdapter);
        } else if ("2".equals(type)) {
            //选择身份
            this.titleDatas = mData;
            if (mData != null) {
                //显示title数据
                for (DictionaryBean bean : mData) {
                    if (bean.getDicCode().equals(LoginUtils.getUser().getTitle())) {
                        tvIdentity.setText(bean.getDicValue());
                    }
                }
            }
            //初始化 选择头衔dialog
            selectorDialog = new SelectorDialog(this, R.style.CancelDialog);
            SelectorDialogAdapter adapter = new SelectorDialogAdapter(mData, this);
            adapter.setClick(this);
            selectorDialog.setAdapter(adapter);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = DensityUtils.dip2px(UserInfoActivity.this, 14);
        }
    }
}
