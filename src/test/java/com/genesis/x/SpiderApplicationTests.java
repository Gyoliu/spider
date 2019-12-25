package com.genesis.x;

import com.genesis.x.crawler.xueqiu.SymbolMonth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {

	@Autowired
	private SymbolMonth symbolMonth;

	@Test
	public void contextLoads() {
		symbolMonth.start();
	}

}

