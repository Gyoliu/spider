package com.genesis.x.crawler.zbj;

import com.alibaba.fastjson.JSON;
import com.genesis.x.dto.SpiderZbjCategoryDto;
import com.genesis.x.repository.SpiderZbjCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.*;

/**
 * @Author: liuxing
 * @Date: 2019/1/8 16:29
 * @Description: 猪八戒网 全部类目
 * https://shenzhen.zbj.com/category
 */
@Slf4j
@Component
public class ZbjAllCategory implements PageProcessor, Pipeline {

    @Autowired
    private SpiderZbjCategoryRepository spiderZbjCategoryRepository;

    @Override
    public void process(Page page) {
        ArrayList<SpiderZbjCategoryDto> categoryDtos = new ArrayList<>();
        ArrayList<SpiderZbjCategoryDto> categoryDtos1 = new ArrayList<>();

        Elements cateItems = page.getHtml().getDocument().body().getElementsByClass("cate-item");
        if(cateItems.size() > 0 ){
            Iterator<Element> iterator = cateItems.iterator();
            while (iterator.hasNext()){
                Element next = iterator.next();
                Elements cateTitleEle = next.getElementsByClass("cate-title");
                String cateTitle = cateTitleEle.text();
                SpiderZbjCategoryDto zbjCategoryDto = new SpiderZbjCategoryDto();
                zbjCategoryDto.setTitle(cateTitle);
                zbjCategoryDto.setHref(cateTitleEle.attr("href"));
                zbjCategoryDto.setLevel(1);
                zbjCategoryDto.setStatus(1);
                zbjCategoryDto.setCreateDate(new Date());
                categoryDtos.add(zbjCategoryDto);

                Iterator<Element> iterator1 = next.getElementsByClass("info-item").iterator();
                while (iterator1.hasNext()){
                    Element next1 = iterator1.next();
                    Elements a = next1.getElementsByTag("a");
                    Elements img = next1.getElementsByTag("img");
                    String text = a.text();
                    String href = a.attr("href");
                    SpiderZbjCategoryDto zbjCategoryDto1 = new SpiderZbjCategoryDto();
                    zbjCategoryDto1.setTitle(text);
                    zbjCategoryDto1.setHref(href);
                    zbjCategoryDto1.setLevel(2);
                    zbjCategoryDto1.setHot(img.size() == 0 ? false:true);
                    zbjCategoryDto1.setParent(cateTitle);
                    zbjCategoryDto1.setStatus(1);
                    zbjCategoryDto1.setCreateDate(new Date());
                    categoryDtos1.add(zbjCategoryDto1);
                }

                Iterator<Element> iterator2 = next.getElementsByClass("cate-info-item").iterator();
                while (iterator2.hasNext()){
                    Element next1 = iterator2.next();
                    Elements a = next1.getElementsByClass("item-title").get(0).getElementsByTag("a");
                    SpiderZbjCategoryDto zbjCategoryDto2 = new SpiderZbjCategoryDto();
                    zbjCategoryDto2.setTitle(a.text());
                    zbjCategoryDto2.setHref(a.attr("href"));
                    zbjCategoryDto2.setLevel(1);
                    zbjCategoryDto2.setStatus(1);
                    zbjCategoryDto2.setCreateDate(new Date());
                    categoryDtos.add(zbjCategoryDto2);

                    Iterator<Element> iterator3 = next1.getElementsByClass("item-links").get(0).getElementsByClass("item-link").iterator();
                    while (iterator3.hasNext()){
                        Element next2 = iterator3.next();
                        Elements a1 = next2.getElementsByTag("a");
                        Elements img = next2.getElementsByTag("img");

                        SpiderZbjCategoryDto zbjCategoryDto1 = new SpiderZbjCategoryDto();
                        zbjCategoryDto1.setTitle(a1.text());
                        zbjCategoryDto1.setHref(a1.attr("href"));
                        zbjCategoryDto1.setLevel(2);
                        zbjCategoryDto1.setHot(img.size() == 0 ? false:true);
                        zbjCategoryDto1.setParent(a.text());
                        zbjCategoryDto1.setStatus(1);
                        zbjCategoryDto1.setCreateDate(new Date());
                        categoryDtos1.add(zbjCategoryDto1);
                    }
                }
            }
            page.putField("one", categoryDtos);
            page.putField("two", categoryDtos1);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("get page: {}", resultItems.getRequest().getUrl());
        Iterator var3 = resultItems.getAll().entrySet().iterator();
        while(var3.hasNext()) {
            Map.Entry entry = (Map.Entry)var3.next();
            log.info("{} : {}", entry.getKey(), JSON.toJSONString(entry.getValue()));
        }
        ArrayList<SpiderZbjCategoryDto> categoryDtos = new ArrayList<>();
        categoryDtos.addAll(resultItems.get("one"));
        categoryDtos.addAll(resultItems.get("two"));
        spiderZbjCategoryRepository.saveAll(categoryDtos);
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
        return site;
    }

    private final Spider spider = Spider.create(this).addUrl("https://shenzhen.zbj.com/category").addPipeline(this).thread(1);

    public void start(){
        spider.start();
    }

    public void stop(){
        spider.stop();
        spider.close();
    }

    public void setSpiderSuccessListener(SpiderListener spiderListener){
        List<SpiderListener> spiderListeners = new ArrayList<>();
        spiderListeners.add(spiderListener);
        spider.setSpiderListeners(spiderListeners);
    }

    public Spider.Status getStatus(){
        return spider.getStatus();
    }

//    public static void main(String[] args) {
//        ZbjAllCategory zbjAllCategory = new ZbjAllCategory();
//        Spider.create(zbjAllCategory).addUrl("https://shenzhen.zbj.com/category").addPipeline(zbjAllCategory).thread(1).run();
//    }
}