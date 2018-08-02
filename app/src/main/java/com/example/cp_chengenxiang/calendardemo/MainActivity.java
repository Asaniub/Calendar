package com.example.cp_chengenxiang.calendardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cp_chengenxiang.calendardemo.calendar2.CalendarDateMgr;
import com.example.cp_chengenxiang.calendardemo.calendar2.view.CalendarFragment;
import com.example.cp_chengenxiang.calendardemo.calendar2.model.CalendarInfo;
import com.example.cp_chengenxiang.calendardemo.calendar2.holder.CalendarItemBaseHolder;
import com.example.cp_chengenxiang.calendardemo.calendar2.holder.MainCalendarItemHolder;
import com.example.cp_chengenxiang.calendardemo.calendar2.listener.CalendarListener;
import com.example.cp_chengenxiang.calendardemo.calendar2.adapter.CalendarItemAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int dayDarkColorRes;
    private int daySelectColorRes;
    private int todayUnSelectColorRes;
    private View mCloseView;
    private View mOpenView;
    private CalendarFragment mCalendarFragment;

    private List<String> mInfoList;

    /**
     * 日历滑动，条目点击，view监听
     */
    private CalendarListener mListener = new CalendarListener() {
        @Override
        public void onPageScrolled(CalendarInfo info) {
            setCalendarTitle(info);
        }

        @Override
        public void onItemClick(CalendarInfo info) {
            mCalendarFragment.switchToWeekMode();
            setCalendarTitle(CalendarDateMgr.instance.getCurrentSelectedDayInfo());
            Toast.makeText(getApplicationContext(), new StringBuffer()
                    .append(info.getYear())
                    .append("年")
                    .append(info.getMonth())
                    .append("月")
                    .append(info.getDay())
                    .append("月").toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public CalendarItemBaseHolder onCreateHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.calendar_item, parent, false);
            return new MainCalendarItemHolder(view);
        }

        @Override
        public void bindItemView(CalendarItemBaseHolder holder, CalendarInfo info, CalendarItemAdapter.ItemClickListener itemClickListener) {
            if (CalendarDateMgr.instance.getCurrentSelectedDayInfo() == null) {
                return;
            }
            MainCalendarItemHolder financialHolder = (MainCalendarItemHolder) holder;
            financialHolder.tv_date.setText(info.getDay());

            if (!TextUtils.isEmpty(info.getFullDate())
                    && info.getFullDate().equals(CalendarDateMgr.instance.getCurrentSelectedDayInfo().getFullDate())) {
                //日历选中
                financialHolder.iv_bg.setVisibility(View.VISIBLE);
                financialHolder.iv_bg_today.setVisibility(View.GONE);
                financialHolder.tv_date.setTextColor(daySelectColorRes);
            } else if (info.isToday()) {
                //当日
                financialHolder.iv_bg.setVisibility(View.GONE);
                financialHolder.iv_bg_today.setVisibility(View.VISIBLE);
                financialHolder.tv_date.setTextColor(todayUnSelectColorRes);
            } else {
                financialHolder.iv_bg.setVisibility(View.GONE);
                financialHolder.iv_bg_today.setVisibility(View.GONE);
                financialHolder.tv_date.setTextColor(dayDarkColorRes);
            }
            //日历事件
            if (info.getDayEventType() != null && info.getDayEventType().length > 0) {
                financialHolder.iv_event.setVisibility(View.VISIBLE);
            } else {
                financialHolder.iv_event.setVisibility(View.GONE);
            }
            financialHolder.item_view.setOnClickListener(itemClickListener);
        }
    };
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_calendar_layout);
        initResource();
        initView();
        requestCalendarData();
    }

    private void initResource() {
        daySelectColorRes = getResources().getColor(R.color.white);
        dayDarkColorRes = getResources().getColor(R.color.gray);
        todayUnSelectColorRes = getResources().getColor(R.color.blue);
    }

    private void initView() {

        mTvTitle = findViewById(R.id.tv_title);
        mCloseView = findViewById(R.id.tv_week);
        mOpenView = findViewById(R.id.tv_month);

        findViewById(R.id.tv_init).setOnClickListener(this);
        mCloseView.setOnClickListener(this);
        mOpenView.setOnClickListener(this);
    }

    private void initFragment() {
        mCalendarFragment = new CalendarFragment();
        mCalendarFragment.setListener(mListener);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_calendar_container, mCalendarFragment)
                .commit();
        switchToCurrentDate();
    }


    /**
     * 请求日历数据
     */
    private void requestCalendarData() {
        CalendarDateMgr.instance.genCalendarInfo(CalendarDateMgr.getCurrentDate(), 2);

        Map<String, String[]> eventMap = new HashMap<>();
        eventMap.put("20180730", new String[]{"1", "2"});
        eventMap.put("20180807", new String[]{"1", "2"});
        eventMap.put("20180811", new String[]{"1", "2"});
        eventMap.put("20180812", new String[]{"1", "2"});
        eventMap.put("20180813", new String[]{"1", "2"});
        eventMap.put("20180814", new String[]{"1", "2"});
        eventMap.put("20180815", new String[]{"1", "2"});
        eventMap.put("20180816", new String[]{"1", "2"});
        eventMap.put("20180817", new String[]{"1", "2"});
        eventMap.put("20180818", new String[]{"1", "2"});
        eventMap.put("20180819", new String[]{"1", "2"});
        eventMap.put("20180822", new String[]{"1", "2"});
        eventMap.put("20180823", new String[]{"1", "2"});
        eventMap.put("20180825", new String[]{"1", "2"});
        eventMap.put("20180826", new String[]{"1", "2"});
        eventMap.put("20180827", new String[]{"1", "2"});
        eventMap.put("20180829", new String[]{"1", "2"});
        eventMap.put("20180914", new String[]{"1", "2"});
        eventMap.put("20180917", new String[]{"1", "2"});
        eventMap.put("20180919", new String[]{"1", "2"});
        CalendarDateMgr.instance.addDayEvent(eventMap);

    }

    /**
     * title显示日期
     */
    private void setCalendarTitle(CalendarInfo calendarInfo) {
        if (calendarInfo != null) {
            mTvTitle.setText(new StringBuffer()
                    .append(calendarInfo.getYear())
                    .append("年")
                    .append(calendarInfo.getMonth())
                    .append("月").toString());
        }
    }

    /**
     * 切换到当日
     */
    private void switchToCurrentDate() {
        setCalendarTitle(CalendarDateMgr.instance.getCurrentDayInfo());
        if (mCalendarFragment != null) {
            mCalendarFragment.switchToToday();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_month) {
            setCalendarTitle(CalendarDateMgr.instance.getCurrentSelectedDayInfo());
            if (mCalendarFragment != null) {
                mCalendarFragment.switchToMonthMode();
            }

        } else if (id == R.id.tv_week) {
            setCalendarTitle(CalendarDateMgr.instance.getCurrentSelectedDayInfo());
            if (mCalendarFragment != null) {
                mCalendarFragment.switchToWeekMode();
            }

        } else if (id == R.id.tv_init) {
            initFragment();

        }
    }

    @Override
    public void finish() {
        CalendarDateMgr.instance.clearCalendarData();
        super.finish();
    }
}
