package com.genesis.x.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/9/6 14:21
 * @Description: 概念股票 查询的数据
 * http://q.10jqka.com.cn/gn/
 */
@Data
@Entity
@Table(name = "spider_ths_gainian_share")
public class GaiNianShareDto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "url")
    private String url;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "today_price")
    private Double todayPrice;

    @Column(name = "yesterday_price")
    private Double yesterdayPrice;

    @Column(name = "min_price")
    private Double minPrice;

    @Column(name = "max_price")
    private Double maxPrice;

    /**
     * 成交量
     */
    @Column(name = "volume")
    private Double volume;

    /**
     * 涨幅
     */
    @Column(name = "increase_rate")
    private Double increaseRate;

    /**
     * 排名
     */
    @Column(name = "ranking")
    private Integer ranking;

    /**
     * 资金净流入
     */
    @Column(name = "capital_inflows")
    private Double capitalInflows;

    /**
     * 成交额
     */
    @Column(name = "turnover")
    private Double turnover;

}