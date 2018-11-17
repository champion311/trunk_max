package com.shosen.max.utils;

import android.app.Activity;
import android.content.Context;

import com.shosen.max.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

public class ShareUtils {
    /**
     * 分享小程序
     *
     * @param activity
     * @param umImage
     * @param url
     * @param title
     * @param umShareListener
     */
    public static void shareMin(
            Activity activity, UMImage umImage, String url, String title, UMShareListener umShareListener) {
        UMMin umMin = new UMMin(url);
        //兼容低版本的网页链接
        umMin.setThumb(umImage);
        // 小程序消息封面图片
        umMin.setTitle(title);
        // 小程序消息title
        umMin.setDescription(title);
        // 小程序消息描述
        umMin.setPath("pages/index/index");
        //小程序页面路径
        umMin.setUserName("gh_ca9acc95304d");
        // 小程序原始id,在微信平台查询
        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).share();
    }

    public static void shareUrl(Activity activity, String url, String
            title, UMImage umImage, UMShareListener umShareListener) {
        new ShareAction(activity).
                withMedia(new UMWeb(url)).
                setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).share();


    }
}
