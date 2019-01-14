package com.genesis.x.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/1/10 16:51
 * @Description:
 */
@Data
@Entity
@Table(name = "spider_zbj_servicelist")
public class SpiderZbjServiceListDto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "shop_id")
    private String shopId;

    @Column(name = "title")
    private String title;

    @Column(name = "score")
    private String score;

    @Column(name = "turnover_amount")
    private String turnoverAmount;

    @Column(name = "shop_year")
    private String shopYear;

    @Column(name = "skills", length = 500)
    private String skills;

    @Column(name = "city")
    private String city;

    @Column(name = "level")
    private String level;

    @Column(name = "user_business")
    private String userBusiness;

    @Column(name = "bz_border")
    private String bzBorder;

    @Column(name = "witkey")
    private String witkey;

    @Column(name = "user_impression")
    private String userImpression;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "item_url")
    private String itemUrl;


}