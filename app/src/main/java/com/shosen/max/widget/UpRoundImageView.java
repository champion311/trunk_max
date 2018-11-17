package com.shosen.max.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shosen.max.R;

public class UpRoundImageView extends ImageView {

    private float radius = 10f;

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private static final int COLORDRAWABLE_DIMENSION = 2;

    private Bitmap mBitmap;

    private Paint mPaint;

    private BitmapShader mBitmapShader;

    private int mBitmapWidth;
    private int mBitmapHeight;

    private static final int DEFAULT_RADIUS = 10;
    public static final int DRAW_ALL = 0;
    public static final int DRAW_TOP = 1;
    public static final int DRAW_BOTTOM = 2;

    private int drawType = DRAW_ALL;


    public UpRoundImageView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_CROP);
    }

    public UpRoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        setScaleType(ScaleType.CENTER_CROP);
    }

    public UpRoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.CENTER_CROP);
        TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.UpRoundImageView, defStyleAttr, 0);
        drawType = a.getInt(R.styleable.UpRoundImageView_drawType, DRAW_ALL);
        radius = a.getDimensionPixelSize(R.styleable.UpRoundImageView_radius, DEFAULT_RADIUS);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        if (drawType == DRAW_ALL) {
            RectF rectF = new RectF(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(rectF, radius, radius, mPaint);
        } else if (drawType == DRAW_TOP) {
            Path path = new Path();
            path.moveTo(0, radius);
            path.quadTo(0, 0, radius, 0);
            path.lineTo(getWidth() - radius, 0);
            path.quadTo(getWidth(), 0, getWidth(), radius);
            path.lineTo(getWidth(), getHeight());
            path.lineTo(0, getHeight());
            path.lineTo(0, radius);
            canvas.clipPath(path);
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    public void setup() {

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 利用BitmapShader绘制圆角图片
     *
     * @param bitmap    待处理图片
     * @param outWidth  结果图片宽度，一般为控件的宽度
     * @param outHeight 结果图片高度，一般为控件的高度
     * @param radius    圆角半径大小
     * @return 结果图片
     */
    private Bitmap roundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }
        // 初始化缩放比
        float widthScale = outWidth * 1.0f / bitmap.getWidth();
        float heightScale = outHeight * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);

        // 初始化绘制纹理图
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 根据控件大小对纹理图进行拉伸缩放处理
        bitmapShader.setLocalMatrix(matrix);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);

        // 初始化目标画布
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用画笔将纹理图绘制到画布上面
        targetCanvas.drawRoundRect(new RectF(0, 0, outWidth, outWidth), radius, radius, paint);

        return targetBitmap;
    }


    /**
     * 利用BitmapShader绘制底部圆角图片
     *
     * @param bitmap    待处理图片
     * @param outWidth  结果图片宽度，一般为控件的宽度
     * @param outHeight 结果图片高度，一般为控件的高度
     * @param radius    圆角半径大小
     * @return 结果图片
     */
    private Bitmap roundBottomBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }
        // 初始化缩放比
        float widthScale = outWidth * 1.0f / bitmap.getWidth();
        float heightScale = outHeight * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);

        // 初始化绘制纹理图
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 根据控件大小对纹理图进行拉伸缩放处理
        bitmapShader.setLocalMatrix(matrix);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);

        // 初始化目标画布
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用画笔绘制底部圆角
        targetCanvas.drawRoundRect(new RectF(0, outHeight - 2 * radius, outWidth, outWidth), radius, radius, paint);

        // 利用画笔绘制顶部上面直角部分
        targetCanvas.drawRect(new RectF(0, 0, outWidth, outHeight - radius), paint);

        return targetBitmap;
    }

}
