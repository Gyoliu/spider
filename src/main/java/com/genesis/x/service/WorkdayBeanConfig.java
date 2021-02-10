package com.genesis.x.service;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @Author: liuxing
 * @Date: 2020/1/7 14:49
 * @Description:
 */
@Configuration
@ConditionalOnProperty(prefix="workday",name = "enable", havingValue = "true")
public class WorkdayBeanConfig {

    @Bean
    @ConditionalOnBean(WorkdayConfig.class)
    @ConditionalOnMissingBean(WorkdayService.class)
    public WorkdayService workdayService(@Autowired WorkdayConfig workdayConfig){
        WorkdayService workdayService = new WorkdayService(workdayConfig);
        return workdayService;
    }

    public static class WorkdayService {

        private static final Logger log = LoggerFactory.getLogger(WorkdayService.class);

        private static final String PATTERN = "yyyy-MM-dd";
        private static final ZoneOffset ZONE8 = ZoneOffset.of("+8");

        private WorkdayConfig workdayConfig;

        private double one_day_hour;

        public WorkdayService(WorkdayConfig workdayConfig){
            this.setWorkdayConfig(workdayConfig);
        }

        public void afterPropertiesSet(){
            if(workdayConfig.getNoonBreakIsWorktime()){
                one_day_hour = workdayConfig.getOneDayPmEnd() - workdayConfig.getOneDayAmStart();
                one_day_hour += workdayConfig.getOffset();
            } else {
                one_day_hour = (workdayConfig.getOneDayAmEnd() - workdayConfig.getOneDayAmStart())
                        + (workdayConfig.getOneDayPmEnd() - workdayConfig.getOneDayPmStart());
            }

//            if(CollectionUtils.isEmpty(workdayConfig.getHolidays()) ||
//                    CollectionUtils.isEmpty(workdayConfig.getHolidays().get(workdayConfig.getMode()))){
//                List<String> holidayList = new ArrayList();
//                holidayList.add("2020-01-01");
//                holidayList.add("2020-01-24");
//                holidayList.add("2020-01-25");
//                holidayList.add("2020-01-26");
//                holidayList.add("2020-01-27");
//                holidayList.add("2020-01-28");
//                holidayList.add("2020-01-29");
//                holidayList.add("2020-01-30");
//                holidayList.add("2020-04-04");
//                holidayList.add("2020-04-05");
//                holidayList.add("2020-04-06");
//                holidayList.add("2020-05-01");
//                holidayList.add("2020-05-02");
//                holidayList.add("2020-05-03");
//                holidayList.add("2020-05-04");
//                holidayList.add("2020-05-05");
//                holidayList.add("2020-06-25");
//                holidayList.add("2020-06-26");
//                holidayList.add("2020-06-27");
//                holidayList.add("2020-10-01");
//                holidayList.add("2020-10-02");
//                holidayList.add("2020-10-03");
//                holidayList.add("2020-10-04");
//                holidayList.add("2020-10-05");
//                holidayList.add("2020-10-06");
//                holidayList.add("2020-10-07");
//                holidayList.add("2020-10-08");
//                if(workdayConfig.getHolidays() == null){
//                    HashMap<String, List<String>> map = new HashMap<String, List<String>>();
//                    map.put(workdayConfig.getMode(), holidayList);
//                    workdayConfig.setHolidays(map);
//                } else {
//                    workdayConfig.getHolidays().put(workdayConfig.getMode(), holidayList);
//                }
//            }
//            if(CollectionUtils.isEmpty(workdayConfig.getWorkdays()) ||
//                    CollectionUtils.isEmpty(workdayConfig.getWorkdays().get(workdayConfig.getMode()))){
//                List<String> workdayList = new ArrayList();
//                workdayList.add("2020-01-19");
//                workdayList.add("2020-02-01");
//                workdayList.add("2020-04-26");
//                workdayList.add("2020-05-09");
//                workdayList.add("2020-06-28");
//                workdayList.add("2020-09-27");
//                workdayList.add("2020-10-10");
//                if(workdayConfig.getWorkdays() == null){
//                    HashMap<String, List<String>> map = new HashMap<String, List<String>>();
//                    map.put(workdayConfig.getMode(), workdayList);
//                    workdayConfig.setWorkdays(map);
//                } else {
//                    workdayConfig.getWorkdays().put(workdayConfig.getMode(), workdayList);
//                }
//            }
        }

