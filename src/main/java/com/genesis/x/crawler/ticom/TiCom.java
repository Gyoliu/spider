package com.genesis.x.crawler.ticom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.genesis.x.crawler.HttpClientDownloader;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Iterator;

/**
 * @Author liuxing
 * @Date 2021/6/15 9:16
 * @Version 1.0
 * @Description:
https://www.ti.com/product/TMUX7213?jktype=homepageproduct
 */
public class TiCom {



    public static void main(String[] args) throws InterruptedException {
        String ROOT_URL = "https://www.ti.com/product/TMUX7213?jktype=homepageproduct";
        int i = 0;
        while (true){
            a(ROOT_URL);
            Thread.sleep(1000);
            i ++ ;
            if(i == 500){
                break;
            }
        }

    }

    private static void a(String ROOT_URL){
        Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                System.out.println(page.getRawText());
                Elements script = page.getHtml().getDocument().getElementsByTag("ti-order-now-table");
                Iterator<Element> iterator = script.iterator();
                while (iterator.hasNext()){
                    Element script1 = iterator.next().tagName("script");
                    JSONObject jsonObject = JSON.parseObject(script1.data(), Feature.DisableSpecialKeyDetect);
                    JSONArray offers = jsonObject.getJSONArray("offers");

                    for (int i = 0; i < offers.size(); i++) {
                        JSONObject jsonObject1 = offers.getJSONObject(i).getJSONObject("itemOffered");
                        System.out.println("产品名称：" + jsonObject1.getString("name"));
                        System.out.println("SKU名称：" + jsonObject1.getString("sku"));

                        JSONObject offers1 = jsonObject1.getJSONObject("offers");
                        System.out.println("单价：" + offers1.getString("price"));
                        System.out.println("币种：" + offers1.getString("priceCurrency"));
                        System.out.println("库存水平：" + offers1.getString("inventoryLevel"));
                        System.out.println("URL：" + offers1.getString("url"));

                        // 价格区间
                        JSONArray priceSpecification = offers1.getJSONArray("priceSpecification");
                        if(priceSpecification == null){
                            continue;
                        }
                        for (int i1 = 0; i1 < priceSpecification.size(); i1++) {
                            JSONObject jsonObject2 = priceSpecification.getJSONObject(i1);
                            String minValue = jsonObject2.getJSONObject("eligibleQuantity").getString("minValue");
                            String maxValue = jsonObject2.getJSONObject("eligibleQuantity").getString("maxValue");
                            String price = jsonObject2.getString("price");
                            String priceCurrency = jsonObject2.getString("priceCurrency");
                            System.out.println(String.format("价格区间：%s-%s %s %s", minValue, maxValue, price, priceCurrency));
                        }
                    }
                }
            }

