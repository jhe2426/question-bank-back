package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jhe.question_bank.common.entity.UserSolvedHistoryEntity;

public interface UserSolvedHistoryRepository extends JpaRepository<UserSolvedHistoryEntity, Integer> {
    int countByUserIdAndSessionIdAndSourceType(String userId, Integer sessionId, String sourceType);

    @Query("SELECT ush.questionId FROM userSolvedHistory ush WHERE ush.userId = :userId AND ush.sessionId = :sessionId AND ush.sourceType = :sourceType")
    List<Integer> findSolvedQuestionIdsBySessionId(@Param("userId") String userId, @Param("sessionId") Integer sessionId, @Param("sourceType") String sourceType);
}
