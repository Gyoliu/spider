package com.genesis.x.crawler.tonghuashun;

import com.genesis.x.dto.GaiNianShareDto;
import com.genesis.x.repository.GaiNianShareRepository;
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
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: liuxing
 * @Date: 2019/9/6 14:15
 * @Description: 同花顺 概念
 */
@Slf4j
@Component
public class GaiNian implements PageProcessor, Pipeline {

    private static final String ROOT_URL = "http://q.10jqka.com.cn/gn/";

    private final Spider spider = Spider.create(this).addUrl(ROOT_URL).addPipeline(this).thread(1);

    public void start(){
        spider.start();
    }

    @Autowired
    private GaiNianShareRepository gaiNianShareRepository;

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
        return site;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<GaiNianShareDto> data = (List<GaiNianShareDto>)resultItems.get("data");
        log.info("data size : {}", data.size());
        if(!CollectionUtils.isEmpty(data)){
            gaiNianShareRepository.saveAll(data);
        }
    }

    @Override
    public void process(Page page) {
        Elements cate_items = page.getHtml().getDocument().body().getElementsByClass("cate_items");
        if(cate_items.size() > 0){
            ArrayList<GaiNianShareDto> gaiNianShareDtos = new ArrayList<>();
            cate_items.forEach(x -> {
                Elements a = x.getElementsByTag("a");
                a.forEach(x1 -> {
                    String text = x1.text();
                    String href = x1.attr("href");
                    GaiNianShareDto gaiNianShareDto = new GaiNianShareDto();
                    gaiNianShareDto.setUrl(href);
                    gaiNianShareDto.setName(text);
                    gaiNianShareDto.setCreateDate(new Date());
                    try {
                        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
                        Page download = httpClientDownloader.download(new Request(href), new Task() {
                            @Override
                            public String getUUID() {
                                return UUID.randomUUID().toString();
                            }

                            @Override
                            public Site getSite() {
                                return Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
                            }
                        });
                        Element body = download.getHtml().getDocument().body();
                        String price = body.getElementsByClass("board-xj").text();
                        gaiNianShareDto.setPrice(Double.parseDouble(price));
                        Elements dd = body.getElementsByClass("board-infos").first().getElementsByTag("dd");
                        String today_price = dd.get(0).text();
                        gaiNianShareDto.setTodayPrice(Double.parseDouble(today_price));
                        String yesterday_price = dd.get(1).text();
                        gaiNianShareDto.setYesterdayPrice(Double.parseDouble(yesterday_price));
                        String minPrice = dd.get(2).text();
                        gaiNianShareDto.setMinPrice(Double.parseDouble(minPrice));
                        String maxPrice = dd.get(3).text();
                        gaiNianShareDto.setMaxPrice(Double.parseDouble(maxPrice));
                        String volume = dd.get(4).text();
                        gaiNianShareDto.setVolume(Double.parseDouble(volume));
                        String increaseRate = dd.get(5).text();
                        gaiNianShareDto.setIncreaseRate(Double.parseDouble(increaseRate.replace("%", "")));
                        String ranking = dd.get(6).text();
                        gaiNianShareDto.setRanking(Integer.parseInt(ranking.split("/")[0]));
                        String capitalInflows = dd.get(8).text();
                        gaiNianShareDto.setCapitalInflows(Double.parseDouble(capitalInflows));
                        String turnover = dd.get(9).text();
                        gaiNianShareDto.setTurnover(Double.parseDouble(turnover));
                    } catch (Exception ex){
                        log.error("url:{}, error:{}", href, ex);
                    }

                    gaiNianShareDtos.add(gaiNianShareDto);
                });
            });
            page.putField("data", gaiNianShareDtos);
        }

    }

}