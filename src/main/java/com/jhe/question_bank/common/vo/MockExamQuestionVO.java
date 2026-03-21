package com.jhe.question_bank.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.jhe.question_bank.common.entity.QuestionEntity;

import lombok.Getter;

@Getter
public class MockExamQuestionVO extends QuestionVO{
    private String shortExplanation;

    private MockExamQuestionVO() {
        super();
    }

    private MockExamQuestionVO(QuestionEntity questionEntity, int questionOrder , boolean hasExplanation) {
        super(questionEntity, questionOrder);
        this.shortExplanation = hasExplanation ? questionEntity.getShortExplanation() : null;
    }

    public static List<MockExamQuestionVO> getMockExamQuestionList(List<QuestionEntity> questionEntities, boolean hasExplanation) {

        List<MockExamQuestionVO> list = new ArrayList<>();

        for (int index = 0; index < questionEntities.size(); index++) {
            MockExamQuestionVO vo = new MockExamQuestionVO(questionEntities.get(index), index+1, hasExplanation);
            list.add(vo);
        }

        return list;
    }
}
