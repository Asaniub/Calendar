package com.example.cp_chengenxiang.calendardemo.calendar2.model;

/**
 * Author:      cgx
 * Date:        2018/7/18
 * Describe:    理财日历info
 */
public class CalendarInfo {
    private String year;
    private String month;
    private String day;
    private String fullDate;

    private int weekIndex;          //周历索引
    private int monthIndex;         //月历索引
    private boolean isToday;        //当前日期
    private boolean isHistory;      //历史日期

    private String[] dayEventType;    //控制事件类型

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean history) {
        isHistory = history;
    }

    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }

    public int getMonthIndex() {
        return monthIndex;
    }

    public void setMonthIndex(int monthIndex) {
        this.monthIndex = monthIndex;
    }

    public String[] getDayEventType() {
        return dayEventType;
    }

    public void setDayEventType(String[] dayEventType) {
        this.dayEventType = dayEventType;
    }
}
