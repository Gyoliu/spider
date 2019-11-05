package com.genesis.x.dto.xueqiu;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/11/1 17:25
 * @Description: 雪球 沪深一览
 */
@Data
@Entity
@Table(name = "spider_xueqiu_as")
public class XueqiuASDto {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    /**
     * 成交额
     */
    @Column(name = "amount")
    private Double amount;

    @Column(name = "amplitude")
    private Double amplitude;

    @Column(name = "chg")
    private Double chg;

    @Column(name = "current")
    private Double current;

    @Column(name = "current_year_percent")
    private Double current_year_percent;

    @Column(name = "dividend_yield")
    private Double dividend_yield;

    @Column(name = "first_percent")
    private Double first_percent;

    @Column(name = "float_market_capital")
    private Double float_market_capital;

    @Column(name = "float_shares")
    private Double float_shares;

    @Column(name = "followers")
    private Double followers;

    @Column(name = "has_follow")
    private Boolean has_follow;

    @Column(name = "income_cagr")
    private Double income_cagr;

    @Column(name = "issue_date_ts")
    private Double issue_date_ts;

    @Column(name = "lot_size")
    private Double lot_size;


    @Column(name = "main_net_inflows")
    private Double main_net_inflows;

    @Column(name = "market_capital")
    private Double market_capital;

    /**
     * 股票名称
     */
    @Column(name = "name")
    private String name;

    @Column(name = "net_profit_cagr")
    private Double net_profit_cagr;

    @Column(name = "pb")
    private Double pb;

    @Column(name = "pb_ttm")
    private Double pb_ttm;

    @Column(name = "pcf")
    private Double pcf;

    @Column(name = "pe_ttm")
    private Double pe_ttm;

    @Column(name = "percent")
    private Double percent;

    @Column(name = "percent5m")
    private Double percent5m;

    @Column(name = "ps")
    private Double ps;

    @Column(name = "roe_ttm")
    private Double roe_ttm;

    /**
     * 股票代码
     */
    @Column(name = "symbol")
    private String symbol;

    @Column(name = "tick_size")
    private Double tick_size;

    @Column(name = "total_percent")
    private Double total_percent;

    @Column(name = "total_shares")
    private Double total_shares;

    @Column(name = "turnover_rate")
    private Double turnover_rate;

    @Column(name = "type")
    private Double type;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "volume_ratio")
    private Double volume_ratio;

}