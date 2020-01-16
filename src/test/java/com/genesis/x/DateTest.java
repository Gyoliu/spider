package com.genesis.x;

import com.genesis.x.crawler.xueqiu.SymbolMonth;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpHost;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2019/1/7 11:06
 * @Description:
 */
public class DateTest {

    private static final String PATTERN = "yyyy-MM-dd";

    @Test
    public void Test02() throws MalformedURLException {
        HttpHost proxy = new HttpHost("proxy.example.com");
        URL url = new URL("https://openai.weixin.qq.com/openapi/nlp/sensitive");
        System.out.println(url.getHost());
        System.out.println(proxy.getHostName());

    }

    @Test
    public void test01() throws ParseException {

        //查询数据库中holiday,遍历存入list（表中每条记录存放的是假期的起止日期,遍历每条结果,并将其中的每一天都存入holiday的list中），以下为模拟假期
        List holidayList = new ArrayList();
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

        List workdayList = new ArrayList();
        workdayList.add("2020-01-19");
        workdayList.add("2020-02-01");
        workdayList.add("2020-04-26");
        workdayList.add("2020-05-09");
        workdayList.add("2020-06-28");
        workdayList.add("2020-09-27");
        workdayList.add("2020-10-10");

        //获取计划激活日期
        Date scheduleActiveDate = DateTest.getScheduleActiveDate(holidayList, workdayList);
        System.out.println("10个工作日后,即计划激活日期为::" + format(scheduleActiveDate, PATTERN));
        System.out.println(format(Calendar.getInstance().getTime()));
    }

    //获取计划激活日期
    public static Date getScheduleActiveDate(List<String> list, List workdayList) throws ParseException {
//        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());//获取当前日期2018-08-26
        Date today = new Date();//Mon Aug 27 00:09:29 CST 2018
        Date tomorrow = null;
        int delay = 1;
        int num = 13;//根据需要设置,这个值就是业务需要的n个工作日
        while(delay <= num){
            tomorrow = getTomorrow(today);
            if(isWorkday(format(tomorrow), workdayList)){
                delay++;
                today = tomorrow;
            }
            //当前日期+1即tomorrow,判断是否是节假日,同时要判断是否是周末,都不是则将scheduleActiveDate日期+1,直到循环num次即可
            else if(!isWeekend(format(tomorrow)) && !isHoliday(format(tomorrow),list)){
                delay++;
                today = tomorrow;
            } else if (isWeekend(format(tomorrow))){
//                tomorrow = getTomorrow(tomorrow);
                today = tomorrow;
                System.out.println(format(tomorrow) + "::是周末");
            } else if (isHoliday(format(tomorrow),list)){
//                tomorrow = getTomorrow(tomorrow);
                today = tomorrow;
                System.out.println(format(tomorrow) + "::是节假日");
            }
        }
        System.out.println("10个工作日后,即计划激活日期为::" + format(today));
        return today;
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

    private static boolean isWorkday(String date, List workdayList) {
        if(workdayList.size() > 0){
            for(int i = 0; i < workdayList.size(); i++){
                if(date.equals(workdayList.get(i))){
                    return true;
                }
            }
        }
        return false;
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
     * 判断是否是weekend
     *
     * @param sdate
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(String sdate) throws ParseException {
        Date date = parse(sdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }

    }

    /**
     * 判断是否是holiday
     *
     * @param sdate
     * @param list
     * @return
     * @throws ParseException
     */
    public static boolean isHoliday(String sdate, List<String> list) throws ParseException {
        if(list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                if(sdate.equals(list.get(i))){
                    return true;
                }
            }
        }
        return false;
    }


}