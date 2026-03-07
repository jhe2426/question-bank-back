package com.jhe.question_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhe.question_bank.common.entity.UserSolvedHistoryEntity;

public interface UserSolvedHistoryRepository extends JpaRepository<UserSolvedHistoryEntity, Integer> {
    
}
