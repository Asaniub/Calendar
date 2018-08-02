package com.example.cp_chengenxiang.calendardemo.calendar2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.cp_chengenxiang.calendardemo.calendar2.listener.RvAdapterListener;
import com.example.cp_chengenxiang.calendardemo.calendar2.holder.RvBaseViewHolder;

import java.util.List;

/**
 * Author:      cgx
 * Date:        2018/7/18
 * Describe:    RecyclerView适配器
 */

public class RvBaseAdapter<T> extends RecyclerView.Adapter<RvBaseViewHolder> {

    public static int NOVIEWTYPE = -1;
    public static int FOOTERVIEW = 1;

    private boolean mHasFooter;
    private List<T> mList;
    private RvAdapterListener mListener;

    public RvBaseAdapter(List<T> financialModelList, boolean hasFooter, RvAdapterListener listener) {
        mList = financialModelList;
        mHasFooter = hasFooter;
        mListener = listener;
    }

    @Override
    public RvBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mListener.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RvBaseViewHolder holder, int position) {
        mListener.bindView(holder, position);
    }

    @Override
    public int getItemCount() {
        if (mHasFooter) {
            return mList == null ? 1 : mList.size() + 1;
        }
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasFooter && position == getItemCount() - 1) {
            return FOOTERVIEW;
        }
        return NOVIEWTYPE;
    }

    public void addFooterView() {
        notifyItemInserted(getItemCount() - 1);
    }
}
