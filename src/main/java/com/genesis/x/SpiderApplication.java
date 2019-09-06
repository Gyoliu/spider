package com.genesis.x;

import com.genesis.x.crawler.tonghuashun.GaiNian;
import com.genesis.x.crawler.zbj.ZbjAllCategory;
import com.genesis.x.crawler.zbj.ZbjDemandList;
import com.genesis.x.crawler.zbj.ZbjServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;

@SpringBootApplication
public class SpiderApplication implements CommandLineRunner {

	@Autowired
	private ZbjAllCategory zbjAllCategory;

	@Autowired
	private ZbjServiceList zbjServiceList;

	@Autowired
	private ZbjDemandList demandList;

	@Autowired
	private GaiNian gaiNian;

	@Override
	public void run(String... strings) throws Exception {
		gaiNian.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpiderApplication.class, args);
	}

}

