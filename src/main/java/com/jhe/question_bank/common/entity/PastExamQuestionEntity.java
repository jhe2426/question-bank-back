package com.jhe.question_bank.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="pastExamQuestions")
@Table(name="past_exam_questions")
@Getter
@NoArgsConstructor
public class PastExamQuestionEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer pastExamQuestionId;
    private Integer questionId;
    private Integer pastExamId;
    private Integer questionOrder;

    public PastExamQuestionEntity(Integer questionId, Integer pastExamId, Integer questionOrder) {
        this.questionId = questionId;
        this.pastExamId = pastExamId;
        this.questionOrder = questionOrder;
    }
}
