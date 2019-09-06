package com.genesis.x.repository;

import com.genesis.x.dto.GaiNianShareDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: liuxing
 * @Date: 2019/9/6 15:02
 * @Description:
 */
@Repository
public interface GaiNianShareRepository extends JpaRepository<GaiNianShareDto, Integer > {
}