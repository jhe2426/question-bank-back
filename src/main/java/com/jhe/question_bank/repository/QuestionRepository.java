package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    QuestionEntity findByQuestionId(Integer questionId);

    int countByQuestionTypeAndDifficulty(String questionType, String difficulty);

    @Query("SELECT q.questionId FROM questions q WHERE q.questionType = :type AND q.difficulty = :difficulty")
    List<Integer> findIdsByTypeAndDifficulty(@Param("type") String type, @Param("difficulty") String difficulty);

    List<QuestionEntity> findAllByQuestionIdIn(List<Integer> questionIds);

    @Query("""
            SELECT q.questionId FROM questions q
            WHERE q.chapterId = :chapterId
                AND questionType = '모의고사'
                AND q.difficulty = :difficulty
                AND NOT EXISTS (
                    SELECT 1 FROM userSolvedHistory ush
                    WHERE ush.questionId = q.questionId
                    AND ush.userId = :userId
                    AND ush.sourceType = :sourceType
                    AND ush.sessionId = :sessionId
                )
            """)
    List<Integer> findUnsolvedMockExamQuestionIds(
        @Param("chapterId") Integer chapterId,
        @Param("difficulty") String difficulty,
        @Param("userId") String userId,
        @Param("sourceType") String sourceType,
        @Param("sessionId") Integer sessionId
    );

    @Query("""
            SELECT CASE
                WHEN COUNT(DISTINCT ush.questionId) = (
                    SELECT COUNT(q.questionId)
                    FROM questions q
                    WHERE q.questionType = '모의고사'
                )
                THEN true
                ELSE false
            END
            FROM userSolvedHistory ush
            WHERE ush.userId = :userId
                AND ush.sourceType = :sourceType
                AND ush.sessionId = :sessionId
            """)
    boolean isAllMockExamSolved(
        @Param("userId") String userId,
        @Param("sourceType") String sourceType,
        @Param("sessionId") Integer sessionId
    );
}
