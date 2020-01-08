package com.genesis.x.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

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
    private static final int one_day_am_start = 9;
    private static final int one_day_am_end = 12;
    /**
     * 几点下班
     */
    private static final int one_day_pm_start = 14;
    private static final int one_day_pm_end = 18;
    /**
     * 一天几个小时
     */
    private static final int one_day_hour = (one_day_am_end - one_day_am_start) + (one_day_pm_end - one_day_pm_start);

    private static final List<String> holidayList;
    private static final List<String> workdayList;

    static {
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

        workdayList = new ArrayList();
        workdayList.add("2020-01-19");
        workdayList.add("2020-02-01");
        workdayList.add("2020-04-26");
        workdayList.add("2020-05-09");
        workdayList.add("2020-06-28");
        workdayList.add("2020-09-27");
        workdayList.add("2020-10-10");
    }

    /**
     * 判断是否是holiday
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean isHoliday(Date date) {
        Assert.notNull(date, "isHoliday，日期不能为空");
        Assert.notEmpty(holidayList, "holidayList不能为空");
        String format = format(date);
        for (String o : holidayList) {
            if(format.equals(o)){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否工作日
     * @param date
     * @return
     */
    public static boolean isWorkday1(Date date){
        Assert.notNull(date, "判断是否工作日，日期不能为空");
        Assert.notEmpty(workdayList, "workdayList不能为空");
        String format = format(date);
        for (String o : workdayList) {
            if(format.equals(o)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是weekend
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }

    }

    private static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
    private static String format(Date date) {
        return format(date, PATTERN);
    }
    private static Date parse(String date) throws ParseException {
        return DateUtils.parseDate(date, PATTERN);
    }

    /**
     * 是否工作日
     * @param date
     * @return
     */
    public static boolean isWorkday(Date date) {
        boolean workday1 = isWorkday1(date);
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
    public static Date getTomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 计算工作日
     * @param num 根据需要设置,这个值就是业务需要的n个工作日
     * @return
     * @throws ParseException
     */
    public static Date getWorkdayDate(int num) {
        Assert.isTrue(num >= 1 , "getWorkdayDate.num 必须大于0");
        Date today = Calendar.getInstance().getTime();
        Date tomorrow = null;
        int delay = 1;
        while(delay <= num){
            tomorrow = getTomorrow(today);
            //当前日期+1即tomorrow,判断是否是节假日,同时要判断是否是周末,都不是则将scheduleActiveDate日期+1,直到循环num次即可
            if(isWorkday(tomorrow)){
                delay++;
            }
            today = tomorrow;
        }
        return today;
    }

    public static Date getWorkdayTime(int day, int hour) {
        // 一天按8小时算,配置的hour大于，day就+1天
        int day1 = hour / one_day_hour;
        int hour2 = hour % one_day_hour;
        day = day + day1;
        Calendar instance = Calendar.getInstance();

        day = day(instance, day, hour2);
        hour = hour(instance, hour2);

        Date workdayDate = instance.getTime();
        if(day > 0){
            workdayDate = getWorkdayDate(day);
        }
        instance.setTime(workdayDate);
        instance.set(Calendar.HOUR_OF_DAY, hour);
        if(hour >= one_day_pm_end){
            instance.set(Calendar.SECOND, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.MILLISECOND, 0);
        }
        return instance.getTime();
    }

    private static int hour(Calendar instance, int hour){
        int hour1 = instance.get(Calendar.HOUR_OF_DAY) + hour;
        if(hour1 > one_day_pm_end){
            hour = hour1 - one_day_pm_end + one_day_am_start;
            if(hour > one_day_am_end){
                hour = hour + (one_day_pm_start - one_day_am_end);
            }
        } else if(hour1 == one_day_pm_end){
            hour = one_day_pm_end;
        } else if(hour1 < one_day_pm_end){
            hour = hour1;
        }
        return hour;
    }

    private static int day(Calendar instance,int day, int hour){
        int hour1 = instance.get(Calendar.HOUR_OF_DAY) + hour;
        if(hour1 > one_day_pm_end){
            day = hour1 > one_day_pm_end ? day + 1 : day;
        }
        return day;
    }


}