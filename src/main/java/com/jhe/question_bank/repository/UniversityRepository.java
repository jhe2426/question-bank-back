package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhe.question_bank.common.entity.UniversityEntity;

public interface UniversityRepository extends JpaRepository<UniversityEntity, Integer> {
    List<UniversityEntity> findAllByOrderByUniversityIdAsc();
}
