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
}
