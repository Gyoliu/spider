package com.genesis.x.repository;

import com.genesis.x.dto.SpiderZbjCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: liuxing
 * @Date: 2019/1/9 17:20
 * @Description:
 */
@Repository
public interface SpiderZbjCategoryRepository extends JpaRepository<SpiderZbjCategoryDto, Integer > {
}