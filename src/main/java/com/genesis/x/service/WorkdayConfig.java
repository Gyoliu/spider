package com.genesis.x.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2020/1/7 14:46
 * @Description:
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "workday")
public class WorkdayConfig {

    private Integer oneDayAmStart = 9;
    private Integer oneDayAmEnd = 12;
    private Integer oneDayPmStart = 14;
    private Integer oneDayPmEnd = 18;
    private boolean noonBreakIsWorktime = false;
    private List<String> holidayList;
    private List<String> workdayList;

    public Integer getOneDayAmStart() {
        return oneDayAmStart;
    }

    public void setOneDayAmStart(Integer oneDayAmStart) {
        this.oneDayAmStart = oneDayAmStart;
    }

    public Integer getOneDayAmEnd() {
        return oneDayAmEnd;
    }

    public void setOneDayAmEnd(Integer oneDayAmEnd) {
        this.oneDayAmEnd = oneDayAmEnd;
    }

    public Integer getOneDayPmStart() {
        return oneDayPmStart;
    }

    public void setOneDayPmStart(Integer oneDayPmStart) {
        this.oneDayPmStart = oneDayPmStart;
    }

    public Integer getOneDayPmEnd() {
        return oneDayPmEnd;
    }

    public void setOneDayPmEnd(Integer oneDayPmEnd) {
        this.oneDayPmEnd = oneDayPmEnd;
    }

    public List<String> getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(List<String> holidayList) {
        this.holidayList = holidayList;
    }

    public List<String> getWorkdayList() {
        return workdayList;
    }

    public void setWorkdayList(List<String> workdayList) {
        this.workdayList = workdayList;
    }

    public boolean isNoonBreakIsWorktime() {
        return noonBreakIsWorktime;
    }

    public void setNoonBreakIsWorktime(boolean noonBreakIsWorktime) {
        this.noonBreakIsWorktime = noonBreakIsWorktime;
    }
}