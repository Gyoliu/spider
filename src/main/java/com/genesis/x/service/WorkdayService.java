//package com.genesis.x.service;
//
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.PostConstruct;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
///**
// * @Author: liuxing
// * @Date: 2020/1/7 14:49
// * @Description:
// */
//@Scope("singleton")
//@Component
//public class WorkdayService {
//
//    private static final String PATTERN = "yyyy-MM-dd";
//
//    @Autowired
//    private WorkdayConfig workdayConfig;
//
//    /**
//     * 每天从几点开始上班
//     */
//    private int one_day_am_start = 9;
//    private int one_day_am_end = 12;
//    /**
//     * 几点下班
//     */
//    private int one_day_pm_start = 14;
//    private int one_day_pm_end = 18;
//
//    private int one_day_hour = 8;
//    private List<String> holidayList;
//    private List<String> workdayList;
//
//    @PostConstruct
//    private void init(){
//        one_day_am_start = workdayConfig.getOneDayAmStart();
//        one_day_am_end = workdayConfig.getOneDayAmEnd();
//        one_day_pm_start = workdayConfig.getOneDayPmStart();
//        one_day_pm_end = workdayConfig.getOneDayPmEnd();
//
//        if(workdayConfig.isNoonBreakIsWorktime()){
//            one_day_hour = one_day_pm_end - one_day_am_start;
//        } else {
//            one_day_hour = (one_day_am_end - one_day_am_start) + (one_day_pm_end - one_day_pm_start);
//        }
//
//        if(CollectionUtils.isEmpty(holidayList)){
//            holidayList = new ArrayList();
//            holidayList.add("2020-01-01");
//            holidayList.add("2020-01-24");
//            holidayList.add("2020-01-25");
//            holidayList.add("2020-01-26");
//            holidayList.add("2020-01-27");
//            holidayList.add("2020-01-28");
//            holidayList.add("2020-01-29");
//            holidayList.add("2020-01-30");
//            holidayList.add("2020-04-04");
//            holidayList.add("2020-04-05");
//            holidayList.add("2020-04-06");
//            holidayList.add("2020-05-01");
//            holidayList.add("2020-05-02");
//            holidayList.add("2020-05-03");
//            holidayList.add("2020-05-04");
//            holidayList.add("2020-05-05");
//            holidayList.add("2020-06-25");
//            holidayList.add("2020-06-26");
//            holidayList.add("2020-06-27");
//            holidayList.add("2020-10-01");
//            holidayList.add("2020-10-02");
//            holidayList.add("2020-10-03");
//            holidayList.add("2020-10-04");
//            holidayList.add("2020-10-05");
//            holidayList.add("2020-10-06");
//            holidayList.add("2020-10-07");
//            holidayList.add("2020-10-08");
//        }
//        if(CollectionUtils.isEmpty(workdayList)){
//            workdayList = new ArrayList();
//            workdayList.add("2020-01-19");
//            workdayList.add("2020-02-01");
//            workdayList.add("2020-04-26");
//            workdayList.add("2020-05-09");
//            workdayList.add("2020-06-28");
//            workdayList.add("2020-09-27");
//            workdayList.add("2020-10-10");
//        }
//    }
//
//
//    /**
//     * 判断是否是holiday
//     * @param date
//     * @return
//     * @throws ParseException
//     */
//    public boolean isHoliday(Date date) {
//        Assert.notNull(date, "isHoliday，日期不能为空");
//        Assert.notEmpty(holidayList, "holidayList不能为空");
//        String format = format(date);
//        return holidayList.contains(format);
//    }
//
//    /**
//     * 是否工作日
//     * @param date
//     * @return
//     */
//    public boolean isContainsWorkdayList(Date date){
//        Assert.notNull(date, "判断是否工作日，日期不能为空");
//        Assert.notEmpty(workdayList, "workdayList不能为空");
//        String format = format(date);
//        return workdayList.contains(format);
//    }
//
//    /**
//     * 判断是否是weekend
//     *
//     * @param date
//     * @return
//     * @throws ParseException
//     */
//    public boolean isWeekend(Date date) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
//            return true;
//        } else{
//            return false;
//        }
//
//    }
//
//    /**
//     * 是否工作日
//     * @param date
//     * @return
//     */
//    public boolean isWorkday(Date date) {
//        boolean workday1 = isContainsWorkdayList(date);
//        if(workday1){
//            return true;
//        }
//        boolean weekend = isWeekend(date);
//        if(weekend){
//            return false;
//        }
//        boolean holiday = isHoliday(date);
//        if(holiday){
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 获取明天的日期
//     *
//     * @param date
//     * @return
//     */
//    private Date getTomorrow(Date date){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DAY_OF_MONTH, +1);
//        date = calendar.getTime();
//        return date;
//    }
//
//    /**
//     * 计算工作日
//     * @param num 根据需要设置,这个值就是业务需要的n个工作日
//     * @return
//     * @throws ParseException
//     */
//    public Date getWorkdayDate(int num) {
//        Assert.isTrue(num >= 1 , "getWorkdayDate.num 必须大于0");
//        Date today = Calendar.getInstance().getTime();
//        return getWorkdayDate(today, num);
//    }
//
//    public Date getWorkdayDate(Date today, int num) {
//        Assert.isTrue(num >= 1 , "getWorkdayDate.num 必须大于0");
//        Assert.isTrue(today != null , "today 不能为空");
//        Date tomorrow = null;
//        int delay = 1;
//        while(delay <= num){
//            tomorrow = getTomorrow(today);
//            if(isWorkday(tomorrow)){
//                delay++;
//            }
//            today = tomorrow;
//        }
//        return today;
//    }
//
//    public Date getWorkdayTime(int day, int hour) {
//        Assert.isTrue(day > -1 , "day 必须>=0");
//        Assert.isTrue(hour > -1 , "hour 必须>=0");
//        Calendar instance = Calendar.getInstance();
//        return getWorkdayTime(instance.getTime(), day, hour);
//    }
//
//    public Date getWorkdayTime(Date date, int day, int hour) {
//        Assert.isTrue(date != null , "date != null");
//        Assert.isTrue(day > -1 , "day 必须>=0");
//        Assert.isTrue(hour > -1 , "hour 必须>=0");
//        // 一天按8小时算,配置的hour大于，day就+1天
//        int day1 = hour / one_day_hour;
//        int hour2 = hour % one_day_hour;
//        day = day + day1;
//        Calendar instance = Calendar.getInstance();
//        instance.setTime(date);
//
//        day = day(instance, day, hour2);
//        hour = hour(instance, hour2);
//
//        Date workdayDate = instance.getTime();
//        if(day > 0){
//            workdayDate = getWorkdayDate(workdayDate, day);
//        }
//        instance.setTime(workdayDate);
//        instance.set(Calendar.HOUR_OF_DAY, hour);
//        if(hour >= one_day_pm_end || (!workdayConfig.isNoonBreakIsWorktime() && hour >= one_day_am_end && hour <= one_day_pm_start)){
//            instance.set(Calendar.SECOND, 0);
//            instance.set(Calendar.MINUTE, 0);
//            instance.set(Calendar.MILLISECOND, 0);
//        }
//        return instance.getTime();
//    }
//
//    /**
//     *
//     * @param instance
//     * @param hour 这个时间肯定是 < one_day_hour
//     * @return
//     */
//    private int hour(Calendar instance, int hour){
//        int hour1 = instance.get(Calendar.HOUR_OF_DAY) + hour;
//        if(hour1 > one_day_pm_end){
//            hour = hour + one_day_am_start;
//            if(hour > one_day_am_end && !workdayConfig.isNoonBreakIsWorktime()){
//                // 大于12点就 加上午休时间
//                hour = hour + (one_day_pm_start - one_day_am_end);
//            }
//        } else if(hour1 == one_day_pm_end){
//            hour = one_day_pm_end;
//        } else if(hour1 < one_day_pm_end){
//            hour = hour1;
//        }
//        return hour;
//    }
//
//    private int day(Calendar instance,int day, int hour){
//        int hour1 = instance.get(Calendar.HOUR_OF_DAY) + hour;
//        if(hour1 > one_day_pm_end){
//            day = hour1 > one_day_pm_end ? day + 1 : day;
//        }
//        return day;
//    }
//
//    private String format(Date date, String pattern) {
//        return DateFormatUtils.format(date, pattern);
//    }
//    private String format(Date date) {
//        return format(date, PATTERN);
//    }
//
//}