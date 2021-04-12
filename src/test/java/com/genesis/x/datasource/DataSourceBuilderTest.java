package com.genesis.x.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.genesis.x.dto.xueqiu.HotDto;
import com.genesis.x.repository.HotRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author liuxing
 * @Date 2021/4/12 14:43
 * @Version 1.0
 * @Description: 多数据源测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataSourceBuilderTest {

    @Autowired @Qualifier("dataSource")
    private DruidDataSource druidDataSource;

    @Autowired @Qualifier("dataSource1")
    private DruidDataSource druidDataSource1;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EntityManager entityManager1;

    @Autowired
    private HotRepository repository;

    @Test
    public void dataSource() {
        System.out.println(druidDataSource.getUrl());
        System.out.println(druidDataSource1.getUrl());

        DynamicDataSourceContextHolder.setDataSourceType("dataSource1");
        Query nativeQuery = entityManager1.createNativeQuery("select * from user limit 10");
        List resultList = nativeQuery.getResultList();
        Query nativeQuery2 = entityManager.createNativeQuery("select * from user limit 10");
        List resultList2 = nativeQuery.getResultList();
        DynamicDataSourceContextHolder.clearDataSourceType();

        DynamicDataSourceContextHolder.setDataSourceType("dataSource");
        Query nativeQuery1 = entityManager1.createNativeQuery("select * from sys_user limit 10");
        List resultList1 = nativeQuery.getResultList();
        DynamicDataSourceContextHolder.clearDataSourceType();

        List<HotDto> all = repository.findAll();

        System.out.println();
    }
}
