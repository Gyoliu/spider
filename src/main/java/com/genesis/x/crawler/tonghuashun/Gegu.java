package com.genesis.x.crawler.tonghuashun;

import com.genesis.x.dto.GaiNianShareDto;
import com.genesis.x.dto.GeguShareDto;
import com.genesis.x.repository.GaiNianShareRepository;
import com.genesis.x.repository.GeguShareRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2019/9/6 14:15
 * @Description: 同花顺 个股
 */
@Slf4j
@Component
public class Gegu implements PageProcessor, Pipeline {

    private final Spider spider = Spider.create(this).addPipeline(this).thread(1);

    @Autowired
    private GeguShareRepository geguShareRepository;

    @Autowired
    private GaiNianShareRepository gaiNianShareRepository;

    public void start(){
        List<GaiNianShareDto> all = gaiNianShareRepository.findAll();
        all.stream().map(x -> x.getUrl()).forEach(x -> spider.addUrl(x));
        spider.start();
    }



    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(60000);
        return site;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<GeguShareDto> data = (List<GeguShareDto>)resultItems.get("data");
        if(!CollectionUtils.isEmpty(data)){
            log.info("data size : {}", data.size());
            geguShareRepository.saveAll(data);
        }
    }

    @Override
    public void process(Page page) {
        Elements cate_items = page.getHtml().getDocument().body().getElementsByClass("tr");
        if(cate_items.size() > 0){
            ArrayList<GeguShareDto> geguShareDtos = new ArrayList<>();
            cate_items.forEach(x -> {
                GeguShareDto geguShareDto = new GeguShareDto();
                String code = x.getElementsByTag("td").get(1).text();
                geguShareDto.setCode(code);
                String name = x.getElementsByTag("td").get(2).text();
                geguShareDto.setName(name);
                String price = x.getElementsByTag("td").get(3).text();
                geguShareDto.setPrice(Double.parseDouble(price));
                String upOrDownRate = x.getElementsByTag("td").get(4).text();
                geguShareDto.setUpOrDownRate(Double.parseDouble(upOrDownRate));
                String upOrDownPrice = x.getElementsByTag("td").get(5).text();
                geguShareDto.setUpOrDownPrice(Double.parseDouble(upOrDownPrice));
                String upOrDownVelocity = x.getElementsByTag("td").get(6).text();
                geguShareDto.setUpOrDownVelocity(Double.parseDouble(upOrDownVelocity));
                String changeHands = x.getElementsByTag("td").get(7).text();
                geguShareDto.setChangeHands(Double.parseDouble(changeHands));
                geguShareDto.setCreateDate(new Date());
                geguShareDto.setRid(page.getUrl().get());
                geguShareDtos.add(geguShareDto);
            });
            page.putField("data", geguShareDtos);
        }

    }

}