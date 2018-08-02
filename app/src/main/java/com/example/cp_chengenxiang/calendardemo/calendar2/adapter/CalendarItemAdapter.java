package com.example.cp_chengenxiang.calendardemo.calendar2.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.cp_chengenxiang.calendardemo.calendar2.CalendarDateMgr;
import com.example.cp_chengenxiang.calendardemo.calendar2.model.CalendarInfo;
import com.example.cp_chengenxiang.calendardemo.calendar2.holder.CalendarItemBaseHolder;
import com.example.cp_chengenxiang.calendardemo.calendar2.listener.CalendarListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:      cgx
 * Date:        2018/7/18
 * Describe:    日历adapter
 */
public class CalendarItemAdapter extends RecyclerView.Adapter<CalendarItemBaseHolder> {

    private CalendarListener mInnerListener;
    private List<CalendarInfo> mInfo = new ArrayList<>();

    public CalendarItemAdapter(CalendarListener listener) {
        mInnerListener = listener;
    }

    @Override
    public CalendarItemBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mInnerListener.onCreateHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CalendarItemBaseHolder holder, int position) {
        CalendarInfo info = mInfo.get(position);
        mInnerListener.bindItemView(holder, info, new ItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mInfo == null ? 0 : mInfo.size();
    }

    public class ItemClickListener implements View.OnClickListener {
        private int position;

        public ItemClickListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            if (mInfo != null) {
                CalendarInfo calendarInfo = mInfo.get(position);
                if (!TextUtils.isEmpty(calendarInfo.getFullDate())) {
                    CalendarDateMgr.instance.setCurrentSelectedDay(calendarInfo);
                    if (mInnerListener != null) {
                        mInnerListener.onItemClick(calendarInfo);
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 更新内层RecyclerView
     */
    public void updateCalendar(List<CalendarInfo> infoList) {
        mInfo.clear();
        mInfo.addAll(infoList);
        notifyDataSetChanged();
    }
}
