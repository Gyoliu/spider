package com.genesis.x.repository;

import com.genesis.x.dto.ZbjDemandDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: liuxing
 * @Date: 2019/1/14 15:25
 * @Description:
 */
@Repository
public interface ZbjDemandRepository extends JpaRepository<ZbjDemandDto, Integer > {
}