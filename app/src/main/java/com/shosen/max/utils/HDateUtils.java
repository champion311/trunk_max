package com.shosen.max.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**

 */
@SuppressLint("SimpleDateFormat")
public class HDateUtils {
    private void HDateUtils() {

    }

    /**
     * 输入一个秒时间戳，返回一个对应的字符串描述
     *
     * @param millios 秒时间戳
     */
    public static String getDateDesc(long millios) {
        String date_desc = "";
        long now = System.currentTimeMillis() / 1000;
        long date = now - millios;
        if (date < 60) {
            date_desc = "刚刚";
        } else if (date > 60 && date < 3600) {
            int i = (int) (date / 60);
            date_desc = i + "分钟前";
        } else if (date > 3600 && date < 86400) {
            int i = (int) (date / 3600);
            date_desc = i + "小时前";
        } else {
            Date dat = new Date(millios);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(dat);
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm");
            date_desc = format.format(gc.getTime());
        }
        return date_desc;
    }

    public static String getDate(long mills, String formatStr) {
        Date dat = new Date(mills);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    formatStr);
            return format.format(gc.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 输入一个秒时间戳，返回一个对应的日期，年-月-日
     *
     * @param millios 秒时间戳
     * @return
     */
    public static String getDate(long millios) {
        Date dat = new Date(millios * 1000);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = format.format(gc.getTime());
        return date;
    }

    /**
     * 输入一个秒时间戳，返回一个对应的日期，年-月-日  时 -分
     *
     * @param millios 秒时间戳
     * @return
     */
    public static String getDateTime(long millios) {
        Date dat = new Date(millios * 1000);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String date = format.format(gc.getTime());
        return date;
    }


    /**
     * 获取系统当前年
     */
    public static String getCurrentYear() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * 获取系统当前时间 时间格式 年 -月 -日
     */

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取系统当前时间 时间格式 年 -月 -日 时 -分 -秒
     */

    public static String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取唯一Id
     */
    public static String getId() {
        String id = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Random rand = new Random();
        int number = rand.nextInt(100);
        id = sdf.format(date) + number;
        return id;
    }


}