        public void setWorkdayConfig(WorkdayConfig workdayConfig) {
            if(workdayConfig == null){
                return;
            }
            this.workdayConfig = new WorkdayConfig();
            BeanUtils.copyProperties(workdayConfig, this.workdayConfig);
            this.workdayConfig.setHolidays(workdayConfig.getHolidays());
            this.workdayConfig.setWorkdays(workdayConfig.getWorkdays());
            this.afterPropertiesSet();
        }

        public void setComputingPattern(String pattern){
            if(StringUtils.isEmpty(pattern)){
                return;
            }
            WorkdayConfig defaultWorkdayConfig;
            try{
               defaultWorkdayConfig = this.getDefaultWorkdayConfig();
            }catch (Exception ex){
                log.warn("没有配置计算工作日的相关数据！");
                defaultWorkdayConfig = new WorkdayConfig();
            }
            switch (pattern){
                case "default":
                    this.setWorkdayConfig(defaultWorkdayConfig);
                    break;
                case "5 x 8":
                    this.workdayConfig.setNoonBreakIsWorktime(true);
                    this.workdayConfig.setWeekendIsHoliday(true);
                    this.workdayConfig.setOneDayAmStart(9d);
                    this.workdayConfig.setOneDayAmEnd(13.5d);
                    this.workdayConfig.setOneDayPmStart(13.5d);
                    this.workdayConfig.setOneDayPmEnd(18d);
                    this.workdayConfig.setMode(defaultWorkdayConfig.getMode());
                    this.workdayConfig.setWorkdays(defaultWorkdayConfig.getWorkdays());
                    this.workdayConfig.setHolidays(defaultWorkdayConfig.getHolidays());
                    this.workdayConfig.setOffset(-1);
                    break;
                case "5 x 15":
                    this.workdayConfig.setNoonBreakIsWorktime(true);
                    this.workdayConfig.setWeekendIsHoliday(true);
                    this.workdayConfig.setOneDayAmStart(7d);
                    this.workdayConfig.setOneDayAmEnd(13.5d);
                    this.workdayConfig.setOneDayPmStart(13.5d);
                    this.workdayConfig.setOneDayPmEnd(22d);
                    this.workdayConfig.setMode(defaultWorkdayConfig.getMode());
                    this.workdayConfig.setWorkdays(defaultWorkdayConfig.getWorkdays());
                    this.workdayConfig.setHolidays(defaultWorkdayConfig.getHolidays());
                    this.workdayConfig.setOffset(0);
                    break;
                case "7 x 15":
                    this.workdayConfig.setNoonBreakIsWorktime(true);
                    this.workdayConfig.setWeekendIsHoliday(false);
                    this.workdayConfig.setOneDayAmStart(7d);
                    this.workdayConfig.setOneDayAmEnd(13.5d);
                    this.workdayConfig.setOneDayPmStart(13.5d);
                    this.workdayConfig.setOneDayPmEnd(22d);
                    this.workdayConfig.setMode(defaultWorkdayConfig.getMode());
                    this.workdayConfig.setWorkdays(defaultWorkdayConfig.getWorkdays());
                    this.workdayConfig.setHolidays(defaultWorkdayConfig.getHolidays());
                    this.workdayConfig.setOffset(0);
                    break;
                case "7 x 24":
                    this.workdayConfig.setNoonBreakIsWorktime(true);
                    this.workdayConfig.setWeekendIsHoliday(false);
                    this.workdayConfig.setOneDayAmStart(0d);
                    this.workdayConfig.setOneDayAmEnd(12d);
                    this.workdayConfig.setOneDayPmStart(12d);
                    this.workdayConfig.setOneDayPmEnd(24d);
                    this.workdayConfig.setMode(defaultWorkdayConfig.getMode());
                    this.workdayConfig.setWorkdays(Maps.newHashMap());
                    this.workdayConfig.setHolidays(Maps.newHashMap());
                    this.workdayConfig.setOffset(0);
                    break;
            }
            this.afterPropertiesSet();
        }

