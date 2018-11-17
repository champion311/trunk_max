package com.shosen.max;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.view.CropImageView;
import com.shosen.max.constant.Contstants;
import com.shosen.max.others.span.GlideImageLoader;
import com.shosen.max.receiver.JPushReceiver;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareConfig;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MaxApplication extends Application {
    private static MaxApplication application;

    @Override
    public void onCreate() {
        Log.d("tag", "test");
        application = this;
        init();
        super.onCreate();
    }

    public static Application getAppContext() {
        return application;
    }

    public void init() {
        CrashReport.initCrashReport(this, "0e96973031", false);
        Utils.init(this);
        initImagePicker();
        initUmeng();
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.setAlias(this, "2", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("application", s);
            }
        });
    }

    public void initUmeng() {

        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, Contstants.UMENG_APP_ID
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //签名ID 签名APP_SECRET
        PlatformConfig.setWeixin(Contstants.WEI_XIN_APP_ID, Contstants.WEI_XIN_APP_SECRET);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(9);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }
}
