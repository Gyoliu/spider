package com.genesis.x.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @Author: liuxing
 * @Date: 2020/1/7 14:46
 * @Description:
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "workday")
public class WorkdayConfig {

    private Double oneDayAmStart = 9d;
    private Double oneDayAmEnd = 12d;
    private Double oneDayPmStart = 14d;
    private Double oneDayPmEnd = 18d;
    /**
     * 偏移量，只在 noonBreakIsWorktime = true 下有效
     */
    private Integer offset = 0;
    /**
     * 午间休息时间是否工作时间
     */
    private Boolean noonBreakIsWorktime = true;
    /**
     * 周末是否是假日
     */
    private Boolean weekendIsHoliday = true;

    /**
     * mainland 内地模式
     * hk 香港模式
     */
    private String mode = "mainland";
    private Map<String, List<String>> holidays;
    private Map<String, List<String>> workdays;

    public Double getOneDayAmStart() {
        return oneDayAmStart;
    }

    public void setOneDayAmStart(Double oneDayAmStart) {
        this.oneDayAmStart = oneDayAmStart;
    }

    public Double getOneDayAmEnd() {
        return oneDayAmEnd;
    }

    public void setOneDayAmEnd(Double oneDayAmEnd) {
        this.oneDayAmEnd = oneDayAmEnd;
    }

    public Double getOneDayPmStart() {
        return oneDayPmStart;
    }

    public void setOneDayPmStart(Double oneDayPmStart) {
        this.oneDayPmStart = oneDayPmStart;
    }

    public Double getOneDayPmEnd() {
        return oneDayPmEnd;
    }

    public void setOneDayPmEnd(Double oneDayPmEnd) {
        this.oneDayPmEnd = oneDayPmEnd;
    }

    public Map<String, List<String>> getHolidays() {
        return holidays;
    }

    public void setHolidays(Map<String, List<String>> holidays) {
        this.holidays = holidays;
    }

    public Map<String, List<String>> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(Map<String, List<String>> workdays) {
        this.workdays = workdays;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Boolean getNoonBreakIsWorktime() {
        return noonBreakIsWorktime;
    }

    public void setNoonBreakIsWorktime(Boolean noonBreakIsWorktime) {
        this.noonBreakIsWorktime = noonBreakIsWorktime;
    }

    public Boolean getWeekendIsHoliday() {
        return weekendIsHoliday;
    }

    public void setWeekendIsHoliday(Boolean weekendIsHoliday) {
        this.weekendIsHoliday = weekendIsHoliday;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}