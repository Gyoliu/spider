package com.genesis.x.dto.xueqiu;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2019/12/26 9:33
 * @Description:
 */
@Data
@Entity
@Table(name = "spider_xueqiu_day_hot")
public class HotDto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "name")
    private String name;

    /**
     * 当前单价
     */
    @Column(name = "current")
    private Double current;

    /**
     * 涨跌额
     */
    @Column(name = "chg")
    private Double chg;

    /**
     * 涨跌幅
     */
    @Column(name = "percent")
    private Double percent;

    @Column(name = "type")
    private String type;

}