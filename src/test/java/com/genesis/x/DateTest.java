package com.genesis.x;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Author: liuxing
 * @Date: 2019/1/7 11:06
 * @Description:
 */
public class DateTest {

    @Test
    public void test01(){
        String d = "sadf";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        try {
            System.out.println(simpleDateFormat.parse(d));
        } catch (ParseException e) {

        }
    }

}