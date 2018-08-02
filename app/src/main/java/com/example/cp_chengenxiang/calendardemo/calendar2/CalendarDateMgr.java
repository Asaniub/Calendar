package com.example.cp_chengenxiang.calendardemo.calendar2;

import android.util.SparseArray;

import com.example.cp_chengenxiang.calendardemo.calendar2.model.CalendarInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Author:      cgx
 * Date:        2018/7/19
 * Describe:    管理日历数据
 */
public class CalendarDateMgr {

    private CalendarInfo mTodayData;
    private CalendarInfo mCurrentSelectedDay;
    private List<CalendarInfo> mWeekInfo = new ArrayList<>();               //单周历
    private List<CalendarInfo> mMonthInfo = new ArrayList<>();              //单月历
    private List<CalendarInfo> mCalendarInfo = new ArrayList<>();           //所有日历

    private SparseArray<List<CalendarInfo>> weekData = new SparseArray<>();
    private SparseArray<List<CalendarInfo>> monthData = new SparseArray<>();

    public String mode = WEEK_MODE;
    public static final String WEEK_MODE = "WEEK_MODE";
    public static final String MONTH_MODE = "MONTH_MODE";

    public static final CalendarDateMgr instance = new CalendarDateMgr();

    /**
     * 生成日历数据
     *
     * @param startDate  日历首月的任意一天
     * @param monthCount 后推月数(总月数 - 1)
     */
    public void genCalendarInfo(String startDate, int monthCount) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date beginDate;
        try {
            beginDate = format.parse(startDate);
        } catch (ParseException e) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);                    //当前时间
        int beginYear = calendar.get(Calendar.YEAR);
        int beginMonth = calendar.get(Calendar.MONTH);
        calendar.set(beginYear, beginMonth, 1);   //当月第一天
        calendar.add(Calendar.MONTH, monthCount);       //往后 monthCount 个月
        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH);

        calendar.set(beginYear, beginMonth, 1);   //当月第一天

        mWeekInfo.clear();
        mMonthInfo.clear();
        mCalendarInfo.clear();
        CalendarInfo info;
        int monthIndex = 0;
        while (calendar.get(Calendar.YEAR) < maxYear
                || (calendar.get(Calendar.YEAR) == maxYear
                && calendar.get(Calendar.MONTH) <= maxMonth)) {

            info = new CalendarInfo();
            info.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
            info.setMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            info.setDay(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            info.setMonthIndex(monthIndex);
            info.setFullDate(new StringBuffer()
                    .append(info.getYear())
                    .append(getDate(calendar.get(Calendar.MONTH) + 1))
                    .append(getDate(calendar.get(Calendar.DAY_OF_MONTH))).toString());
            info.setToday(startDate.equals(info.getFullDate()));
            info.setHistory(startDate.compareTo(info.getFullDate()) > 0);

            mMonthInfo.add(info);
            mCalendarInfo.add(info);

            //添加月历数据
            if (mMonthInfo.size() == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                List<CalendarInfo> infoList = new ArrayList<>();
                infoList.addAll(mMonthInfo);
                mMonthInfo.clear();
                monthData.put(monthIndex, infoList);
                monthIndex++;
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1); //往后一天
        }
    }

    /**
     * 添加日历条目事件
     *
     * @param eventMap <date,event>
     */
    public void addDayEvent(Map<String, String[]> eventMap) {
        if (mCalendarInfo == null) {
            return;
        }
        Iterator<String> iterator = eventMap.keySet().iterator();
        while (iterator.hasNext()) {
            String date = iterator.next();
            //周历
            for (int i = 0; i < mCalendarInfo.size(); i++) {
                if (date.equals(mCalendarInfo.get(i).getFullDate())) {
                    mCalendarInfo.get(i).setDayEventType(eventMap.get(date));
                    break;
                }
            }
            //月历
            for (int i = 0; i < monthData.size(); i++) {
                if (!date.equals(monthData.get(i).get(0).getFullDate())) {
                    continue;
                }
                List<CalendarInfo> infoList = monthData.get(i);
                for (int j = 0; j < infoList.size(); j++) {
                    if (date.equals(infoList.get(i).getFullDate())) {
                        infoList.get(i).setDayEventType(eventMap.get(date));
                        break;
                    }
                }
            }
        }

        genWeekInfo();
        genMonthInfo();
        getCurrentDayInfo();
    }

    private void genWeekInfo() {
        //首月第一周前面补空
        int weekNo = getWeekDay(mCalendarInfo.get(0).getFullDate(), "yyyyMMdd");
        int emptyDays = weekNo == 0 ? 6 : weekNo - 1;
        for (int i = 0; i < emptyDays; i++) {
            CalendarInfo info = new CalendarInfo();
            mCalendarInfo.add(0, info);
        }

        //日历按周分组，添加周索引
        for (int i = 0; i < mCalendarInfo.size(); i++) {
            CalendarInfo info = mCalendarInfo.get(i);
            info.setWeekIndex(i / 7);
            mWeekInfo.add(info);
            if ((i != 0 && (i + 1) % 7 == 0) || i == mCalendarInfo.size() - 1) {
                List<CalendarInfo> infoList = new ArrayList<>();
                infoList.addAll(mWeekInfo);
                weekData.put(info.getWeekIndex(), infoList);
                mWeekInfo.clear();
            }
        }
    }

    private void genMonthInfo() {
        for (int i = 0; i < monthData.size(); i++) {
            List<CalendarInfo> infoList = monthData.get(i);
            //第一周前面补空
            int weekNo = getWeekDay(infoList.get(0).getFullDate(), "yyyyMMdd");
            int emptyDays = weekNo == 0 ? 6 : weekNo - 1;
            for (int j = 0; j < emptyDays; j++) {
                CalendarInfo info = new CalendarInfo();
                infoList.add(0, info);
            }
        }
    }

    private String getDate(int num) {
        if (num <= 9) {
            return "0" + num;
        }
        return String.valueOf(num);
    }

    /**
     * 获取今天信息
     */
    public CalendarInfo getCurrentDayInfo() {
        if (mTodayData != null) {
            return mTodayData;
        }
        for (int i = 0; i < mCalendarInfo.size(); i++) {
            if (getCurrentDate().equals(mCalendarInfo.get(i).getFullDate())) {
                mTodayData = mCalendarInfo.get(i);
            }
        }
        if (mCurrentSelectedDay == null) {
            setCurrentSelectedDay(mTodayData);
        }
        return mTodayData;
    }

    /**
     * 获取当前选中日期信息
     */
    public CalendarInfo getCurrentSelectedDayInfo() {
        return mCurrentSelectedDay;
    }

    public void setCurrentSelectedDay(CalendarInfo currentSelectedDay) {
        mCurrentSelectedDay = currentSelectedDay;
    }

    /**
     * 获取周历数据
     */
    public SparseArray<List<CalendarInfo>> getWeekData() {
        return weekData;
    }

    /**
     * 获取月历数据
     */
    public SparseArray<List<CalendarInfo>> getMonthData() {
        return monthData;
    }

    /**
     * 初始化
     */
    public void clearCalendarData() {
        mTodayData = null;
        mCurrentSelectedDay = null;
        mWeekInfo.clear();
        mMonthInfo.clear();
        mCalendarInfo.clear();
        weekData.clear();
        monthData.clear();
    }

    public int getWeekDay(String resTime, String pattern) {
        try {
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            DateFormat format = new SimpleDateFormat(pattern);
            Date resDate = format.parse(resTime);
            Date now = new Date();
            c.setTime(resDate);
            int resWeek = c.get(Calendar.DAY_OF_WEEK);
            c.setTime(now);
            int weekday = resWeek;
            if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
                weekday = resWeek - 1;
            } else if (c.getFirstDayOfWeek() == Calendar.MONDAY && weekday == 7) {
                weekday = 0;
            }
            return weekday;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(System.currentTimeMillis());
    }
}
