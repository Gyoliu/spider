package com.genesis.x;

import com.genesis.x.crawler.xueqiu.SymbolMonth;
import com.genesis.x.service.WorkdayService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {

	@Autowired
	private WorkdayService service;

	@Test
	public void contextLoads() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.HOUR_OF_DAY, 20);
		Date workdayTime = service.getWorkdayTime(instance.getTime(), 0, 8);
		System.out.println(DateFormatUtils.format(workdayTime, "yyyy-MM-dd HH:mm:ss"));
	}

}

