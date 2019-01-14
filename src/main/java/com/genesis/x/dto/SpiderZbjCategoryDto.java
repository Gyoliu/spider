package com.genesis.x.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/1/8 17:47
 * @Description:
 */
@Data
@Entity
@Table(name = "spider_zbj_category")
public class SpiderZbjCategoryDto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "level")
    private Integer level;

    @Column(name = "title")
    private String title;

    @Column(name = "href")
    private String href;

    @Column(name = "parent")
    private String parent;

    @Column(name = "hot")
    private Boolean hot;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "status")
    private Integer status;

}