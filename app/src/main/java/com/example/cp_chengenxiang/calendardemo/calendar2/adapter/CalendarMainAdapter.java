package com.example.cp_chengenxiang.calendardemo.calendar2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cp_chengenxiang.calendardemo.R;
import com.example.cp_chengenxiang.calendardemo.calendar2.CalendarDateMgr;
import com.example.cp_chengenxiang.calendardemo.calendar2.model.CalendarInfo;
import com.example.cp_chengenxiang.calendardemo.calendar2.listener.CalendarListener;

import java.util.List;

/**
 * Author:      cgx
 * Date:        2018/7/19
 * Describe:    周历月历 page adapter
 */
public class CalendarMainAdapter extends RecyclerView.Adapter<CalendarMainAdapter.GalleryViewHolder> {

    private Context mContext;
    private RecyclerView mOutRv;
    private CalendarListener mInnerListener;
    private SparseArray<List<CalendarInfo>> mDataList;

    public CalendarMainAdapter(Context ctx, RecyclerView rv, SparseArray<List<CalendarInfo>> dataList, CalendarListener listener) {
        mOutRv = rv;
        mContext = ctx;
        mDataList = dataList;
        mInnerListener = listener;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.calendar_layout, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if (holder.innerRv.getAdapter() == null) {
            holder.innerRv.setLayoutManager(new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL));
            holder.innerRv.setAdapter(new CalendarItemAdapter(mInnerListener));
        }
        ((CalendarItemAdapter) holder.innerRv.getAdapter()).updateCalendar(mDataList.get(position));
    }

    /**
     * 更新外层RecyclerView的数据
     *
     * @param mode
     */
    public void updateMainCalendar(String mode) {
        CalendarDateMgr.instance.mode = mode;
        if (mode.equals(CalendarDateMgr.WEEK_MODE)) {
            mDataList = CalendarDateMgr.instance.getWeekData();
            notifyDataSetChanged();
            mOutRv.scrollToPosition(CalendarDateMgr.instance.getCurrentSelectedDayInfo().getWeekIndex());

        } else if (mode.equals(CalendarDateMgr.MONTH_MODE)) {
            mDataList = CalendarDateMgr.instance.getMonthData();
            notifyDataSetChanged();
            mOutRv.scrollToPosition(CalendarDateMgr.instance.getCurrentSelectedDayInfo().getMonthIndex());
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView innerRv;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            innerRv = (RecyclerView) itemView.findViewById(R.id.rv_inner);
        }
    }
}
