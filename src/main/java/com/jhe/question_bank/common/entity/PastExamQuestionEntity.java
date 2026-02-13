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
@AllArgsConstructor
public class PastExamQuestionEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer pastExamQuestionId;
    private Integer questionId;
    private Integer pastExamRound;
    private Integer questionOrder;
}
