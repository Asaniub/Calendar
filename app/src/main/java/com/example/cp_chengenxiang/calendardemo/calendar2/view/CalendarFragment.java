package com.example.cp_chengenxiang.calendardemo.calendar2.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cp_chengenxiang.calendardemo.R;
import com.example.cp_chengenxiang.calendardemo.calendar2.CalendarDateMgr;
import com.example.cp_chengenxiang.calendardemo.calendar2.model.CalendarInfo;
import com.example.cp_chengenxiang.calendardemo.calendar2.adapter.CalendarItemAdapter;
import com.example.cp_chengenxiang.calendardemo.calendar2.adapter.CalendarMainAdapter;
import com.example.cp_chengenxiang.calendardemo.calendar2.holder.CalendarItemBaseHolder;
import com.example.cp_chengenxiang.calendardemo.calendar2.listener.CalendarListener;

import java.util.List;

/**
 * Author:      cgx
 * Date:        2018/7/18
 * Describe:    日历
 */
public class CalendarFragment extends Fragment implements CalendarListener {

    /**
     * 默认切换页面获取星期日日期（title显示）
     */
    private int weekDay = 6;

    private CalendarMainAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private CalendarListener mOuterListener;
    private SparseArray<List<CalendarInfo>> mWeekData;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mRecyclerView.requestDisallowInterceptTouchEvent(false);
                mAdapter.notifyDataSetChanged();

                CalendarInfo calendarInfo = null;
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();

                if (CalendarDateMgr.MONTH_MODE.equals(CalendarDateMgr.instance.mode)) {
                    List<CalendarInfo> calendarInfoList = CalendarDateMgr.instance.getMonthData().get(position);
                    if (calendarInfoList != null && calendarInfoList.size() > 0) {
                        calendarInfo = CalendarDateMgr.instance.getMonthData().get(position).get(weekDay);
                    }
                } else if (CalendarDateMgr.WEEK_MODE.equals(CalendarDateMgr.instance.mode)) {
                    if (mWeekData == null) {
                        mWeekData = CalendarDateMgr.instance.getWeekData();
                    }
                    if (position == mWeekData.size() - 1) {
                        List<CalendarInfo> calendarInfos = mWeekData.valueAt(position);
                        weekDay = calendarInfos.size() - 1;
                    }
                    calendarInfo = CalendarDateMgr.instance.getWeekData().get(position).get(weekDay);
                }

                if (calendarInfo != null && mOuterListener != null) {
                    mOuterListener.onPageScrolled(calendarInfo);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        initRecyclerView(mRecyclerView);
        switchToToday();
    }

    private void initRecyclerView(RecyclerView rv) {
        mAdapter = new CalendarMainAdapter(getActivity(), rv, CalendarDateMgr.instance.getWeekData(), this);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(mAdapter);
        rv.addOnScrollListener(mScrollListener);
    }

    @Override
    public void onPageScrolled(CalendarInfo info) {

    }

    @Override
    public void onItemClick(CalendarInfo info) {
        if (!TextUtils.isEmpty(info.getFullDate())) {
            switchToSelectedDay(info.getFullDate().compareTo(CalendarDateMgr.getCurrentDate()) <= 0
                    ? CalendarDateMgr.instance.getCurrentDayInfo() : info);
            mOuterListener.onItemClick(info);
        }
    }

    @Override
    public CalendarItemBaseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return mOuterListener.onCreateHolder(parent, viewType);
    }

    @Override
    public void bindItemView(CalendarItemBaseHolder holder, CalendarInfo info, CalendarItemAdapter.ItemClickListener itemClickListener) {
        mOuterListener.bindItemView(holder, info, itemClickListener);
    }

    private void switchToSelectedDay(CalendarInfo info) {
        CalendarDateMgr.instance.setCurrentSelectedDay(info);
        switchToWeekMode();
    }

    /**
     * 日历page切换监听
     */
    public void setListener(CalendarListener listener) {
        mOuterListener = listener;
    }

    /**
     * 切换到今天
     */
    public void switchToToday() {
        switchToSelectedDay(CalendarDateMgr.instance.getCurrentDayInfo());
    }

    /**
     * 切换到周历
     */
    public void switchToWeekMode() {
        if (mAdapter != null) {
            mAdapter.updateMainCalendar(CalendarDateMgr.WEEK_MODE);
        }
    }

    /**
     * 切换到月历
     */
    public void switchToMonthMode() {
        if (mAdapter != null) {
            mAdapter.updateMainCalendar(CalendarDateMgr.MONTH_MODE);
        }
    }
}
