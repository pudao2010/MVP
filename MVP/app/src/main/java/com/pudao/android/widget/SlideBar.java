package com.pudao.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pudao.android.R;
import com.pudao.android.utils.DensityUtil;

/**
 * Created by pucheng on 2017/2/27.
 *
 */

public class SlideBar extends View {

    public static final String TAG = "SlideBar";

    private float mTextSize = 0;

    private static final String[] SECTIONS = {"↑"," ☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private Paint mPaint;

    private int mCurrentIndex = - 1;

    private float mTextBaseline = 0;

    private OnSlideBarChangeListener mOnSlideBarChangeListener;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.section_text_color));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 12));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //计算分配给字符的高度
        mTextSize = h * 1.0f / SECTIONS.length;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //获取绘制字符的实际高度
        float mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        //计算字符居中时的baseline
        mTextBaseline = mTextSize / 2 + mTextHeight/2 - fontMetrics.descent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() * 1.0f / 2;
        float baseline = mTextBaseline;
        for(int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, baseline, mPaint);
            baseline += mTextSize;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.bg_slide_bar);
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_MOVE:
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                if (mOnSlideBarChangeListener != null) {
                    mOnSlideBarChangeListener.onSlidingFinish();
                }
                break;
        }
        return true;
    }

    private void notifySectionChange(MotionEvent event) {
        int index = getTouchIndex(event);
        if (mOnSlideBarChangeListener != null && mCurrentIndex != index) {
            mCurrentIndex = index;
            mOnSlideBarChangeListener.onSectionChange(index, SECTIONS[index]);
        }
    }

    private int getTouchIndex(MotionEvent event) {
        int index = (int) (event.getY() / mTextSize);
        if (index < 0) {
            index = 0;
        } else if (index > SECTIONS.length - 1) {
            index = SECTIONS.length - 1;
        }
        return index;
    }

    public interface OnSlideBarChangeListener{

        void onSectionChange(int index, String section);

        void onSlidingFinish();

    }

    public void setOnSlidingBarChangeListener(OnSlideBarChangeListener l) {
        mOnSlideBarChangeListener = l;
    }

}