package com.genesis.x.crawler.zbj;

import com.alibaba.fastjson.JSON;
import com.genesis.x.dto.ZbjDemandDto;
import com.genesis.x.repository.ZbjDemandRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2019/1/14 14:35
 * @Description: 猪八戒任务中心
 * https://task.zbj.com/?i=3
 */
@Slf4j
@Component
public class ZbjDemandList implements PageProcessor, Pipeline {

    @Autowired
    private ZbjDemandRepository demandRepository;

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
        return site;
    }

    private final Spider spider = Spider.create(this).addUrl("https://task.zbj.com/?i=3").addPipeline(this).thread(1);

    public void start(){
        spider.start();
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<ZbjDemandDto> demandDtos = (List<ZbjDemandDto>)resultItems.get("one");
        log.info("data: {}", JSON.toJSONString(demandDtos));
        demandRepository.saveAll(demandDtos);
    }

    @Override
    public void process(Page page) {
        Elements demand = page.getHtml().getDocument().body().getElementsByClass("demand");
        if(demand.size() > 0){
            ArrayList<ZbjDemandDto> zbjDemandDtos = new ArrayList<>();
            Iterator<Element> iterator = demand.iterator();
            while (iterator.hasNext()){
                ZbjDemandDto zbjDemandDto = new ZbjDemandDto();

                Element next = iterator.next();
                String title = next.getElementsByClass("xq-title").text();
                zbjDemandDto.setTitle(title);
                String linkDetail = "https:" + next.getElementsByClass("link-detail").attr("href");
                zbjDemandDto.setLinkDetail(linkDetail);
                String topics = next.getElementsByClass("icons-topics").text();
                zbjDemandDto.setTopics(topics);
                String price = next.getElementsByClass("d-base-price").text();
                zbjDemandDto.setPrice(price);
                String desc = next.getElementsByClass("d-des").text();
                zbjDemandDto.setDescription(desc);
                String status = next.getElementsByClass("bid-status").text();
                zbjDemandDto.setItemStatus(status);
                zbjDemandDto.setStatus(1);
                zbjDemandDto.setCreateDate(new Date());
                zbjDemandDto.setPageUrl(page.getRequest().getUrl());
                zbjDemandDtos.add(zbjDemandDto);

            }
            page.putField("one", zbjDemandDtos);

            String curPage = page.getHtml().$("#utopia_widget_7 input[name='curPage']").get();
            if(Integer.parseInt(Jsoup.parse(curPage).body().getElementsByTag("input").attr("value")) == 1){
                String s = page.getHtml().$("#utopia_widget_7 input[name='total']").get();
                int value = Integer.parseInt(Jsoup.parse(s).body().getElementsByTag("input").attr("value"));
                ArrayList<String> arrayList = new ArrayList<>();
                for(int i=2;i<=value;i++){
                    //https://task.zbj.com/page2.html?i=3
                    arrayList.add("https://task.zbj.com/page" + i + ".html?i=3");
                }
                log.info("urls:{}", JSON.toJSONString(arrayList));
                page.addTargetRequests(arrayList);
            }
        }
    }
}