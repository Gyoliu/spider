package com.genesis.x.crawler.zbj;

import com.alibaba.fastjson.JSON;
import com.genesis.x.dto.SpiderZbjCategoryDto;
import com.genesis.x.dto.SpiderZbjServiceListDto;
import com.genesis.x.repository.SpiderZbjCategoryRepository;
import com.genesis.x.repository.SpiderZbjServiceListRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2019/1/10 15:34
 * @Description: 猪八戒的2级菜单 点进去的列表
 */
@Slf4j
@Component
public class ZbjServiceList implements PageProcessor, Pipeline {

    @Autowired
    private SpiderZbjCategoryRepository spiderZbjCategoryRepository;

    @Autowired
    private SpiderZbjServiceListRepository serviceListRepository;

    public List<SpiderZbjCategoryDto> getCategory(){
        SpiderZbjCategoryDto spiderZbjCategoryDto = new SpiderZbjCategoryDto();
        spiderZbjCategoryDto.setLevel(2);
        spiderZbjCategoryDto.setStatus(10);
        List<SpiderZbjCategoryDto> all = spiderZbjCategoryRepository.findAll(Example.of(spiderZbjCategoryDto), Sort.by(Sort.Direction.ASC, "createDate"));
        return all;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.getRequest().getUrl();
        log.info("get page: {}", url);

        List<SpiderZbjServiceListDto> spiderZbjServiceListDtos = (List<SpiderZbjServiceListDto>)resultItems.get("one");
        log.info("save data: {}", JSON.toJSONString(spiderZbjServiceListDtos));

        SpiderZbjCategoryDto spiderZbjCategoryDto = new SpiderZbjCategoryDto();
        spiderZbjCategoryDto.setHref(url);
        List<SpiderZbjCategoryDto> all = spiderZbjCategoryRepository.findAll(Example.of(spiderZbjCategoryDto));
        List<SpiderZbjCategoryDto> collect = all.stream().map(x -> {
            if(CollectionUtils.isEmpty(spiderZbjServiceListDtos)){
                x.setStatus(3);
            } else {
                x.setStatus(11);
            }
            x.setUpdateDate(new Date());
            return x;
        }).collect(Collectors.toList());
        log.info("update url status: {}", JSON.toJSONString(all));
        spiderZbjCategoryRepository.saveAll(collect);

        if(!CollectionUtils.isEmpty(spiderZbjServiceListDtos)){
            serviceListRepository.saveAll(spiderZbjServiceListDtos);
        }
    }

    @Override
    public void process(Page page) {
        Elements elementsByClass = page.getHtml().getDocument().body().getElementsByClass("item-wrap");
        if(elementsByClass.size() > 0){
            ArrayList<SpiderZbjServiceListDto> spiderZbjServiceListDtos = new ArrayList<>();

            Iterator<Element> iterator = elementsByClass.iterator();
            while (iterator.hasNext()){
                SpiderZbjServiceListDto spiderZbjServiceListDto = new SpiderZbjServiceListDto();
                Element next = iterator.next();
                String shopId = next.attr("data-shop-id");
                spiderZbjServiceListDto.setShopId(shopId);
                String userId = next.getElementsByClass("witkey-hover").attr("data-userid");
                spiderZbjServiceListDto.setUserId(userId);
                String title = next.getElementsByClass("title").get(0).text();
                spiderZbjServiceListDto.setTitle(title);
                Elements elementsByClass1 = next.getElementsByClass("info-item");
                //综合评分
                String score1 = elementsByClass1.get(0).getElementsByClass("score").text();
                spiderZbjServiceListDto.setScore(score1);
                //人才90天成交额
                String turnoverAmount = elementsByClass1.get(1).getElementsByClass("score").text();
                spiderZbjServiceListDto.setTurnoverAmount(turnoverAmount);
                //开店时间
                String shopYear = elementsByClass1.get(2).getElementsByClass("score").text();
                spiderZbjServiceListDto.setShopYear(shopYear);
                //擅长技能
                ArrayList<String> skills = new ArrayList<>();
                Elements elementsByClass2 = next.getElementsByClass("specialty-wrap").get(0).getElementsByTag("span");
                Iterator<Element> iterator1 = elementsByClass2.iterator();
                while (iterator1.hasNext()){
                    skills.add(iterator1.next().text());
                }
                spiderZbjServiceListDto.setSkills(String.join(",", skills));
                String city = next.getElementsByClass("city-icon").get(0).getElementsByTag("span").get(0).text();
                spiderZbjServiceListDto.setCity(city);
                String level = next.getElementsByClass("ico-level").text();
                spiderZbjServiceListDto.setLevel(level);
                String userBusiness = next.getElementsByClass("ico-user-business").text();
                spiderZbjServiceListDto.setUserBusiness(userBusiness);
                String bzBorder = next.getElementsByClass("bz-border").text();
                spiderZbjServiceListDto.setBzBorder(bzBorder);
                String witkey = next.getElementsByClass("witkey-dync").text();
                spiderZbjServiceListDto.setWitkey(witkey);
                String userImpression = next.getElementsByClass("user-impression").get(0).getElementsByTag("a").attr("href");
                spiderZbjServiceListDto.setUserImpression(userImpression);

                spiderZbjServiceListDto.setItemUrl(page.getRequest().getUrl());
                spiderZbjServiceListDtos.add(spiderZbjServiceListDto);
            }

            page.putField("one", spiderZbjServiceListDtos);

            /**
             * 处理分页
             *
             */
            Element body = page.getHtml().getDocument().body();
            String text = body.getElementsByClass("pagination").get(0).getElementsByClass("active").text();
            if(Integer.parseInt(text) == 1){
                String host = "https://shenzhen.zbj.com";
                Elements elementsByTag = body.getElementsByClass("pagination").get(0).getElementsByTag("a");
                String attr = elementsByTag.get(2).attr("href");
                if(attr.indexOf("javascript") <= -1){
                    String pk = attr.split("/")[2].replace("pk", "").replace(".html", "");

                    String totalPage = elementsByTag.last().attr("data-total");
                    ArrayList<String> arrayList = new ArrayList<>();
                    for(int i=1;i<=Integer.parseInt(totalPage);i++){
                        if(i == 1){
                            arrayList.add(host + attr);
                        } else {
                            String[] split = attr.split("/");
                            String s = host + "/" + split[0] + split[1] + "/pk" + (Integer.parseInt(pk) + (i * 40 - 40)) + ".html";
                            arrayList.add(s);
                        }
                    }
                    log.info("addTargetRequests page info url: {}", JSON.toJSONString(arrayList));
                    page.addTargetRequests(arrayList);
                }
            }

        }

    }

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
        return site;
    }

    private final Spider spider = Spider.create(this).thread(1);

    public void start(){
        List<SpiderZbjCategoryDto> category = this.getCategory();
        if(CollectionUtils.isEmpty(category)){
            return;
        }
        List<String> collect = category.stream().map(x -> x.getHref()).collect(Collectors.toList());
        spider.addPipeline(this);
        spider.startUrls(collect);
        spider.start();
    }

    public void stop(){
        spider.stop();
        spider.close();
    }

    public void getStatus(){
        spider.getStatus();
    }
}