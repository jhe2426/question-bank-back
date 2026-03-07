package com.jhe.question_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.GroupQuestionEntity;

@Repository
public interface  GroupQuestionRepository extends JpaRepository<GroupQuestionEntity, Integer>{
    
}
