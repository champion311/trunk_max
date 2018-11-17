package com.shosen.max.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.shosen.max.bean.User;

public class LoginUtils {

    public static boolean isLogin = false;

    public static User mUser;

    private static SPUtils spUtils = SPUtils.getInstance("login_info");

    public static void putUser(User user) {
        if (user == null) {
            return;
        }
        isLogin = true;
        mUser = user;
        //commit 运行在主线程当中
        spUtils.put("user", new Gson().toJson(mUser), false);
    }

    public static User getUser() {
        if (mUser == null) {
            String gsonStr = spUtils.getString("user");
            mUser = new Gson().fromJson(gsonStr, User.class);
            if (mUser != null) {
                isLogin = true;
            } else {
                isLogin = false;
            }
        }
        return mUser;
    }

    /**
     * 登陆者信息修改完后，保存到本地
     *
     * @param ctx 上下文
     * @return 保存成功返回true，其它返回false
     */
    @Deprecated
    public static synchronized boolean saveUserInfo(Context ctx) {
        if (mUser != null) {
            //commit 运行在主线程当中
            spUtils.put("user", new Gson().toJson(mUser), false);
            return true;
        } else {
            isLogin = false;
            return false;
        }
    }

    public static void quitLogin() {
        //commit 运行在主线程当中
        spUtils.clear(false);
        isLogin = false;
        mUser = null;
    }

    public static boolean isMe(String userId) {
        return LoginUtils.isLogin &
                LoginUtils.getUser().getUid().equals(userId);
    }
}