        public WorkdayConfig getWorkdayConfig(){
            return workdayConfig;
        }

        public WorkdayConfig getDefaultWorkdayConfig(){
            return SpringContextHolder.getBean(WorkdayConfig.class);
        }

        public static WorkdayService getDefaultWorkdayService(){
            return new WorkdayService(SpringContextHolder.getBean(WorkdayConfig.class));
        }

        public void setMode(String mode){
            this.workdayConfig.setMode(mode);
            this.afterPropertiesSet();
        }

        public void setNoonBreakIsWorktime(boolean bo){
            this.workdayConfig.setNoonBreakIsWorktime(bo);
            this.afterPropertiesSet();
        }

        public void setWeekendIsHoliday(boolean bo){
            this.workdayConfig.setWeekendIsHoliday(bo);
            this.afterPropertiesSet();
        }

        /**
         * 判断是否是指定的holiday
         * @param date
         * @return
         * @throws ParseException
         */
        private boolean isHoliday(Date date) {
            Assert.notNull(date, "isHoliday，日期不能为空");
            if(CollectionUtils.isEmpty(workdayConfig.getHolidays())){
                return false;
            }
            List<String> holidayList = workdayConfig.getHolidays().get(workdayConfig.getMode());
            if(CollectionUtils.isEmpty(holidayList)){
                return false;
            }
            String format = format(date);
            return holidayList.contains(format);
        }

