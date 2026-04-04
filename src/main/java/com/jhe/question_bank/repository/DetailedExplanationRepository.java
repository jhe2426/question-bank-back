package com.jhe.question_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.DetailedExplanationEntity;

@Repository
public interface DetailedExplanationRepository extends JpaRepository<DetailedExplanationEntity, Integer> {
    
}
