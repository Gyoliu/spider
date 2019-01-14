package com.genesis.x.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/1/14 15:08
 * @Description:
 */
@Data
@Entity
@Table(name = "spider_zbj_demands")
public class ZbjDemandDto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "status")
    private Integer status;


    @Column(name = "page_url")
    private String pageUrl;

    @Column(name = "link_detail_url")
    private String linkDetail;

    @Column(name = "title")
    private String title;

    @Column(name = "topics")
    private String topics;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price")
    private String price;

    @Column(name = "item_status")
    private String itemStatus;

}