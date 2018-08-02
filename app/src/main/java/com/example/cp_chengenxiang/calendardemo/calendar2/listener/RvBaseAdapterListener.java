package com.example.cp_chengenxiang.calendardemo.calendar2.listener;

import android.view.ViewGroup;

import com.example.cp_chengenxiang.calendardemo.calendar2.holder.RvBaseViewHolder;

/**
 * Author:      cgx
 * Date:        2018/7/18
 */

public interface RvBaseAdapterListener {
    RvBaseViewHolder onCreateViewHolder(ViewGroup parent, int position);

    void bindView(RvBaseViewHolder viewHolder, int position);
}
