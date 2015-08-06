package com.satan.swipebacklibrary;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by satan on 2015/8/1.
 */
public class SwipeBackFrameLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mContentView;
    private int mContentWidth;
    private int mMoveLeft;
    private boolean isClose = false;
    private CallBack mCallBack;

    public SwipeBackFrameLayout(Context context) {
        this(context, null);
    }

    public SwipeBackFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            //返回true表示可以拖动
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mContentView;
            }

            //记录值的变化
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                mMoveLeft = left;
                if (isClose && (left == mContentWidth)) {
                    mCallBack.onFinish();
                }
            }

            //手指松开会触发这个方法，做复位操作就在此方法中实现
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //一定得重写computeScroll()方法，不然没有效果
                //如果移动的距离大于或等于当前界面的1/2，则触发关闭
                if (mMoveLeft >= (mContentWidth / 2)) {
                    isClose = true;
                    mViewDragHelper.settleCapturedViewAt(mContentWidth, releasedChild.getTop());
                    invalidate();
                } else {
                    mViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                    invalidate();
                }
            }

            //重新定位水平移动的位置
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.min(mContentWidth, Math.max(left, 0));
            }


            //关键方法：设置水平拖动的距离
            @Override
            public int getViewHorizontalDragRange(View child) {
                return mContentWidth;
            }

        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mContentWidth = mContentView.getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onFinish();
    }
}
