package com.genesis.x.crawler.xueqiu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genesis.x.dto.xueqiu.HotDto;
import com.genesis.x.repository.HotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2019/12/26 9:27
 * @Description:
 */
@Component
public class Hot implements PageProcessor, Pipeline {

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(60000);
        site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        site.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        site.addHeader("Accept-Encoding","gzip, deflate, br");
        site.addHeader("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        site.addHeader("Cache-Control","no-cache");
        site.addHeader("Connection","keep-alive");
        site.addHeader("Host","xueqiu.com");
        site.addHeader("Origin","https://xueqiu.com");
        site.addHeader("Pragma","no-cache");
        site.addHeader("Referer","https://xueqiu.com/hq");
        site.addHeader("Sec-Fetch-Mode","navigate");
        site.addHeader("Sec-Fetch-Site","none");
        site.addHeader("Sec-Fetch-User","?1");
        site.addHeader("Upgrade-Insecure-Requests","1");
        site.addHeader("X-Requested-With","XMLHttpRequest");
        site.addHeader("Cookie","aliyungf_tc=AQAAAAvljgisygUAotFfcEn3pZr5ey8X; acw_tc=2760822115772640663867530e815b3f8b94888cc5477a0f8a099d3fa825cb; xq_a_token=c9d3b00a3bd89b210c0024ce7a2e049f437d4df3; xq_r_token=8712d4cae3deaa2f3a3d130127db7a20adc86fb2; u=661577264069642; device_id=24700f9f1986800ab4fcc880530dd0ed; s=ch11yqtkg5; __utmc=1; __utmz=1.1577264074.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); Hm_lvt_1db88642e346389874251b5a1eded6e3=1575440320,1577264070,1577264492; __utma=1.1749056857.1577264074.1577264074.1577323555.2; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1577324956; __utmb=1.3.10.1577323555");
        return site;
    }

    @Autowired
    private HotRepository hotRepository;

    private static final String hua = "https://xueqiu.com/service/v5/stock/screener/quote/list?page=1&size=90&order=desc&order_by=percent&exchange=CN&market=CN&type=sha&_=";
    private static final String sza = "https://xueqiu.com/service/v5/stock/screener/quote/list?page=1&size=90&order=desc&order_by=percent&exchange=CN&market=CN&type=sza&_=";
    private static final String zxb = "https://xueqiu.com/service/v5/stock/screener/quote/list?page=1&size=90&order=desc&order_by=percent&exchange=CN&market=CN&type=zxb&_=";

    private final Spider spider = Spider.create(this).addPipeline(this).thread(1);
    public void start(){
        spider.addUrl(hua + System.currentTimeMillis()).start();
    }
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<HotDto> data = (List<HotDto>) resultItems.get("data");
        if(!CollectionUtils.isEmpty(data)){
            data = data.stream().map(x ->{
                x.setCreateDate(new Date());
                x.setType("沪A20200103");
                return x;
            }).collect(Collectors.toList());
            hotRepository.saveAll(data);
            hotRepository.flush();
        }
    }

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText, JSONObject.class);
        List<HotDto> hotDtos = JSON.parseArray(jsonObject.getJSONObject("data").getString("list"), HotDto.class);
        if(!CollectionUtils.isEmpty(hotDtos)){
            page.putField("data", hotDtos);
        }
    }
}