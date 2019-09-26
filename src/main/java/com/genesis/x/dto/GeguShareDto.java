package com.genesis.x.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/9/6 14:21
 * @Description: 概念列表下的 股票
 */
@Data
@Entity
@Table(name = "spider_ths_gegu_share")
public class GeguShareDto {

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

    /**
     * 涨跌幅度 %
      */
    @Column(name = "up_or_down_rate")
    private Double upOrDownRate;

    /**
     * 涨跌价
     */
    @Column(name = "up_or_down_price")
    private Double upOrDownPrice;

    /**
     * 涨速 %
     */
    @Column(name = "up_or_down_velocity")
    private Double upOrDownVelocity;

    /**
     * 换手
     */
    @Column(name = "change_hands")
    private Double changeHands;

    @Column(name = "r_id")
    private String rid;


}