        /**
         * 是否指定的工作日
         * @param date
         * @return
         */
        private boolean isContainsWorkdayList(Date date){
            Assert.notNull(date, "判断是否工作日，日期不能为空");
            if(CollectionUtils.isEmpty(workdayConfig.getWorkdays())){
                return false;
            }
            List<String> workdayList = workdayConfig.getWorkdays().get(workdayConfig.getMode());
            if(CollectionUtils.isEmpty(workdayList)){
                return false;
            }
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
        private boolean isWeekend(Date date) {
            if(!workdayConfig.getWeekendIsHoliday()){
                // 没有周末休息
                return false;
            }
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
            boolean workday = isContainsWorkdayList(date);
            if(workday){
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

        private Date getYesterday(Date date){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
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

        /**
         * 计算整天的
         * @param today
         * @param day
         * @return
         */
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

        /**
         * 计算两个时间 中有多少个工作日 不能有分秒
         * @param start
         * @param end
         * @return
         */
        public int getWorkdayDate(Date start, Date end) {
            Assert.isTrue(start != null , "start 不能为空");
            Assert.isTrue(end != null , "end 不能为空");
            if(end.getTime() < start.getTime()){
                return 0;
            }
            String format = DateFormatUtils.format(start, PATTERN);
            String format1 = DateFormatUtils.format(end, PATTERN);
            if (format.equals(format1)) {
                return 0;
            }

            long daysDiff = ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
            int delay = 0;
            if(isWorkday(start)){
                delay++;
            }
            Date tomorrow = null;
            for (int i=0;i < daysDiff; i++){
                tomorrow = getTomorrow(start);
                if(isWorkday(tomorrow)){
                    delay++;
                }
                start = tomorrow;
            }
            return delay;
        }

        public List<String> getWorkdays(Date start, Date end) {
            ArrayList<String> days = new ArrayList<>();
            Assert.isTrue(start != null , "start 不能为空");
            Assert.isTrue(end != null , "end 不能为空");
            if(end.getTime() < start.getTime()){
                return days;
            }
            long daysDiff = ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
            Date tomorrow = null;
            for (int i=0;i <= daysDiff; i++){
                tomorrow = getTomorrow(start);
                if(isWorkday(tomorrow)){
                    days.add(DateFormatUtils.format(tomorrow, PATTERN));
                }
                start = tomorrow;
            }
            return days;
        }

        public long getWorkdayDifference(Date start, Date end) {
            Assert.isTrue(start != null , "start date 不能为空");
            Assert.isTrue(end != null , "end date 不能为空");
            if(end.getTime() <= start.getTime()){
                return 0l;
            }

            Date start1 = start;
            while (!this.isWorkday(start1) && start1.before(end)){
                // 如果开始时间是假期，推算出一个最近的工作日，这里指的是日期
                start1 = getTomorrow(start1);
            }
            if(!this.isWorkday(start1)){
                // 如果最后的开始时间还是 工作日 则返回0
                return 0;
            } else {
                if(start.compareTo(start1) != 0){
                    start1 = this.setDayHours(start1, this.workdayConfig.getOneDayAmStart()).getTime();
                }
            }

            Date end1 = end;
            while (!this.isWorkday(end1) && end1.after(start1) ){
                end1 = getYesterday(end1);
            }
            if(!this.isWorkday(end1)){
                // 如果最后的结束时间还是 工作日 则返回0
                return 0;
            } else {
                if(end.compareTo(end1) != 0){
                    end1 = this.setDayHours(end1, this.workdayConfig.getOneDayPmEnd()).getTime();
                }
            }

            // 开始时间大于结束时间
            if(start1.compareTo(end1) > 0){
                return 0;
            }
            // 如果是相同的日期 只要算 差 就行
            if(DateFormatUtils.format(start1, PATTERN).equals(DateFormatUtils.format(end1,PATTERN))){
                Calendar calendar1 = this.setDayHours(start1);
                Calendar calendar2 = this.setDayHours(end1);
                return ChronoUnit.SECONDS.between(calendar1.getTime().toInstant(), calendar2.getTime().toInstant());
            }

            long start_seconds = 0;
            // 设置开始时间
            // 判断开始时间是否在工作时间,上面是针对日期，这里是针对小时
            // this.workdayConfig.getOneDayAmStart() < start1 < this.workdayConfig.getOneDayPmEnd()
            // 保证了这个时间是在工作时间段
            Calendar calendar1 = this.setDayHours(start1);
            start1 = calendar1.getTime();
            // 设置这一天的下班时间
            Calendar calendar2 = this.setDayHours(start1, this.workdayConfig.getOneDayPmEnd());
            if(calendar1.before(calendar2)){
                // 计算开始时间这一天到下班时间 的秒数
                start_seconds = ChronoUnit.SECONDS.between(calendar1.toInstant(), calendar2.toInstant());
            }

            long end_seconds = 0;
            calendar1 = this.setDayHours(end1);
            end1 = calendar1.getTime();
            calendar2 = this.setDayHours(end1, this.workdayConfig.getOneDayAmStart());
            if(calendar2.before(calendar1)){
                end_seconds = ChronoUnit.SECONDS.between(calendar2.toInstant(), calendar1.toInstant());
            }

            calendar1.setTime(start1);
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            // 上面计算了当天的时间差 这里就 + 1天
            calendar1.add(Calendar.DAY_OF_MONTH , 1);

            // 上面已经计算了 end 这一天工作秒，那么这里设置成 0 就行
            boolean dection = isDection(end1);
            calendar2.setTime(end1);
            calendar2.set(Calendar.HOUR_OF_DAY, 0);
            calendar2.set(Calendar.MINUTE, 0);
            calendar2.set(Calendar.SECOND, 0);
            if(dection){
                // 说明指定的结束时间这天不是有效的，日期减一天
                calendar2.add(Calendar.DAY_OF_MONTH , -1);
            }

            int workdayDate = DateFormatUtils.format(calendar1.getTime(), PATTERN)
                    .equals(DateFormatUtils.format(calendar2.getTime(),PATTERN)) && isWorkday(calendar1.getTime()) ? 1 : 0;
            if(calendar1.before(calendar2)){
                // 计算两个日期还相差多少工作天
                workdayDate = this.getWorkdayDate(calendar1.getTime(), calendar2.getTime());
            }
            // 将工作天转换成秒
            long daySeconds = (long) (workdayDate * this.one_day_hour * 60 * 60);
            return start_seconds + end_seconds + daySeconds;
        }

        private boolean isDection(Date date){
            BigDecimal hours = new BigDecimal(date.getHours());
            BigDecimal minutes = new BigDecimal(date.getMinutes()).divide(new BigDecimal(60), 5, BigDecimal.ROUND_FLOOR);
            BigDecimal seconds = new BigDecimal(date.getSeconds()).divide(new BigDecimal(3600), 5, BigDecimal.ROUND_FLOOR);
            double doubleValue = hours.add(minutes).add(seconds).doubleValue();

            if(doubleValue < this.workdayConfig.getOneDayAmStart()){
                return false;
            } else if(doubleValue <= this.workdayConfig.getOneDayPmEnd()){
                return true;
            }
            return false;
        }

        private Calendar setDayHours(Date date){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            boolean minuteto0 = true;
            boolean secondsto0 = true;

            BigDecimal hours = new BigDecimal(date.getHours());
            BigDecimal minutes = new BigDecimal(date.getMinutes()).divide(new BigDecimal(60), 5, BigDecimal.ROUND_FLOOR);
            BigDecimal seconds = new BigDecimal(date.getSeconds()).divide(new BigDecimal(3600), 5, BigDecimal.ROUND_FLOOR);
            double doubleValue = hours.add(minutes).add(seconds).doubleValue();

            if(doubleValue < this.workdayConfig.getOneDayAmStart()){
                // 这个时间小于上班时间，那就设置成上班时间
                doubleValue = this.workdayConfig.getOneDayAmStart();
                minuteto0 = false;
                secondsto0 = false;
            } else if(doubleValue > this.workdayConfig.getOneDayPmEnd()){
                // 这个时间 大于下班时间，设置成下班时间
                doubleValue = this.workdayConfig.getOneDayPmEnd();
                minuteto0 = false;
                secondsto0 = false;
            }
            int i = new Double(doubleValue).intValue();
            double v = doubleValue - i;

            if(minuteto0){
                // minuteto0 = true, 说明小时 是在上下班的时间内
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, (int)(v * 60));
            }
            if(secondsto0){
                calendar.set(Calendar.SECOND, date.getSeconds());
            } else {
                calendar.set(Calendar.SECOND, 0);
            }
            return calendar;
        }

        private Calendar setDayHours(Date date, Double hours){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int i = hours.intValue();
            double v = hours - i;
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, (int)v * 60);
            calendar.set(Calendar.SECOND, 0);
            return calendar;
        }

        /**
         * 计算带时分秒的
         * @param day
         * @param hour
         * @param minute
         * @return
         */
        public Date getWorkdayTime(int day, int hour, int minute) {
            Assert.isTrue(day > -1 , "day 必须>=0");
            Assert.isTrue(hour > -1 , "hour 必须>=0");
            Assert.isTrue(minute > -1 , "minute 必须>=0");
            Calendar instance = Calendar.getInstance();
            return getWorkdayTime(instance.getTime(), day, hour, minute, 0);
        }

        public Date getWorkdayTime(Date date, int day, int hour, int minute, int second) {
            Assert.isTrue(date != null , "date != null");
            Assert.isTrue(day > -1 , "day 必须>=0");
            Assert.isTrue(hour > -1 , "hour 必须>=0");
            Assert.isTrue(minute > -1 , "minute 必须>=0");
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, 0, ZONE8);

            // 计算 秒
            boolean isMinuteNeedAdd = second >= 60;
            int second1 = second % 60;
            // 计算分钟
            int targetMinute = isMinuteNeedAdd ? minute + second / 60 : minute;
            boolean isHourNeedAdd = targetMinute >= 60;
            int minuteToSecond = (targetMinute % 60) * 60;
            // 计算 小时
            int targetHour = isHourNeedAdd ? hour + targetMinute / 60 : hour;
            boolean isDayNeedAdd = targetHour >= one_day_hour;
            int hourToSecond = (int) Math.round((targetHour % one_day_hour) * 3600);
            // 计算 天
            int day1 = isDayNeedAdd ? day + (int)(targetHour / one_day_hour) : day;

            localDateTime = localDateTime.plusSeconds(second1 + minuteToSecond + hourToSecond);

            // 如果计算好的小时 >= 下班时间 则天数需要 + 1
            Double v1 = 0d;
            if(localDateTime.getHour() >= workdayConfig.getOneDayPmEnd()){
                day1 += 1;
                v1 = localDateTime.getHour() - workdayConfig.getOneDayPmEnd() + workdayConfig.getOneDayAmStart();
            }

            Date workdayDate = new Date(localDateTime.toEpochSecond(ZONE8) * 1000);
            if(day1 > 0){
                workdayDate = getWorkdayDate(workdayDate, day1);
            }
            LocalDateTime localDateTime1 = LocalDateTime.ofEpochSecond(workdayDate.getTime() / 1000, 0, ZONE8);
            localDateTime1 = localDateTime1.withSecond(localDateTime.getSecond())
                    .withMinute(localDateTime.getMinute())
                    .withHour(v1 > 0 ? v1.intValue() : localDateTime.getHour())
                    .plusSeconds((long) ((v1 - v1.intValue()) * 60 * 60));
            if(!workdayConfig.getNoonBreakIsWorktime()){
                int hour1 = localDateTime1.getHour();
                // 是午休时间的话，统一安排到 下午的开始时间
                if(hour1 >= workdayConfig.getOneDayAmEnd() && hour1 < workdayConfig.getOneDayPmStart()){
                    int i = workdayConfig.getOneDayPmStart().intValue();
                    double v = workdayConfig.getOneDayPmStart() - i;
                    localDateTime1 = localDateTime1.withHour(i).plusSeconds((long) (v * 60 * 60));
                }
            }

            if(localDateTime1.getHour() < workdayConfig.getOneDayAmStart()){
                // 如果时间 小于 一天的开始上班时间，则统一安排在 开始上班的 时间点
                int i = workdayConfig.getOneDayAmStart().intValue();
                double v = workdayConfig.getOneDayAmStart() - i;
                localDateTime1 = localDateTime1.withHour(i).plusSeconds((long) (v * 60 * 60));
            }
            long l = localDateTime1.toInstant(ZONE8).toEpochMilli();
            return new Date(l);
        }

        private String format(Date date, String pattern) {
            return DateFormatUtils.format(date, pattern);
        }
        private String format(Date date) {
            return format(date, PATTERN);
        }

    }

