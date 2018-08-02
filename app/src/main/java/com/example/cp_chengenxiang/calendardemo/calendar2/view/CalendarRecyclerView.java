package com.example.cp_chengenxiang.calendardemo.calendar2.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author:      cgx
 * Date:        2018/7/20
 * Describe:    屏宽条目横向全屏滑动
 */
public class CalendarRecyclerView extends RecyclerView {

    private int mStartX;
    private int mScrollPosition;     //目标条目position
    private int mPageScrollDiff;     //页面切换的最小距离

    public CalendarRecyclerView(Context context) {
        this(context, null);
    }

    public CalendarRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPageScrollDiff = 200;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) e.getX();
                mScrollPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                requestFocus();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) e.getX();
                if (Math.abs(upX - mStartX) > mPageScrollDiff) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) e.getX();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) e.getX();
                if (Math.abs(upX - mStartX) > mPageScrollDiff) {
                    if (upX > mStartX) {    //页面左滑
                        mScrollPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                    } else {
                        mScrollPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                    }
                }

                //解首条目左滑或末尾条目右滑点击失效问题 *_*
                if (mScrollPosition == 0 || (getAdapter() != null && getAdapter().getItemCount() - 1 == mScrollPosition)) {
                    scrollBy(mScrollPosition == 0 ? 1 : -1, 0);
                }

                smoothScrollToPosition(mScrollPosition);
                return true;
        }
        return super.onTouchEvent(e);
    }
}
