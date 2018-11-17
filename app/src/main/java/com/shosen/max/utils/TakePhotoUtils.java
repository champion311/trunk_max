package com.shosen.max.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.shosen.max.constant.Contstants;

import java.io.File;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class TakePhotoUtils {

    private static String TAG = "TakePhotoUtils";

    /**
     * 调用拍照camera
     *
     * @param mContext
     */
    public static void openCamera(Activity mContext) {
        if (mContext == null) {
            return;
        }
//        String path = Environment.getExternalStorageDirectory() +
//////                "/Android/data/com.shosen.max/files/";
        //String path = Environment.getExternalStorageDirectory() + "/images/";
//        String path = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator;
//        File file = new File(path, "output.jpg");
        File file = getOutPutFile(mContext);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageUri = null;
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.shosen.max.provider", file);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intentFromCapture.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intentFromCapture.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

        } else {
            imageUri = Uri.fromFile(file);
        }
        intentFromCapture.putExtra("imageUri", imageUri.toString());
        mContext.startActivityForResult(intentFromCapture, Contstants.CAMERA_REQUEST_CODE);
    }

    public static void openGallery(Activity mContext) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            Uri uriForFile = FileProvider.getUriForFile
                    (mContext, "com.shosen.max.provider", getGalleryFile(mContext));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.startActivityForResult(intent, Contstants.SELECT_PIC_NOUGAT);
        } else {
            //7.0以下不需要设置
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mGalleryFile));
            mContext.startActivityForResult(intent, Contstants.SELECT_PIC_UNDER_NOUGAT);
        }
    }

    public static void startPhotoZoom(Activity activity, Uri inputUri) {
        if (inputUri == null) {
            Log.e("error", "The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        File mCropFile = getCropFile(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = FileUtils.getFilePathByUri(activity, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat", "JPEG");
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        activity.startActivityForResult(intent, Contstants.CROP_IMAGE_REQUEST_CODE);//
    }


    //external-files-path  mContext.getExternalFilesDir
    public static File getOutPutFile(Context mContext) {
        String path = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator;
        File file = new File(path, "output.jpg");
        return file;
    }

    public static File getGalleryFile(Context mContext) {
        String path = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator;
        File file = new File(path, "gallery.jpg");
        return file;
    }

    public static File getCropFile(Context mContext) {
        String path = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator;
        File file = new File(path, "cropped.jpg");
        return file;
    }

    public static File getSavedBitmapFile(Context mContext) {
        String path = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator;
        File file = new File(path, "saved_image.jpg");
        return file;
    }

}