            @Override
            public Site getSite() {
                Site site = Site.me().setRetryTimes(0).setSleepTime(1000).setTimeOut(30000);
                site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
                site.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                site.addHeader("Accept-Encoding","gzip, deflate, br");
                site.addHeader("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
                site.addHeader("Cache-Control","no-cache");
                site.addHeader("Connection","keep-alive");
                site.addHeader("Cookie","ti_geo=country=CN|city=GUANGZHOU|continent=AS|tc_ip=119.147.212.244; ti_ua=Mozilla%2f5.0%20(Windows%20NT%2010.0%3b%20Win64%3b%20x64)%20AppleWebKit%2f537.36%20(KHTML,%20like%20Gecko)%20Chrome%2f87.0.4280.88%20Safari%2f537.36; ti_bm=; CONSENTMGR=ts:1623719145607%7Cconsent:true; tiSessionID=017a0d33f08b0020201e6a8e147003072003a06a00bd0; gpn=TMUX7213-gpn; alias=homepageproduct; tipage=%2Fanalog%20%26%20mixed-signal%2Fswitches%20%26%20multiplexers%2Fanalog%20switches%20%26%20muxes%2Ftmux7213%2Fproduct%20folder-tmux7213-en; tipageshort=product%20folder-tmux7213-en; ticontent=%2Fanalog%20%26%20mixed-signal%2Fswitches%20%26%20multiplexers%2Fanalog%20switches%20%26%20muxes%2Ftmux7213; ga_page_cookie=product%20folder-tmux7213-en; ga_content_cookie=%2Fanalog%20%26%20mixed-signal%2Fswitches%20%26%20multiplexers%2Fanalog%20switches%20%26%20muxes%2Ftmux7213; last-domain=www.ti.com; user_pref_language=\"en-US\"; pf-accept-language=en-US; ak_bmsc=3A5FB5E97D39874ADF4F7701B50715C4~000000000000000000000000000000~YAAQHYyUG//MXGp5AQAA6OgzDQyWRVJadZlOXaFSVeCF1HHpmLOtM0rP9X3tOx0ST40bvTn9Z9XiCGglLYoCDEqlbjdL+s98YPCcbwueH/fJ3zBi0p1Ez3+979YfzJAE2REBy8TLVUC5hp+ZZ93U1U+vGgGuQXntY8XCEIfTebuCW824kY23YmYVYCuoNDQAT7uBCJfJINGfP9zb8Kwltsq6n+Omc7tp/BkvDHeG1/OKO0YEkPI9HzsDe91cpNPWokVXZnMmNnbaRewCeb3fdUw8T8wIN6WEp0j79qZhfWLEzUUEhebL7q1CgMz4RVAXsZX4OwCgEFjru73XJ8s7EUJ/ukJbJBUpgPJHCVBCjpvetEsESYqeAYiJXdSxIn97QicDP5vGoCFHd0re6cZhCMsYGV6gaK5BrCMVXmgMdryun9N6eL9lHGJji0XMSpyupeSNhP+GZnvmdottG5sPI7b99qd0Ryf/iChTatz2; seerses=e; seerid=u_714112917583620100; da_sid=CFE90B648E33AE8F4178AA134E5B4C6C3D|4|0|3; da_lid=FCDA38579A73EA14D429BB990C5906678E|0|0|0; da_intState=; ELOQUA=GUID=868E461EEB6E471298D5772392164857; bm_sv=CF8BE0667F73A3E278125A60F3547F7D~3ZYPw2Ww0r6IbB4M1l6MAnnvogpbTkiSybwOw0Xxf1wTXMafLndyCGcWnVUaEQwJaF0X2m8/J024e/lwbJlCPiYrQw2FsKP8cfq19yAcCZDxbsyxSY+WrKNGPQy7/3zTvCd5m6qASTnhKZNhQmflDw==; ABTasty=uid=gsb4jwey28wj33wy&fst=1623719147687&pst=-1&cst=1623719147687&ns=0&pvt=5&pvis=5&th=; ABTastySession=mrasn=&sen=4&lp=https%253A%252F%252Fwww.ti.com%252Fproduct%252FTMUX7213%253Fjktype%253Dhomepageproduct; utag_main=v_id:017a0d33f08b0020201e6a8e147003072003a06a00bd0$_sn:1$_ss:0$_pn:6%3Bexp-session$_st:1623721348442$ses_id:1623719145611%3Bexp-session$free_trial:false; ti_rid=da990b3");
                site.addHeader("Host","www.ti.com");
                site.addHeader("Pragma","no-cache");
                site.addHeader("Sec-Fetch-Dest","document");
                site.addHeader("Sec-Fetch-Mode","navigate");
                site.addHeader("Sec-Fetch-Site","none");
                site.addHeader("Sec-Fetch-User","?1");
                site.addHeader("Upgrade-Insecure-Requests","1");
                return site;
            }
        }).setDownloader(new HttpClientDownloader()).addUrl(ROOT_URL).addPipeline(new ConsolePipeline()).thread(1).start();
    }
}