    public enum WorkTimeMode{
        T58(0, "5 x 8"),
        T515(1, "5 x 15"),
        T715(2, "7 x 15"),
        T724(3, "7 x 24"),
        ;
        private int code;
        private String name;

        WorkTimeMode(int i, String s) {
            this.code = i;
            this.name = s;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static WorkTimeMode find(Object obj){
            if(ObjectUtils.isEmpty(obj)){
                return T58;
            }
            if(obj instanceof Integer){
                return Arrays.stream(WorkTimeMode.values()).filter(x -> x.code == (int)obj).findAny().orElseGet(() -> T58);
            } else if(obj instanceof String){
                return Arrays.stream(WorkTimeMode.values()).filter(x -> x.name.equalsIgnoreCase((String)obj)).findAny().orElseGet(() -> T58);
            }
            throw new RuntimeException("查找按钮类型指定了不支持的类型");
        }
    }

    public enum VacationType{
        mainland(0, "mainland"),
        hk(1, "hk"),
        ;
        private int code;
        private String name;

        VacationType(int i, String s) {
            this.code = i;
            this.name = s;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static VacationType find(Object obj){
            if(ObjectUtils.isEmpty(obj)){
                return mainland;
            }
            if(obj instanceof Integer){
                return Arrays.stream(VacationType.values()).filter(x -> x.code == (int)obj).findAny().orElseGet(() -> mainland);
            } else if(obj instanceof String){
                return Arrays.stream(VacationType.values()).filter(x -> x.name.equalsIgnoreCase((String)obj)).findAny().orElseGet(() -> mainland);
            }
            throw new RuntimeException("查找按钮类型指定了不支持的类型");
        }
    }

}