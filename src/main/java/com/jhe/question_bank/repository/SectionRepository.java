package com.jhe.question_bank.repository;


import com.jhe.question_bank.common.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository  extends JpaRepository<SectionEntity, Integer> {
}
