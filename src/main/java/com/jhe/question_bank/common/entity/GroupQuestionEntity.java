package com.jhe.question_bank.common.entity;

import com.jhe.question_bank.common.vo.PastExamUserAnswerVO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="groupQuestions")
@Table(name="group_questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupQuestionEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer groupQuestionId;
    private Integer groupId;
    private Integer questionId;
    private Integer userAnswer;
    private boolean isCorrect;
    private Integer questionOrder;

    public GroupQuestionEntity (PastExamUserAnswerVO userAnswerVO, boolean isCorrect, Integer questionOrder) {
        this.questionId = userAnswerVO.getQuestionId();
        this.userAnswer = userAnswerVO.getAnswer();
        this.isCorrect = isCorrect;
        this.questionOrder = questionOrder;
    }

    public void assignGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
