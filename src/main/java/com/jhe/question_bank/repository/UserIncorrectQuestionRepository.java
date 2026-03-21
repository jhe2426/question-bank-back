package com.jhe.question_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.UserIncorrectQuestionEntity;
import com.jhe.question_bank.common.entity.primaryKey.UserIncorrectQuestionPk;

@Repository
public interface UserIncorrectQuestionRepository extends JpaRepository<UserIncorrectQuestionEntity, UserIncorrectQuestionPk>{
    UserIncorrectQuestionEntity findByUserIdAndQuestionIdAndSourceType(String userId, Integer questionId, String sourceType);
}
