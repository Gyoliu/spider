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
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2019/1/14 14:35
 * @Description: 猪八戒任务中心
 * https://task.zbj.com/?i=3
 */
@Slf4j
@Component
public class ZbjDemandList implements PageProcessor, Pipeline {

    private static final String ROOT_URL = "https://task.zbj.com/tasklist";

    @Autowired
    private ZbjDemandRepository demandRepository;

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
        return site;
    }

    private final Spider spider = Spider.create(this).addUrl(ROOT_URL).addPipeline(this).thread(100);

    public void start(){
        spider.start();
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<ZbjDemandDto> demandDtos = (List<ZbjDemandDto>)resultItems.get("one");
        log.info("data size : {}", demandDtos.size());
        if(!CollectionUtils.isEmpty(demandDtos)){
            demandRepository.saveAll(demandDtos);
        }
    }

    @Override
    public void process(Page page) {
        Elements demand = page.getHtml().getDocument().body().getElementsByClass("demand-card");
        if(demand.size() > 0){
            ArrayList<ZbjDemandDto> zbjDemandDtos = new ArrayList<>();
            Iterator<Element> iterator = demand.iterator();
            while (iterator.hasNext()){
                ZbjDemandDto zbjDemandDto = new ZbjDemandDto();

                Element next = iterator.next();
                String title = next.getElementsByAttribute("title").attr("title");
                zbjDemandDto.setTitle(title);
                String linkDetail = "https:" + next.getElementsByClass("demand-title").first().getElementsByTag("a").attr("href");
                zbjDemandDto.setLinkDetail(linkDetail);
                String topics = next.getElementsByClass("demand-foot-tags").text();
                zbjDemandDto.setTopics(topics);
                String price = next.getElementsByClass("demand-price").text();
                zbjDemandDto.setPrice(price);
                // 发布时间
                String pubTime = next.getElementsByClass("card-pub-time").text();
                zbjDemandDto.setPublishTime(pubTime);
                // 名额
                String quota = next.getElementsByClass("card-pub-left").text();
                zbjDemandDto.setQuota(quota);
                // 详情
                String desc = next.getElementsByClass("demand-card-desc").text();
                zbjDemandDto.setDescription(desc);
                // 状态
                String status = next.getElementsByClass("demand-mode").text();
                zbjDemandDto.setItemStatus(status);
                zbjDemandDto.setStatus(1);
                zbjDemandDto.setCreateDate(new Date());
                zbjDemandDto.setPageUrl(page.getRequest().getUrl());
                zbjDemandDtos.add(zbjDemandDto);

            }
            page.putField("one", zbjDemandDtos);

            if(page.getUrl().get().equals(ROOT_URL)){
                ArrayList<String> arrayList = new ArrayList<>();
                for(int i = 2;i <= 500;i++){
                    arrayList.add("https://task.zbj.com/page" + i + ".html");
                }
                log.info("urls:{}", JSON.toJSONString(arrayList));
                page.addTargetRequests(arrayList);
            }
        }
    }
}