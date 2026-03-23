package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.UserIncorrectQuestionEntity;
import com.jhe.question_bank.common.entity.primaryKey.UserIncorrectQuestionPk;

@Repository
public interface UserIncorrectQuestionRepository extends JpaRepository<UserIncorrectQuestionEntity, UserIncorrectQuestionPk>{
    UserIncorrectQuestionEntity findByUserIdAndQuestionIdAndSourceType(String userId, Integer questionId, String sourceType);

    @Query("SELECT uiq.questionId FROM userIncorrectQuestions uiq WHERE uiq.userId = :userId AND uiq.sourceType = :sourceType")
    List<Integer> findIdsByUserIdAndSourceType(@Param("userId") String userId, @Param("sourceType") String sourceType);
}
