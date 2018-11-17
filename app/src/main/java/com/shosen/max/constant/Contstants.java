package com.shosen.max.constant;

public class Contstants {

    //TODO 定义一些状态码
    public static final int NET_ERROR = 0x01;

    public static final int NET_SUCCESS = 0x01 + 1;

    public static boolean isLogin = false;


    public static final String CHARITY_URL = "https://api.shosen.cn/news/gy.html";//慈善网址

    public static final String MAX_URL = "https://api.shosen.cn/news/news.html";//MAX网址

    public static final String WEB_VIEW_CACHE_FILE_NAME = "maxmake_webview_cache";//默认缓存位置

    public static final String CACHE_FILE_NAME = "maxmake_cache";//默认网络缓存位置

    //拍摄图片
    public static final int CAMERA_REQUEST_CODE = 500;
    @Deprecated
    public static final int GALLERY_REQUEST_CODE = 501;

    public static final int CROP_IMAGE_REQUEST_CODE = 503;//裁剪图片

    public static final int SELECT_PIC_NOUGAT = 504;//从相册中获取图片 android版本>=N

    public static final int SELECT_PIC_UNDER_NOUGAT = 505;//从相册中获取图片 android版本<N


    //	wx482945bce5f90e1b
    public static final String WEI_XIN_APP_ID = "wx482945bce5f90e1b";
    //c77f05ea6aadcb1833f2adb7e1ffe271
    public static final String WEI_XIN_APP_SECRET = "c77f05ea6aadcb1833f2adb7e1ffe271";
    //5bbdbcc7f1f556c5ea000036
    public static final String UMENG_APP_ID = "5bbdbcc7f1f556c5ea000036";

    public static final String BUGLY_APP_KEY = "";

    //5bbc78d8b465f5bb1b00008e
    //支付状态
    public static final int PAY_SUCCESS = 0x100;

    public static final int PAY_FAILED = 0x100 + 1;

    public static final int PAY_CANCELED = 0x100 + 2;

    public static final int PAY_NOT_YET = 0x100 + 3;
    //字典数据缓存
    public static final String DICTIONARY_CACHE = "dictionary_cache";
    //调用发布状态
    public static final int IMAGE_ITEM_ADD = -1;

    public static final int REQUEST_CODE_SELECT = 100;

    public static final int REQUEST_CODE_PREVIEW = 101;

    public static final int CIRCLE_FRAGMENT = 201;

    public static final int FINDING_FRAGMENT = 202;

    //未读消息SP
    public static final String SP_NEW_MESSAGE = "sp_new_message";

    public static final String NEW_MESSAGE_COUNT = "new_message_count";


}
