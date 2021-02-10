package com.genesis.x.crawler.xueqiu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.genesis.x.dto.GaiNianShareDto;
import com.genesis.x.dto.xueqiu.XueqiuASDto;
import com.genesis.x.repository.XueqiuASDtoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: liuxing
 * @Date: 2019/9/6 14:15
 * @Description: 沪深一览
 */
@Slf4j
@Component
public class AsYiLan implements PageProcessor, Pipeline {

    private static final String ROOT_URL = "https://xueqiu.com/service/v5/stock/screener/quote/list?page=%s&size=90&order=desc&orderby=percent&order_by=current_year_percent&market=CN&type=sh_sz&_=%s";

    private final Spider spider = Spider.create(this).addPipeline(this).thread(1);

    public void start(){
        String url = ROOT_URL;
        url = String.format(url, 1, System.currentTimeMillis());
        System.out.println("抓取url:" + url);
        spider.addUrl(url);
        spider.start();
    }

    @Autowired
    private XueqiuASDtoRepository xueqiuASDtoRepository;

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(60000);
        site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
        site.addHeader("Accept","*/*");
        site.addHeader("Accept-Encoding","gzip, deflate, br");
        site.addHeader("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        site.addHeader("Cache-Control","no-cache");
        site.addHeader("Connection","keep-alive");
        site.addHeader("Host","xueqiu.com");
        site.addHeader("Pragma","no-cache");
        site.addHeader("Referer","https://xueqiu.com/hq");
        site.addHeader("Sec-Fetch-Mode","cors");
        site.addHeader("Sec-Fetch-Site","same-origin");
        site.addHeader("X-Requested-With","XMLHttpRequest");
        site.addHeader("Cookie","acw_tc=2760829515706759934348386ec5071ea39c1361ba414e2a84b7ebda89004d; device_id=24700f9f1986800ab4fcc880530dd0ed; s=bu139ott3z; __utma=1.1730295619.1570676040.1570676040.1570676040.1; __utmz=1.1570676040.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); xq_a_token=87993a504d5d350e6271c337ad8e9ec8809acb79; xqat=87993a504d5d350e6271c337ad8e9ec8809acb79; xq_r_token=2b9912fb63f07c0f11e94985018ad64e78cca498; u=371572599918204; Hm_lvt_1db88642e346389874251b5a1eded6e3=1570675998,1572599919; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1572599928");
        return site;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<XueqiuASDto> data = (List<XueqiuASDto>)resultItems.get("data");
        log.info("data size : {}", data.size());
        if(!CollectionUtils.isEmpty(data)){
            xueqiuASDtoRepository.saveAll(data);
        }
    }

    private AtomicInteger pageNum = new AtomicInteger(43);

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText, JSONObject.class);
        String list = jsonObject.getJSONObject("data").get("list").toString();
        List<XueqiuASDto> xueqiuASDtos = JSON.parseArray(list, XueqiuASDto.class);
        page.putField("data", xueqiuASDtos);
        if(pageNum.get() > 1){
            String url = String.format(ROOT_URL, pageNum, System.currentTimeMillis());
            page.addTargetRequest(url);
            pageNum.getAndDecrement();
            System.out.println("分页:" + url);
            try {
                int sleep = (new Random().nextInt(60) + 1) * 100;
                System.out.println("睡眠时间：" + sleep);
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

            }
        }
    }

}