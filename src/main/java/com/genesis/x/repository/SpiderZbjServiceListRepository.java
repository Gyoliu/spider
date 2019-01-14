package com.genesis.x.repository;

import com.genesis.x.dto.SpiderZbjServiceListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: liuxing
 * @Date: 2019/1/11 14:37
 * @Description:
 */
@Repository
public interface SpiderZbjServiceListRepository extends JpaRepository<SpiderZbjServiceListDto, Integer > {
}