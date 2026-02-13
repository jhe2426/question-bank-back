package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.UnitEntity;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer>{
    List<UnitEntity> findAllByOrderByUnitIdAsc();
}
