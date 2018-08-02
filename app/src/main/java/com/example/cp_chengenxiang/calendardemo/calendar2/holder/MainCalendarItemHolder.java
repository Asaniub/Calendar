package com.example.cp_chengenxiang.calendardemo.calendar2.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cp_chengenxiang.calendardemo.R;

/**
 * Author:      cgx
 * Date:        2018/7/20
 * Describe:    理财日历holder
 */
public class MainCalendarItemHolder extends CalendarItemBaseHolder {

    public View item_view;
    public TextView tv_date;
    public ImageView iv_bg;
    public ImageView iv_bg_today;
    public ImageView iv_event;

    public MainCalendarItemHolder(View itemView) {
        super(itemView);
        item_view = itemView;
        tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        iv_bg_today = (ImageView) itemView.findViewById(R.id.iv_bg_today);
        iv_event = (ImageView) itemView.findViewById(R.id.iv_event);
    }
}
