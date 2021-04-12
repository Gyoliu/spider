package com.genesis.x.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.genesis.x.service.SpringContextHolder;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liuxing
 * @Date 2021/4/12 14:37
 * @Version 1.0
 * @Description:
 */
@Configuration
@DependsOn(value = "springContextHolder")
@EnableConfigurationProperties
public class DataSourceBuilder {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource dataSource(){
        return new DruidDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource1")
    public DruidDataSource dataSource1(){
        return new DruidDataSource();
    }

    @Bean
    public DynamicDataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        DruidDataSource dataSource = SpringContextHolder.getBean("dataSource", DruidDataSource.class);
        dynamicDataSource.setDefaultTargetDataSource(dataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("dataSource", dataSource);
        targetDataSources.put("dataSource1", SpringContextHolder.getBean("dataSource1", DruidDataSource.class));
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

}
