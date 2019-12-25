package com.genesis.x.crawler.xueqiu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.genesis.x.dto.xueqiu.XueqiuASDto;
import com.genesis.x.repository.XueqiuASDtoRepository;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2019/12/4 14:28
 * @Description:
 */
@Slf4j
@Component
public class SymbolMonth implements PageProcessor, Pipeline {

    @Autowired
    private XueqiuASDtoRepository xueqiuASDtoRepository;

    public List<XueqiuASDto> querySymbol(){
        List<XueqiuASDto> all = xueqiuASDtoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.lt(root.get("current"), 30));
            predicates.add(criteriaBuilder.gt(root.get("current"), 5));
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            return criteriaQuery.getRestriction();
        });
        Map<String, String> collect = all.stream().collect(Collectors.toMap(XueqiuASDto::getSymbol, XueqiuASDto::getName));
        log.info("[要查询的股票,总数：{}, 数据：{}]", collect.size(), JSON.toJSONString(collect));
        return all;
    }

    private static final String ROOT_URL = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=%s&begin=%s&period=month&type=before&count=-142&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
    public static final String path = "D:\\works\\genesisx\\spider\\logs\\webmagic\\";
    private final Spider spider = Spider.create(this).addPipeline(this).thread(1);
    private int count = 0;
    private List<String> urls = new ArrayList<>();

    public void start(){
        List<XueqiuASDto> xueqiuASDtos = this.querySymbol();
        for (XueqiuASDto xueqiuASDto : xueqiuASDtos) {
            String url = ROOT_URL;
            url = String.format(url, xueqiuASDto.getSymbol(), System.currentTimeMillis() + (24 * 60 * 60 * 1000));
            log.info("要抓取的url:{}", url);
            spider.addUrl(url);
        }
        count = xueqiuASDtos.size();
        // spider.addUrl("https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=SZ300014&begin=1575535846342&period=month&type=before&count=-142&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance");
        spider.start();
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(60000);
        site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        site.addHeader("Accept","*/*");
        site.addHeader("Accept-Encoding","gzip, deflate, br");
        site.addHeader("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        site.addHeader("Cache-Control","no-cache");
        site.addHeader("Connection","keep-alive");
        site.addHeader("Host","stock.xueqiu.com");
        site.addHeader("Origin","https://xueqiu.com");
        site.addHeader("Pragma","no-cache");
        site.addHeader("Referer","https://xueqiu.com/hq");
        site.addHeader("Sec-Fetch-Mode","cors");
        site.addHeader("Sec-Fetch-Site","same-site");
        site.addHeader("X-Requested-With","XMLHttpRequest");
        site.addHeader("Cookie","xq_a_token=5e0d8a38cd3acbc3002589f46fc1572c302aa8a2; xqat=5e0d8a38cd3acbc3002589f46fc1572c302aa8a2; xq_r_token=670668eda313118d7214487d800c21ad0202e141; u=741575440318873; Hm_lvt_1db88642e346389874251b5a1eded6e3=1572850183,1572860762,1575440320; device_id=24700f9f1986800ab4fcc880530dd0ed; s=dp12dtrcqt; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1575440516");
        return site;
    }
    public static String PATH_SEPERATOR = "/";
    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.get("data").toString();
        urls.add(url);
        if(!"nodata".equals(url)){
            String symbol = SymbolMonth.getParam(url, "symbol");
            XueqiuASDto xueqiuASDto = new XueqiuASDto();
            xueqiuASDto.setSymbol(symbol);
            XueqiuASDto xueqiuASDto1 = xueqiuASDtoRepository.findOne(Example.of(xueqiuASDto)).get();
            if(xueqiuASDto1 != null){
                xueqiuASDto1.setChecked(1);
                xueqiuASDtoRepository.saveAndFlush(xueqiuASDto1);
            }
        }
        if(urls.size() >= count){
            String path = SymbolMonth.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;

            try {
                PrintWriter e = new PrintWriter(new FileWriter(this.getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".json")));
                e.write(JSON.toJSONString(urls));
                e.close();
            } catch (IOException var5) {
                log.warn("write file error", var5);
            }
        }
    }

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText, JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("item");
        List<Float> integers = new ArrayList<>();
        int i1 = jsonArray.size() - 12;
        for (int i = i1; i1 > 0 && i < jsonArray.size(); i++) {
            integers.add(Float.parseFloat(jsonArray.getJSONArray(i).get(5).toString()));
        }
        boolean increase = false;
        if(integers.size() > 6){
            increase = this.isIncrease(integers, integers.size(), 3);
        }
        if(increase){
            log.info("计算的URL：{}", page.getUrl().get());
            page.putField("data", page.getUrl().get());
        } else {
            page.putField("data", "nodata");
        }
    }

    private boolean isIncrease(List<Float> list, int n, int k){
        if(n <= 2){
            return true;
        }
        boolean b = list.get(n - 1) > list.get(n - 2);
        if(!b && k > 0){
            k--;
            b = true;
        }
        return (b && isIncrease(list, n - 1, k));
    }

    public File getFile(String fullName) {
        this.checkAndMakeParentDirecotry(fullName);
        return new File(fullName);
    }

    public void checkAndMakeParentDirecotry(String fullName) {
        int index = fullName.lastIndexOf(PATH_SEPERATOR);
        if(index > 0) {
            String path = fullName.substring(0, index);
            File file = new File(path);
            if(!file.exists()) {
                file.mkdirs();
            }
        }
    }

    public static String getParam(String url, String name) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        return split.get(name);
    }
}