package com.example.cp_chengenxiang.calendardemo.calendar2.listener;

import android.view.ViewGroup;

import com.example.cp_chengenxiang.calendardemo.calendar2.model.CalendarInfo;
import com.example.cp_chengenxiang.calendardemo.calendar2.holder.CalendarItemBaseHolder;
import com.example.cp_chengenxiang.calendardemo.calendar2.adapter.CalendarItemAdapter;

/**
 * Author:      cgx
 * Date:        2018/7/24
 * Describe:    日历监听
 */
public interface CalendarListener {

    /**
     * 日历滑动
     */
    void onPageScrolled(CalendarInfo info);

    /**
     * 日历点击
     */
    void onItemClick(CalendarInfo info);


    /**
     * 创建日历viewHolder
     */
    CalendarItemBaseHolder onCreateHolder(ViewGroup parent, int viewType);

    /**
     * 绑定日历条目view
     */
    void bindItemView(CalendarItemBaseHolder holder, CalendarInfo info, CalendarItemAdapter.ItemClickListener itemClickListener);

}
