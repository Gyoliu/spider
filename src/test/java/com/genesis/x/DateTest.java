package com.genesis.x;

import com.genesis.x.crawler.xueqiu.SymbolMonth;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2019/1/7 11:06
 * @Description:
 */
public class DateTest {

    private boolean isIncrease(List<Float> list, int n){
        if(n <= 2){
            return true;
        }
        return (list.get(n - 1) > list.get(n - 2) && isIncrease(list, n - 1));
    }

    @Test
    public void test01(){
        /*ArrayList<Float> floats = new ArrayList<>();
        floats.add(1f);
        floats.add(2f);
        floats.add(3f);
        floats.add(4f);
        floats.add(5f);
        floats.add(7f);
        floats.add(13f);
        boolean increase = this.isIncrease(floats, floats.size());
        System.out.println(increase);*/
        String url = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=SZ300014&begin=1575535846342&period=month&type=before&count=-142&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
        String symbol = SymbolMonth.getParam(url, "symbol");
        System.out.println(symbol);
    }

}