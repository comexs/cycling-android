package com.alex.cycling.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.alex.cycling.R;

/**
 * Created by youzehong on 16/4/19.
 */
public class ArcProgressBar extends View {

    private Paint mArcPaint;
    private Paint mBackArcPaint;
    private Paint mTextPaint;
    private Paint mProgressPaint;
    private RectF mRountRect;
    private RectF mArcRect;
    /**
     * 圆弧宽度
     */
    private float mArcWidth = 10.0f;
    /**
     * 圆弧颜色
     */
    private int mArcBgColor = 0xFF2CAF61;
    /**
     * 圆弧两边的距离
     */
    private int mPdDistance = 50;
    /**
     * 底部默认文字
     */
    private String mArcText = "";
    /**
     * 线条数
     */
    private int mDottedLineCount = 100;
    /**
     * 线条宽度
     */
    private int mDottedLineWidth = 40;
    /**
     * 线条高度
     */
    private int mDottedLineHeight = 6;
    /**
     * 圆弧跟虚线之间的距离
     */
    private int mLineDistance = 20;
    /**
     * 进度条最大值
     */
    private int mProgressMax = 100;
    /**
     * 进度文字大小
     */
    private int mProgressTextSize = 40;
    /**
     * 进度描述
     */
    private String mProgressDesc;

    private int mProgress;
    private int mArcCenterX;
    private int mArcRadius; // 圆弧半径
    private double bDistance;
    private double aDistance;
    private boolean isRestart = false;
    private int mRealProgress;

    public ArcProgressBar(Context context) {
        this(context, null, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiAttributes(context, attrs);
        initView();
    }

    private void intiAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
        mPdDistance = a.getInteger(R.styleable.ArcProgressBar_arcDistance, mPdDistance);
        mArcBgColor = a.getColor(R.styleable.ArcProgressBar_arcBgColor, mArcBgColor);
//        mDottedDefaultColor = a.getColor(R.styleable.ArcProgressBar_dottedDefaultColor, mDottedDefaultColor);
//        mDottedRunColor = a.getColor(R.styleable.ArcProgressBar_dottedRunColor, mDottedRunColor);
        mDottedLineCount = a.getInteger(R.styleable.ArcProgressBar_dottedLineCount, mDottedLineCount);
        mDottedLineWidth = a.getInteger(R.styleable.ArcProgressBar_dottedLineWidth, mDottedLineWidth);
        mDottedLineHeight = a.getInteger(R.styleable.ArcProgressBar_dottedLineHeight, mDottedLineHeight);
        mLineDistance = a.getInteger(R.styleable.ArcProgressBar_lineDistance, mLineDistance);
        mProgressMax = a.getInteger(R.styleable.ArcProgressBar_progressMax, mProgressMax);
        mProgressTextSize = a.getInteger(R.styleable.ArcProgressBar_progressTextSize, mProgressTextSize);
        mProgressDesc = a.getString(R.styleable.ArcProgressBar_progressDesc);
        mArcText = a.getString(R.styleable.ArcProgressBar_arcText);
        if (TextUtils.isEmpty(mArcText)) {
            mArcText = "0km";
        }
        a.recycle();
    }

    private void initView() {
        // 外层圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setColor(mArcBgColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mBackArcPaint = new Paint();
        mBackArcPaint.setAntiAlias(true);
        mBackArcPaint.setStyle(Paint.Style.STROKE);
        mBackArcPaint.setStrokeWidth(mArcWidth);
        mBackArcPaint.setColor(Color.BLACK);
        mBackArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(getResources(), 20));
        mTextPaint.setColor(Color.WHITE);
        // 中间进度画笔
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.WHITE);
        mProgressPaint.setTextSize(dp2px(getResources(), mProgressTextSize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcCenterX = (int) (w / 2.f);

        mArcRect = new RectF();
        mArcRect.top = 0;
        mArcRect.left = 0;
        mArcRect.right = w;
        mArcRect.bottom = h;

        mArcRect.inset(mArcWidth / 2, mArcWidth / 2);
        mArcRadius = (int) (mArcRect.width() / 2);

        bDistance = Math.cos(Math.PI * 45 / 180) * mArcRadius;
        aDistance = Math.sin(Math.PI * 45 / 180) * mArcRadius;

        mRountRect = new RectF();
        mRountRect.left = (float) (2 * mArcCenterX - 2 * aDistance) / 2 - mArcWidth / 2 + 40;
        mRountRect.top = (float) (mArcCenterX + bDistance) - 20;
        mRountRect.right = (float) (2 * mArcCenterX - (2 * mArcCenterX - 2 * aDistance) / 2) - mArcWidth / 2 - 40;
        mRountRect.bottom = (float) (mArcRadius + mArcRadius) - 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mArcRect, 135, 270, false, mBackArcPaint);

        canvas.drawText(mArcText, mArcRadius - mTextPaint.measureText(mArcText) / 2,
                (float) (mArcRadius + bDistance) - (mTextPaint.descent() + mTextPaint.ascent()), mTextPaint);

        drawRunText(canvas);
        drawRunProgress(canvas);
    }

    private void drawRunText(Canvas canvas) {
        String progressStr = this.mRealProgress + "%";
        if (!TextUtils.isEmpty(mProgressDesc)) {
            progressStr = mProgressDesc;
        }
        canvas.drawText(progressStr, mArcCenterX - mProgressPaint.measureText(progressStr) / 2,
                mArcCenterX - (mProgressPaint.descent() + mProgressPaint.ascent()) / 2 - 20, mProgressPaint);
    }

    private void drawRunProgress(Canvas canvas) {
        canvas.drawArc(mArcRect, 135, mRealProgress, false, mArcPaint);
    }

    public void restart() {
        isRestart = true;
        this.mRealProgress = 0;
        this.mProgressDesc = "";
        invalidate();
    }

    /**
     * 设置中间进度描述
     *
     * @param desc
     */
    public void setProgressDesc(String desc) {
        this.mProgressDesc = desc;
        postInvalidate();
    }

    /**
     * 设置最大进度
     *
     * @param max
     */
    public void setMaxProgress(int max) {
        this.mProgressMax = max;
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        // 进度100% = 控件的75%
        this.mRealProgress = progress;
        isRestart = false;
        this.mProgress = ((mDottedLineCount * 3 / 4) * progress) / mProgressMax;
        postInvalidate();
    }

    public void startAnimation(int progress) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, progress).setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}