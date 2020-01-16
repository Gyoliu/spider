package com.genesis.x.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2019/12/31 16:41
 * @Description:
 */
public class WorkdayUtils {

    private static final String PATTERN = "yyyy-MM-dd";

    /**
     * 每天从几点开始上班
     */
    private int one_day_am_start = 9;
    private int one_day_am_end = 12;
    /**
     * 几点下班
     */
    private int one_day_pm_start = 14;
    private int one_day_pm_end = 18;
    private boolean isNoonBreakIsWorktime = true;

    private int one_day_hour = one_day_pm_end - one_day_am_start;
    private List<String> holidayList;
    private List<String> workdayList;

    private void init(){
        if(CollectionUtils.isEmpty(holidayList)){
            holidayList = new ArrayList();
            holidayList.add("2020-01-01");
            holidayList.add("2020-01-24");
            holidayList.add("2020-01-25");
            holidayList.add("2020-01-26");
            holidayList.add("2020-01-27");
            holidayList.add("2020-01-28");
            holidayList.add("2020-01-29");
            holidayList.add("2020-01-30");
            holidayList.add("2020-04-04");
            holidayList.add("2020-04-05");
            holidayList.add("2020-04-06");
            holidayList.add("2020-05-01");
            holidayList.add("2020-05-02");
            holidayList.add("2020-05-03");
            holidayList.add("2020-05-04");
            holidayList.add("2020-05-05");
            holidayList.add("2020-06-25");
            holidayList.add("2020-06-26");
            holidayList.add("2020-06-27");
            holidayList.add("2020-10-01");
            holidayList.add("2020-10-02");
            holidayList.add("2020-10-03");
            holidayList.add("2020-10-04");
            holidayList.add("2020-10-05");
            holidayList.add("2020-10-06");
            holidayList.add("2020-10-07");
            holidayList.add("2020-10-08");
        }
        if(CollectionUtils.isEmpty(workdayList)){
            workdayList = new ArrayList();
            workdayList.add("2020-01-19");
            workdayList.add("2020-02-01");
            workdayList.add("2020-04-26");
            workdayList.add("2020-05-09");
            workdayList.add("2020-06-28");
            workdayList.add("2020-09-27");
            workdayList.add("2020-10-10");
        }
    }


    /**
     * 判断是否是holiday
     * @param date
     * @return
     * @throws ParseException
     */
    public boolean isHoliday(Date date) {
        Assert.notNull(date, "isHoliday，日期不能为空");
        Assert.notEmpty(holidayList, "holidayList不能为空");
        String format = format(date);
        return holidayList.contains(format);
    }

    /**
     * 是否工作日
     * @param date
     * @return
     */
    public boolean isContainsWorkdayList(Date date){
        Assert.notNull(date, "判断是否工作日，日期不能为空");
        Assert.notEmpty(workdayList, "workdayList不能为空");
        String format = format(date);
        return workdayList.contains(format);
    }

    /**
     * 判断是否是weekend
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }

    }

    /**
     * 是否工作日
     * @param date
     * @return
     */
    public boolean isWorkday(Date date) {
        boolean workday1 = isContainsWorkdayList(date);
        if(workday1){
            return true;
        }
        boolean weekend = isWeekend(date);
        if(weekend){
            return false;
        }
        boolean holiday = isHoliday(date);
        if(holiday){
            return false;
        }
        return true;
    }

    /**
     * 获取明天的日期
     *
     * @param date
     * @return
     */
    private Date getTomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 计算工作日
     * @param day 根据需要设置,这个值就是业务需要的n个工作日
     * @return
     * @throws ParseException
     */
    public Date getWorkdayDate(int day) {
        Assert.isTrue(day >= 1 , "getWorkdayDate.day 必须大于0");
        Date today = Calendar.getInstance().getTime();
        return getWorkdayDate(today, day);
    }

