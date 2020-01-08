package com.genesis.x;

import org.jsoup.Jsoup;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2018/12/29 17:56
 * @Description:
 */
public class TestSpider implements PageProcessor {
    private int i = 0;

    @Override
    public void process(Page page) {
        List<String> all = page.getHtml().$("a").all();
        page.putField("test", all);
        /*for (String url : all){
            String href = Jsoup.parse(url).body().getElementsByTag("a").attr("href");
            if(href.startsWith("http://") || href.startsWith("https://")){
                if(i < 10){
                    page.addTargetRequest(href);
                } else {
                    i++;
                }
            }
            System.out.println(href);
        }*/
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
        return site;
    }

    public static void main(String[] args) {
//        Spider.create(new TestSpider()).addUrl("https://www.baidu.com/").thread(1).run();
//        Spider.create(new TestSpider()).addUrl("http://webmagic.io/").addPipeline(new FilePipeline()).thread(1).run();

        // List<String> collec1t = Arrays.asList(StringUtils.split("111", ";")).stream().map(x -> "'" + x + "'").collect(Collectors.toList());
        List<String> collect = Arrays.asList("111".split(";")).stream().map(x -> "'" + x + "'").collect(Collectors.toList());
        System.out.println(StringUtils.split("111", ";"));
        System.out.println("111".split(";"));
    }

}