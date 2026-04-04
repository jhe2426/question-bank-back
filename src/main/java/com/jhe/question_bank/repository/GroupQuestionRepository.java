package com.jhe.question_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhe.question_bank.common.entity.GroupQuestionEntity;
import com.jhe.question_bank.repository.resultSet.GetExamResultSet;

@Repository
public interface  GroupQuestionRepository extends JpaRepository<GroupQuestionEntity, Integer>{
    
    @Query(value = """
            SELECT
                Q.question_id as questionId,
                GQ.question_order as questionOrder,
                Q.question_text as questionText,
                Q.image_url as imageUrl,
                Q.option_1 as option1,
                Q.option_2 as option2,
                Q.option_3 as option3,
                Q.option_4 as option4,
                Q.answer as answer,
                DE.content as detailExplanationContent,
                GQ.user_answer as userAnswer,
                GQ.is_correct as isCorrect
            FROM group_questions GQ
            JOIN questions Q ON GQ.question_id = Q.question_id
            LEFT JOIN detailed_explanations DE ON Q.detail_explanation_id = DE.detail_explanation_id
            WHERE GQ.group_id = :groupId
                AND (
                    :filter = 'all'
                    OR (:filter = 'incorrect' AND GQ.is_correct = 0)
                )
            ORDER BY GQ.question_order ASC
            """, nativeQuery = true)
    List<GetExamResultSet> findExamResultByGroupId(@Param("groupId") Integer groupId, @Param("filter") String filter);

}