    public Date getWorkdayDate(Date today, int day) {
        Assert.isTrue(day >= 1 , "getWorkdayDate.day 必须大于0");
        Assert.isTrue(today != null , "today 不能为空");
        Date tomorrow = null;
        int delay = 1;
        while(delay <= day){
            tomorrow = getTomorrow(today);
            if(isWorkday(tomorrow)){
                delay++;
            }
            today = tomorrow;
        }
        return today;
    }

    public Date getWorkdayTime(int day, int hour, int minute) {
        Assert.isTrue(day > -1 , "day 必须>=0");
        Assert.isTrue(hour > -1 , "hour 必须>=0");
        Assert.isTrue(minute > -1 , "second 必须>=0");
        Calendar instance = Calendar.getInstance();
        return getWorkdayTime(instance.getTime(), day, hour, minute);
    }

    public Date getWorkdayTime(Date date, int day, int hour, int minute) {
        Assert.isTrue(date != null , "date != null");
        Assert.isTrue(day > -1 , "day 必须>=0");
        Assert.isTrue(hour > -1 , "hour 必须>=0");
        Assert.isTrue(minute > -1 , "minute 必须>=0");
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        // 算按分钟配置了几个小时
        int mi = minute / 60;
        // 配置小时+分钟的小时
        hour = hour + mi;
        // 分钟=%60
        minute = minute % 60;

        // 一天按one_day_hour小时算,配置的hour大于，day就+1天
        int day1 = hour / one_day_hour;
        day = day + day1;
        int hour2 = hour % one_day_hour;

        day = day(instance, day, hour2);
        hour = hour(instance, hour2);

        minute = instance.get(Calendar.MINUTE) + minute;
        if(minute >= 60){
            // 如果分钟超过一小时 则小时+1
            hour = hour + 1;
            // 则设置分钟=超过了多少分钟
            minute = minute - 60;
            if(hour >= one_day_pm_end){
                // 如果小时数 >= 下班数 则天+1
                day = day + 1;
                hour = hour - one_day_pm_end + one_day_am_start;
            }
        }

        Date workdayDate = instance.getTime();
        if(day > 0){
            workdayDate = getWorkdayDate(workdayDate, day);
        }
        instance.setTime(workdayDate);
        instance.set(Calendar.HOUR_OF_DAY, hour);
        instance.set(Calendar.MINUTE, minute);
        if(hour >= one_day_pm_end || (!isNoonBreakIsWorktime && hour >= one_day_am_end && hour < one_day_pm_start)){
            instance.set(Calendar.SECOND, 0);
            instance.set(Calendar.MILLISECOND, 0);
        }
        return instance.getTime();
    }

    /**
     *
     * @param instance
     * @param hour 这个时间肯定是 < one_day_hour
     * @return
     */
    private int hour(Calendar instance, int hour){
        int hour1 = instance.get(Calendar.HOUR_OF_DAY) + hour;
        if(hour1 > one_day_pm_end){
            hour = (hour1 - one_day_pm_end) + one_day_am_start;
            if(hour > one_day_am_end && !isNoonBreakIsWorktime){
                // 大于12点就 加上午休时间
                hour = hour + (one_day_pm_start - one_day_am_end);
            }
        } else if(hour1 == one_day_pm_end){
            hour = one_day_pm_end;
        } else if(hour1 < one_day_pm_end){
            hour = hour1;
        }
        return hour;
    }

    private int day(Calendar instance,int day, int hour){
        int hour1 = instance.get(Calendar.HOUR_OF_DAY) + hour;
        if(hour1 > one_day_pm_end){
            day = hour1 > one_day_pm_end ? day + 1 : day;
        }
        return day;
    }

    private String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
    private String format(Date date) {
        return format(date, PATTERN);
    }


    public static void main(String[] args) {
        WorkdayUtils workdayUtils = new WorkdayUtils();
        workdayUtils.init();

        Date workdayTime = workdayUtils.getWorkdayTime(0, 6, 25);
        System.out.println(DateFormatUtils.format(workdayTime, "yyyy-MM-dd HH:mm:ss"));

        System.out.println(55 / 60);
    }


}